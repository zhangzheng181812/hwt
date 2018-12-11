package com.zhx.admin.stockOrderCount.controller;

import com.zhx.admin.common.CommonResult;
import com.zhx.admin.stockOrderCount.service.StockOrderCountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @ClassName StockOrederCountController
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/22
 * @Time 11:15
 * @Viersion 1.0
 **/
@Controller
@RequestMapping(value = "/stockOrder")
public class StockOrderCountController {


    @Resource
    private StockOrderCountService stockOrderCountService;


    /**
     * 统计页面
     * @param modelAndView
     * @return
     */
    @RequestMapping("/stockOrderList")
    public ModelAndView orderCountHtml(ModelAndView modelAndView){
        modelAndView.setViewName("stockOrder/stockOrderCount");
        return modelAndView;
    }

    /**
     * 查询统计
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/stockOrderCount")
    @ResponseBody
    public CommonResult orderCount(String param, int page, int pageSize) {
        CommonResult result = stockOrderCountService.stockOrderCount(param, page, pageSize);
        return result;
    }

}
