package com.zhx.admin.goodsStock.controller;

import com.zhx.admin.common.CommonResult;
import com.zhx.admin.goodsStock.service.GoodsStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存产品配置
 *
 * @author SimonHu
 * @Description:
 * @Created on 2018/11/8 15:28
 */
@Controller
@RequestMapping("/goodsStock")
public class GoodsStockController {
    private static final Logger log = LoggerFactory.getLogger(GoodsStockController.class);

    @Autowired
    private GoodsStockService goodsStockService;
    /**
     * 显示库存配置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/showConfig")
    public ModelAndView showECodeStocking(ModelAndView model) {
        model.setViewName("goodsStock/goodsStock");
        return model;
    }

    /**
     * 查询产品配置信息
     * @param goods_id
     * @param status
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/configList")
    @ResponseBody
    public CommonResult configList(@RequestParam(value = "goods_id", required = false) String goods_id,
                                   @RequestParam(value = "status", required = false) String status, int page, int pageSize) {
        Map map = new HashMap();
        map.put("goods_id", goods_id);
        map.put("status", status);
        CommonResult result = goodsStockService.selectGoodsStock(map,page,pageSize);
        return result;
    }

    /**
     * 编辑产品库存配置
     * @param id
     * @param goods_id
     * @param cateid
     * @param status
     * @return
     */
    @RequestMapping("/saveGoodsStock")
    @ResponseBody
    public CommonResult saveGoodsStock(String id, String goods_id, String cateid,  String status){
        Map param = new HashMap();
        param.put("goods_id", goods_id);
        param.put("cateid", cateid);
        param.put("status", status);
        int count = 0;
        if (StringUtils.isEmpty(id)) {
            count = goodsStockService.insGoodsDetail(param);
        } else {
            param.put("id", id);
            count = goodsStockService.updateGoodsDetail(param);
        }
        if(count>0){
            return CommonResult.success();
        }else {
            return CommonResult.result("101","编辑失败");
        }
    }
}
