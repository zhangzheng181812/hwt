package com.zhx.admin.orderInfo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.common.CommonResult;
import com.zhx.admin.common.util.ExcelUtils;
import com.zhx.admin.common.util.JsonUtils;
import com.zhx.admin.fileUtil.fileUtil;
import com.zhx.admin.orderInfo.mapper.OrderInfoMapper;
import com.zhx.hwtcommon.common.service.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @ClassName OrderInfoService
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/7
 * @Time 14:08
 * @Viersion 1.0
 **/
@Service
public class OrderInfoService {

    private Logger log = LoggerFactory.getLogger(String.valueOf(OrderInfoService.class));

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private CommonService commonService;

    @Value("${filePath.xlsfile}")
    private  String filePath;

    /**
     * 导入的订单信息
     * @param file  订单文件
     * @return
     */
    @Transactional
    public Map doLoadDAate(File file) {
        Map resMap = new HashMap();
        //读取数据
        Map datamap = ExcelUtils.readFile(file,file.getName(),"Batch&公司缩写&员工工号&员工姓名&产品名称&单价&数量&销售价小计&收件人姓名&快递地址&" +
                "邮编&收件人手机号&供应商名&供应商的商品编号&订单编号");
        if(datamap ==null || datamap.isEmpty()){
            resMap.put("returncode","011");
            resMap.put("msg","文件内容信息有误");
            return resMap;
        }
        if(datamap.get(ExcelUtils.ERROR)!=null){
            resMap.put("returncode","001");
            resMap.put("msg",datamap.get(ExcelUtils.ERROR));
            return resMap;
        }
        Map errormsg = (Map) datamap.get(ExcelUtils.ERRORMESSAGE);
        if(errormsg.get(ExcelUtils.ERRORNUM)!=null){
            resMap.put("returncode","002");
            resMap.put("msg","第"+String.valueOf(errormsg.get(ExcelUtils.ERRORNUM))+"行数据格式不正确");
            return resMap;
        }
        List list = (List) datamap.get(ExcelUtils.DATA);
        log.info("line:*******************************************"+list.size());
        if(list.size()<=1 ){
            resMap.put("returncode","004");
            resMap.put("msg","文档数据获取有误，请联系技术运维人员。");
            return resMap;
        }
        List subList = null;
        for(int i=list.size()-1;i>0;i--){
            if(subList==null){
                subList=new ArrayList();
            }
            Map map = (Map) list.get(i);
            String batch_no = String.valueOf(map.get("Batch")).replaceAll("[\\s|\\t|\\r|\\n]","");
            String company_abridge = String.valueOf(map.get("公司缩写"));
            String emp_no = String.valueOf(map.get("员工工号")).replaceAll("[\\s|\\t|\\r|\\n]","");
            String emp_name = String.valueOf(map.get("员工姓名"));
            String product_name = String.valueOf(map.get("产品名称"));
            Double product_price = Double.parseDouble(String.valueOf(map.get("单价")));
            String num = String.valueOf(map.get("数量")).replaceAll("[\\s|\\t|\\r|\\n]","");
            Double total_price =  Double.parseDouble(String.valueOf(map.get("销售价小计")));
            String receive_name = String.valueOf(map.get("收件人姓名"));
            String receive_address = String.valueOf(map.get("快递地址"));
            String zipcode = String.valueOf(map.get("邮编")).replaceAll("[\\s|\\t|\\r|\\n]","");
            String receive_phone = String.valueOf(map.get("收件人手机号")).replaceAll("[\\s|\\t|\\r|\\n]","");
            String supplier_name = String.valueOf(map.get("供应商名"));
            String product_no = String.valueOf(map.get("供应商的商品编号")).replaceAll("[\\s|\\t|\\r|\\n]","");
            String order_no = String.valueOf(map.get("订单编号")).replaceAll("[\\s|\\t|\\r|\\n]","");
            map.put("batch_no",batch_no);
            //公司缩写即公司编号
            map.put("org_code",company_abridge);
            map.put("ee_no",emp_no);
            map.put("emp_name",emp_name);
            map.put("product_name",product_name);
            map.put("product_price",product_price);
            map.put("num",num);
            map.put("total_price",total_price);
            map.put("receive_name",receive_name);
            map.put("receive_address",receive_address);
            map.put("zipcode",zipcode);
            map.put("receive_phone",receive_phone);
            map.put("supplier_name",supplier_name);
            map.put("goods_id",product_no);
            map.put("order_no",order_no);
            map.put("status",0);
            map.put("client_no","");
            map.put("user_id","");
            subList.add(map);
            list.remove(i);
            //每30000条批量插入一次
            if(subList.size()==30000 || (list.size()<=30000 && list.size()==1)){
                log.info("insert_start:******************"+i+"*************************"+new Date().getTime());
                int insCount = orderInfoMapper.importHwtXshStock(subList);
                log.info("********insCount********"+insCount);
                if(subList.size()!=insCount){
                    log.error("********插入数据库出现问题********");
                    resMap.put("returncode","002");
                    resMap.put("msg","数据有误，请联系技术运维人员。");
                    return resMap;
                }
                log.info("insert_end:********************"+i+"***********************"+new Date().getTime());
                subList=null;
            }
        }
        resMap.put("returncode","000");
        resMap.put("msg","插入成功");
        return resMap;
    }

    /**
     * 查询订单信息
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult selectOrderList(String param, int page, int pageSize){
        Map paramMap = new HashMap();
        if(StringUtils.isNotEmpty(param)){
            paramMap= JsonUtils.toObject(param,HashMap.class);
        }
        paramMap.put("param",param);
        PageHelper.startPage(page,pageSize);
        List pageList = orderInfoMapper.selectOrderList(paramMap);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

    /**
     * 查询订单统计
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult orderCount(String param, int page, int pageSize){
        Map paramMap = new HashMap();
        if(StringUtils.isNotEmpty(param)){
            paramMap= JsonUtils.toObject(param,HashMap.class);
        }
        paramMap.put("param",param);
        PageHelper.startPage(page,pageSize);
        List pageList = orderInfoMapper.orderCount(paramMap);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }


    /**
     * 查询采购单位订单列表
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult selectPurchaseOrgOrder(String param, int page, int pageSize){
        Map paramMap = new HashMap();
        if(StringUtils.isNotEmpty(param)){
            paramMap= JsonUtils.toObject(param,HashMap.class);
        }
        paramMap.put("param",param);
        PageHelper.startPage(page,pageSize);
        List pageList = orderInfoMapper.selectPurchaseOrgOrder(paramMap);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

    /**
     * 确认到账信息录入
     * @param param
     * @return
     */
    @Transactional
    public Map insertOrderAmount(Map param) {
        Map rspMap = new HashMap();
        if(param!=null){
            int saveSuccess = this.orderInfoMapper.insertOrderAmount(param);
            if(saveSuccess==1){
                //保存成功修改订单状态值
                param.put("status",6);
                this.orderInfoMapper.updateOrderStatusInfo(param);
                rspMap.put("resultCode","000");
                rspMap.put("msg","保存成功！");
                return rspMap;
            }
        }
        rspMap.put("resultCode","001");
        rspMap.put("msg","保存失败！");
        return rspMap;
    }

    /**
     * 生成采购明细单（先查询数据再调用生成Excel表格工具类进行下载）
     * @param orgCode   需要生成明细单的机构代码，用于查询数据
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public Map createTableOrderInfo(String orgCode){
        String[] code = orgCode.split(",");
        List<Map<String,String>> goodsList = this.orderInfoMapper.selectOrderTableList(code);
        List<Map> orgList = this.orderInfoMapper.selectOrderOrgList(code);
        String orderNo = commonService.buildOrderNo();
        Map rspMap = new HashMap();
        try {
            fileUtil fileUtil = new fileUtil();
            fileUtil.newXleFile(orderNo, goodsList, orgList, filePath);
            boolean success = this.orderInfoMapper.updateOrderStatus(code, orderNo);
            if (success){
                this.orderInfoMapper.updateOrderAmount(code,orderNo);
                rspMap.put("resultCode", "000");
                rspMap.put("msg", "success");
                rspMap.put("fileName", orderNo + ".xls");
                return rspMap;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        rspMap.put("resultCode","001");
        rspMap.put("msg","error");
        return rspMap;
    }

}
