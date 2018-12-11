package com.zhx.admin.orderInfo.controller;

import com.zhx.admin.common.CommonResult;
import com.zhx.admin.orderInfo.service.OrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderInfoController
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/7
 * @Time 14:06
 * @Viersion 1.0
 **/
@Controller
@RequestMapping(value = "/order")
public class OrderInfoController {
    private static final Logger log = LoggerFactory.getLogger(OrderInfoController.class);

    @Resource
    private OrderInfoService orderInfoService;

    @Value("${filePath.xlsfile}")
    private String xlspath;
    /**
     * 订单列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/showOrderList")
    public ModelAndView showECodeStocking(ModelAndView model) {
        model.setViewName("order/orderList");
        return model;
    }

    /**
     * 订单信息导入
     *
     * @param productFile 订单文件信息
     * @param request     客户端请求信息
     * @return
     */
    @RequestMapping(value = "/importOrder", method = RequestMethod.POST)
    @ResponseBody
    public Map importOrderInfo(@RequestParam(value = "productFile", required = true) MultipartFile productFile,
                               HttpServletRequest request) {

        Map resMap = null;
        File file = null;
        String fileName = productFile.getOriginalFilename().toUpperCase();
        if (!fileName.endsWith(".CSV") && !fileName.endsWith(".XLS") && !fileName.endsWith(".XLSX")) {
            if (resMap == null) {
                resMap = new HashMap();
            }
            resMap.put("returncode", "009");
            resMap.put("msg", "请上传csv、xls或xlsx文件");
            return resMap;
        }
        try {
            file = new File(request.getSession().getServletContext().getRealPath("/tmp"), productFile.getOriginalFilename());
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            productFile.transferTo(file);
            Date a = new Date();
            resMap = this.orderInfoService.doLoadDAate(file);
            Date b = new Date();
            log.info("************all_time*************************" + (b.getTime() - a.getTime()));
            return resMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("----错误信息---：",e);
            if(resMap == null){
                resMap = new HashMap();
            }
            resMap.put("returncode","999");
            resMap.put("msg","程序异常，请联系管理员");
            return resMap;
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 查询订单信息
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectOrderList")
    @ResponseBody
    public CommonResult selectOrderList(String param, int page, int pageSize) {
        CommonResult result = orderInfoService.selectOrderList(param, page, pageSize);
        return result;
    }

    /**
     * 订单统计页面
     * @param modelAndView
     * @return
     */
    @RequestMapping("orderCountHtml")
    public ModelAndView orderCountHtml(ModelAndView modelAndView){
        modelAndView.setViewName("order/orderCount");
        return modelAndView;
    }

    /**
     * 查询产品统计
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/orderCount")
    @ResponseBody
    public CommonResult orderCount(String param, int page, int pageSize) {
        CommonResult result = orderInfoService.orderCount(param, page, pageSize);
        return result;
    }


    /**
     * 采购单位订单页面
     * @param modelAndView
     * @return
     */
    @RequestMapping("/purchaseOrgOrderHtml")
    public ModelAndView purchaseOrgHtml(ModelAndView modelAndView){
        modelAndView.setViewName("order/purchaseOrgOrderList");
        return modelAndView;
    }


    /**
     * 查询采购单位订单列表
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/searchPurchaseOrgOrder")
    @ResponseBody
    public CommonResult selectPurchaseOrgOrder(String param, int page, int pageSize) {
        CommonResult result = orderInfoService.selectPurchaseOrgOrder(param, page, pageSize);
        return result;
    }

    /**
     * 确认到账录入信息
     * @param orgName  公司名称
     * @param orderAmount   订单总金额
     * @param payAmount     汇款金额
     * @param payAccount    支付账户
     * @param payTime   收款时间
     * @return
     */
    @RequestMapping(value="/insertOrderAmount")
    @ResponseBody
    public Map insertOrderAmount(@RequestParam(value="id",required = true) String id,
                                 @RequestParam(value="orgName",required = true) String orgName,
                                 @RequestParam(value="orgCode",required = true) String orgCode,
                                 @RequestParam(value="orderAmount",required = true) String orderAmount,
                                 @RequestParam(value="payAmount",required = true) String payAmount,
                                 @RequestParam(value="payAccount",required = true) String payAccount,
                                 @RequestParam(value="payTime",required = true) String payTime){
        Map reqMap = new HashMap();
        reqMap.put("id",id);
        reqMap.put("org_code",orgCode);
        reqMap.put("org_name",orgName);
        reqMap.put("order_amount",orderAmount);
        reqMap.put("pay_amount",payAmount);
        reqMap.put("pay_account",payAccount);
        reqMap.put("pay_time",payTime);
        return this.orderInfoService.insertOrderAmount(reqMap);
    }

    /**
     * 生成采购明细单
     * @param orderInfo
     * @return
     */
    @RequestMapping(value="/createTableOrder")
    @ResponseBody
    public Map createTableOrder(String orderInfo){
        return this.orderInfoService.createTableOrderInfo(orderInfo);
    }

    /**
     * 下载(excel)文件
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        String fileName= request.getParameter("fileName");
//        String xlspath = ResourceUtils.getURL("classpath:").getPath()+"static/file/";
        try {
            File f = new File(xlspath  + fileName);
            //文件类型
            response.setContentType("application/x-msexcel");
            response.setCharacterEncoding("UTF-8");
            //文件类型+名称
            response.setHeader("Content-Disposition","attachment; filename="+fileName);
            //文件长度
            response.setHeader("Content-Length",String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            //删除文件
            //new File(rootpath  + fileName).delete();
        }

    }

}

