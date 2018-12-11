package com.zhx.admin.card.controller;

import com.zhx.admin.card.service.CardService;
import com.zhx.admin.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * gaoxiang by 2018/11/9 14:17
 */

@Controller
@RequestMapping("cardMap")
public class CradController {

    @Autowired
    private CardService cardService;

    @RequestMapping("showHtml")
    public ModelAndView showHtml(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("card/cardHtml");
        return mav;
    }

    @RequestMapping("showList")
    @ResponseBody
    public CommonResult showList(String startTime, String endTime, int page, int pageSize){
        CommonResult result = cardService.showCardList(startTime, endTime, page, pageSize);
        return result;

    }



    /**
     * 根据订单批次发送卡
     * @param purchaseNo 订单c采购批次号
     * @return
     */
    @RequestMapping("cardDelivery")
    @ResponseBody
    public Map cardDelivery(String purchaseNo){
        Map result = cardService.toCard(purchaseNo);

        return result;
    }

}
