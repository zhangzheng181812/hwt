package com.zhx.admin.eproduct.controller;


import com.zhx.admin.common.CommonResult;
import com.zhx.admin.common.util.ImageUtil;
import com.zhx.admin.eproduct.service.EProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电子码产品列表
 */
@Controller
@RequestMapping("/eProduct")
public class EProductController {
    private static final Logger log = LoggerFactory.getLogger(EProductController.class);

    //图片保存绝对路径
    @Value("${filePath.homeActivitypicsPath}")
    private String homeActivitypicsPath;
    //图片URL路径
    @Value("${filePath.showhomeActivitypicsPath}")
    private String showhomeActivitypicsPath;
    //临时
    @Value("${filePath.homeTempPath}")
    private String homeTempPath;
    @Autowired
    private EProductService eProductService;


    /**
     * 商品码进货单--显示页
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/showStockLog")
    public ModelAndView showStockLog(ModelAndView model, HttpServletRequest request) {
        model.setViewName("eProduct/stockLogPage");
        return model;
    }

    /**
     * 商品码进货单--列表
     * @param cateid
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping("/stockLogList")
    @ResponseBody
    public CommonResult toStockLogList( @RequestParam(value = "cateid", required = false)String cateid ,int page, int pageSize) throws Exception{
        return eProductService.selStockLogList(cateid, page, pageSize);
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
    @RequestMapping("/submitStockLogList")
    @ResponseBody
    public CommonResult submitStockLogList(@RequestParam(value = "id", required = true)String id,
                                           @RequestParam(value = "input_no", required = true)String input_no,
                                           @RequestParam(value = "sup_out_no", required = true)String sup_out_no,
                                           @RequestParam(value = "sup_id", required = true)String sup_id,
                                           @RequestParam(value = "cateid", required = true)String cateid,
                                           @RequestParam(value = "face_price", required = true)String face_price,
                                           @RequestParam(value = "amount", required = true)String amount,
                                           @RequestParam(value = "buy_count", required = true)String buy_count,
                                           @RequestParam(value = "create_id", required = true)String create_id,
                                           @RequestParam(value = "totalamount", required = true)String totalamount,
                                           @RequestParam(value = "expire_time", required = true)String expire_time,
                                           @RequestParam(value = "payment_type", required = true)String payment_type,
                                           @RequestParam(value = "is_pay", required = true)String is_pay) throws Exception{
        return eProductService.submitStockLogList(id,input_no,sup_out_no,sup_id,cateid,face_price,amount,buy_count,
                create_id,totalamount,expire_time,payment_type, is_pay);
    }

    /**
     * 商品码进货单--根据id查询
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getStockLogById")
    @ResponseBody
    public CommonResult getStockLogById(@RequestParam(value = "id", required = true)String id) throws Exception {
        return eProductService.getStockLogById(id);
    }



    /**
     * 显示电子码产品导入库存页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/showECodeStocking")
    public ModelAndView showECodeStocking(ModelAndView model, HttpServletRequest request) {
        model.setViewName("eProduct/stockingPage");
        return model;
    }

    /**
     * 导入数据
     *
     * @param model
     * @param suppliers   供应商ID
     * @param productFile csv文件
     * @param inputNo     进货单号
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadProductDate", produces = {"application/json;charset=UTF-8"})
    public
    @ResponseBody
    Map loadProductDate(ModelAndView model,
                        @RequestParam(value = "suppliers", required = true) String suppliers,
                        @RequestParam(value = "num", required = true) String num,
                        @RequestParam(value = "productFile", required = true) MultipartFile productFile,
                        @RequestParam(value = "inputNo", required = true) String inputNo,
                        HttpServletRequest request
    ) {
        Map resMap = null;
        File file = null;
        try {
            String fileName = productFile.getOriginalFilename().toUpperCase();
            if(!fileName.endsWith(".CSV") && !fileName.endsWith(".XLS")&& !fileName.endsWith(".XLSX")){
                if(resMap == null){
                    resMap = new HashMap();
                }
                resMap.put("returncode","009");
                resMap.put("msg","请上传csv、xls或xlsx文件");
                return resMap;
            }
            file = new File(request.getSession().getServletContext().getRealPath("/tmp"),productFile.getOriginalFilename());
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            productFile.transferTo(file);
            Date a = new Date();
            resMap = eProductService.doLoadDAate(suppliers,num,inputNo,file);
            Date b = new Date();
            log.info("************all_time*************************"+(b.getTime()-a.getTime()));
            return  resMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("----错误信息---：",e);
            if(resMap == null){
                resMap = new HashMap();
            }
            resMap.put("returncode","999");
            resMap.put("msg","程序异常，请联系管理员");
            return resMap;
        }finally {
            if (file!=null&&file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 产品详情页
     *
     * @return
     */
    @RequestMapping("/toGoodsDetail")
    public ModelAndView toGoodsDetailByGoodsIdNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("goodsDetail/detailList");
        return modelAndView;
    }

    /**
     * 产品详情页数据展示
     *
     * @param
     * @return
     */
    @RequestMapping("/toGoodsDetailList")
    @ResponseBody
    public Map toGoodsDetailList(@RequestParam(value = "goods_id", required = false) String goods_id, @RequestParam(value = "time", required = false) String time, @RequestParam(value = "status", required = false) String status, int page, int pageSize
    ) throws Exception {
        Map resultMap = new HashMap();
        Map map = new HashMap();
        if (!StringUtils.isEmpty(goods_id)) {
            map.put("goods_id", goods_id);
        }
        if (!StringUtils.isEmpty(time)) {
            map.put("create_time", time);
        }
        if (!StringUtils.isEmpty(status)) {
            map.put("status", status);
        }

        CommonResult commonResult = null;
        try {
            commonResult = eProductService.selGoodsDetail(page, pageSize, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (commonResult.isSuc()) {
            resultMap = (Map) commonResult.getData();
        }
        return resultMap;
    }

    /**
     * 图片上传
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/toGoodsDetailPic")
    @ResponseBody
    public List toPicFie(HttpServletRequest request, HttpServletResponse response) {
        String pic_src = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String todayFile = sdf.format(new Date());
        List list = new ArrayList();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        Map map = new HashMap();
                        log.info("myFileName----"+myFileName+"---");
                        if (!(myFileName.substring(myFileName.lastIndexOf("."), myFileName.length())).equals(".png") && !(myFileName.substring(myFileName.lastIndexOf("."), myFileName.length())).equals(".jpg")) {
                            map.put("error", myFileName + "上传格式错误");
                            list.add(map);
                            return list;
                        }
                        //重命名上传后的文件名
                        String newFileName = String.valueOf(Math.random()).substring(2, 13) + myFileName.substring(myFileName.indexOf("."), myFileName.length());
                        //定义上传路径
                        String path = homeTempPath + newFileName.substring(0, newFileName.indexOf(".")) + "_temp.png";
                        File localFile = new File(path);
                        File parentFile = localFile.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        try {
                            file.transferTo(localFile);
                        } catch (IOException e) {
                            log.error("========error====="+ e.getMessage());
                            e.printStackTrace();
                            map.put("error", myFileName + "上传错误");
                            pic_src = showhomeActivitypicsPath + todayFile + "/" + newFileName;
                            map.put("url", pic_src);
                            map.put("name", myFileName);
                            list.add(map);
                            return list;
                        }
                        pic_src = showhomeActivitypicsPath + todayFile + "/" + newFileName;
                        map.put("url", pic_src);
                        map.put("name", myFileName);
                        list.add(map);
                    }
                }
            }

        }
        return list;
    }

    /**
     * 添加修改产品详情
     * @param id
     * @param goods_id
     * @param topic
     * @param content
     * @param status
     * @param orderby
     * @param pic_list
     * @return
     */
    @RequestMapping("/saveGoodsInfoDetail")
    @ResponseBody
    public CommonResult saveGoodsInfoDetail(String id, String goods_id, String topic, String content, String status, String orderby, String pic_list){
        Map param = new HashMap();
        param.put("goods_id", goods_id);
        param.put("topic", topic);
        param.put("content", content);
        param.put("status", status);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayFile = sdf.format(new Date());
        param.put("orderby", orderby);
        param.put("pic_list", pic_list);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String today = sdf1.format(new Date());
        //图片移动指定目录
        try {
            if(!StringUtils.isEmpty(pic_list)) {
                for (String str : pic_list.split(",")) {
                    int c = str.lastIndexOf("/");
                    String newname = str.substring(c+1, str.length());
                    int cn = newname.lastIndexOf(".");
                    String n = newname.substring(0, cn);
                    ImageUtil.copyFile(homeTempPath+ n +"_temp.png"  , homeActivitypicsPath + today + "/" + newname);
                    String bigPic = ImageUtil.getPicStr(newname,"1080");
                    String litPic = ImageUtil.getPicStr(newname,"640");
                    ImageUtil.zoom(homeActivitypicsPath + today + "/" + newname, homeActivitypicsPath  + today + "/" + litPic, 0.667f);
                    ImageUtil.zoom(homeActivitypicsPath + today + "/" + newname, homeActivitypicsPath  + today + "/"+ bigPic, 1);
                }
            }
        } catch (Exception e) {
            log.error("---------"+e.getMessage()+"--------");
            e.printStackTrace();
        }
        int count = 0;
        if (StringUtils.isEmpty(id)) {
            param.put("create_time", todayFile);
            count = eProductService.insGoodsDetail(param);
        } else {
            param.put("id", id);
            count = eProductService.updateGoodsDetail(param);
        }
        return CommonResult.success();
    }

    /**
     * 更新详情状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/updateGoodsDetailStatus")
    @ResponseBody
    public CommonResult updateGoodsDetailStatus(String id, String status) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("status", status);
        int i =eProductService.updateGoodsDetailStatus(map);
        if(i>0){
            return CommonResult.success();
        }else {
            return CommonResult.result("101","更新失败");
        }
    }
    /**
     * 查询有效产品信息
     * @return
     */
    @RequestMapping("/selGoodsInfoId")
    @ResponseBody
    public List selGoodsInfoId(String status){
        return  eProductService.selGoodsInfoId(status);
    }

}
