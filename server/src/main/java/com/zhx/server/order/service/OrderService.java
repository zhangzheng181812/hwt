package com.zhx.server.order.service;

import com.zhx.hwtcommon.common.service.HwtApiService;
import com.zhx.hwtcommon.common.util.TripleDESUtil;
import com.zhx.server.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gaoxiang by 2018/11/7 11:08
 */

@Service
public class OrderService {

    @Value("${site.encryptkey}")
    private String encryptkey;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private HwtApiService hwtApiService;

    /**
     * 查询订单列表
     *
     * @param code
     * @param orderNo
     * @return
     */
    public Map orderDetail(String code, String orderNo) {
        Map result = new HashMap();
        Map<String, String> userInfo = null;
        //查询用户
        try {
            userInfo = hwtApiService.getUser(code);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg","用户信息有误");
            return result;
        }
        if(userInfo == null){
            result.put("msg","用户不存在");
            return result;
        }
        String eeNo = userInfo.get("ee_no");
        String orgCode = userInfo.get("org_code");
        String userId = String.valueOf(userInfo.get("user_id"));
        Map param = new HashMap();
        param.put("eeNo", eeNo);
        param.put("orgCode", orgCode);
        param.put("userId", userId);
        param.put("orderNo", orderNo);
        //查询订单
        Map orderInfo = orderMapper.selOrderByEE(param);
        if (orderInfo == null) {
            result.put("msg","订单不存在或订单未发货");
            return result;
        }
        List<Map> cardList = orderMapper.selOrderCardList(param);
        for (Map card : cardList) {
            card.put("password", TripleDESUtil.decryptString(String.valueOf(card.get("pwd")), encryptkey));
            card.remove("pwd");
        }
        //查询商品
        List<Map> goodsList = orderMapper.selGoodsInfo(String.valueOf(orderInfo.get("goods_id")));

        if(goodsList != null  && goodsList.size()>0) {
            result.put("goodsName", goodsList.get(0).get("goodsName"));
            result.put("description", goodsList.get(0).get("description"));
            result.put("picSrc", goodsList.get(0).get("picSrc"));
        }
        result.put("goodsId", "goodsInfo?goodsId="+String.valueOf(orderInfo.get("goods_id")));
        result.put("cardList", cardList);
        result.put("code","000");
        return result;
    }

    /**
     * @param goodsId
     * @return
     */
    public Map goodsInfo(String goodsId) {
        Map result = new HashMap();
        List<Map> goodsList = orderMapper.selGoodsInfo(goodsId);
        List piclists = null;

        result.put("goodsName", goodsList.get(0).get("goodsName"));
        result.put("description", goodsList.get(0).get("description"));
        result.put("picSrc", goodsList.get(0).get("picSrc"));

        for (Map item : goodsList) {
            String piclist = String.valueOf(item.get("pic_list"));
            piclists = new ArrayList();
            if (!"".equals(piclist) && !"null".equals(piclist)) {
                String[] pics = piclist.split(",");
                for (String i : pics) {
                    if (!"".equals(i)) {
                        piclists.add(i);
                    }
                }
            }
            item.remove("goodsName");
            item.remove("description");
            item.remove("picSrc");
            item.put("picList", piclists);
            item.remove("pic_list");
        }
        result.put("detail", goodsList);

        return result;
    }

}
