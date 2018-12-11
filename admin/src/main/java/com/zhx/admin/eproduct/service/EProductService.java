package com.zhx.admin.eproduct.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.common.CommonResult;
import com.zhx.admin.common.util.ExcelUtils;
import com.zhx.admin.common.util.MathUtil;
import com.zhx.admin.common.util.TripleDESUtil;
import com.zhx.admin.eproduct.mapper.EProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * 电子码产品列表service
 */
@Service("eProductService")
public class EProductService {
    private Logger log = LoggerFactory.getLogger(String.valueOf(EProductService.class));
    @Autowired
    private EProductMapper eProductMapper;
    @Value("${site.encryptkey}")
    private String encryptkey;

    /**
     * 商品码进货单--列表
     * @param cateid
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult selStockLogList(String cateid,int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        Map param = new HashMap();
        if(!StringUtils.isEmpty(cateid)){
            param.put("cateid",cateid);
        }
        List pageList = eProductMapper.selStockLogList(param);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

    /**
     * 商品码进货单--保存
     * @param id
     * @param input_no
     * @param sup_out_no
     * @param sup_id
     * @param cateid
     * @param face_price
     * @param amount
     * @param buy_count
     * @param create_id
     * @param totalamount
     * @param expire_time
     * @param payment_type
     * @param is_pay
     * @return
     * @throws Exception
     */
    public CommonResult submitStockLogList(String id,String input_no,String sup_out_no,String sup_id,String cateid,String face_price,String amount,String buy_count,
                                           String create_id,String totalamount,String expire_time,String payment_type, String is_pay)throws Exception{
        Map param = new HashMap();
        param.put("input_no",input_no);
        param.put("sup_out_no",sup_out_no);
        param.put("sup_id",sup_id);
        param.put("cateid",cateid);
        param.put("face_price",face_price);
        param.put("amount",amount);
        param.put("buy_count",buy_count);
        param.put("create_id",create_id);
        param.put("totalamount", MathUtil.mul(amount,buy_count));
        param.put("expire_time",expire_time);
        param.put("payment_type",payment_type);
        param.put("is_pay",is_pay);
        int i = 0;
        if(StringUtils.isEmpty(id)){
           i = eProductMapper.insStockLog(param);
        }else {
            param.put("id",id);
            i = eProductMapper.updateStockLog(param);
        }
        if(i==1){
            return CommonResult.success();
        } else {
            return CommonResult.result("999","系统错误");
        }
    }

    /**
     * 商品码进货单--根据id查询
     * @param id
     * @return
     * @throws Exception
     */
    public CommonResult getStockLogById(String id) throws Exception {
        Map resultMap = eProductMapper.selStockLogById(id);
        return CommonResult.success(resultMap);
    }





    /**
     * 导入数据
     *
     * @param suppliers 供应商ID
     * @param num       产品数量
     * @param inputNo   进货单号
     * @param file
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public Map doLoadDAate(String suppliers, String num, String inputNo, File file) throws Exception{
        Map resMap = new HashMap();
        //读取数据
        Map datamap = ExcelUtils.readFile(file, file.getName(), "cate_id&card_no&ecode&amount&start_time&expire_time");
        if (datamap.get(ExcelUtils.ERROR) != null) {
            resMap.put("returncode", "001");
            resMap.put("msg", datamap.get(ExcelUtils.ERROR));
            return resMap;
        }
        Map errormsg = (Map) datamap.get(ExcelUtils.ERRORMESSAGE);
        if (errormsg.get(ExcelUtils.ERRORNUM) != null) {
            resMap.put("returncode", "002");
            resMap.put("msg", "第" + String.valueOf(datamap.get(ExcelUtils.ERROR)) + "行数据格式不正确");
            return resMap;
        }
        List list = (List) datamap.get(ExcelUtils.DATA);


        System.out.println("line:*******************************************" + list.size());
        if (list.size() != Integer.valueOf(num)) {
            resMap.put("returncode", "004");
            resMap.put("msg", "导入数量不匹配");
            return resMap;
        }
        if (Integer.valueOf(num) > 300000) {
            resMap.put("returncode", "005");
            resMap.put("msg", "单次导入不能超过30W条");
            return resMap;
        }

        List subList = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (subList == null) {
                subList = new ArrayList();
            }
            Map map = (Map) list.get(i);
            map.put("sid", suppliers);
            map.put("input_no", inputNo);
            map.put("comments", "");
            map.put("status", "04");
            String cate_id = String.valueOf(map.get("cate_id"));
            String card_no = String.valueOf(map.get("card_no")).replaceAll("[\\s|\\t|\\r|\\n]", "");
            String ecode = String.valueOf(map.get("ecode")).replaceAll("[\\s|\\t|\\r|\\n]", "");
            String amount = String.valueOf(map.get("amount"));
            map.put("cateid", cate_id);
            map.put("card_no", card_no);
            map.put("e_code_no", TripleDESUtil.getEncryptString(ecode, encryptkey));
            map.put("value_amount", amount);
            subList.add(map);
            list.remove(i);
            //每30000条批量插入一次
            if (subList.size() == 30000 || (list.size() <= 30000 && list.size() == 0)) {
                System.out.println("insert_start:******************" + i + "*************************" + new Date().getTime());
                int insCount = eProductMapper.insHwtXshStock(subList);
                System.out.println("********insCount********" + insCount);
                if (subList.size() != insCount) {
                    throw new Exception("插入数据库失败,插入数据数:"+insCount+",需要插入数:"+subList.size());
                }
                System.out.println("insert_end:********************" + i + "***********************" + new Date().getTime());
                subList = null;
            }
        }
        resMap.put("returncode", "000");
        resMap.put("msg", "插入成功");
        return resMap;
    }

    /**
     * 活动详情
     *
     * @param page
     * @param pageSize
     * @param map
     * @return
     * @throws Exception
     */
    public CommonResult selGoodsDetail(int page, int pageSize, Map map) throws Exception {
        PageHelper.startPage(page, pageSize);
        List pageList = eProductMapper.selGoodsDetail(map);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

    /**
     * 添加
     * @param
     * @return
     */
    public int insGoodsDetail(Map param){
        return eProductMapper.insGoodsDetail(param);
    }

    /**
     * 更新
     * @param param
     * @return
     */
    public int updateGoodsDetail(Map param){
        return eProductMapper.updateGoodsDetail(param);
    }

    /**
     * 查询产品名称
     * @param status
     * @return
     */
    public List selGoodsInfoId(String status){
        Map param = new HashMap();
        param.put("status",status);
        return  eProductMapper.selGoodsInfoId(param);
    }

    /**
     * 状态更新
     * @param param
     * @return
     */
    public int updateGoodsDetailStatus(Map param){
        return eProductMapper.updateGoodsDetailStatus(param);
    }

}
