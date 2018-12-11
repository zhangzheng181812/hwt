package com.zhx.admin.card.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.card.mapper.CardMapper;
import com.zhx.admin.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * gaoxiang by 2018/11/8 16:19
 */

@Service
public class CardService {

    @Autowired
    private CardMapper cardMapper;

    public CommonResult showCardList(String startTime, String endTime, int page, int pageSize) {
        Map map = new HashMap();
        PageHelper.startPage(page, pageSize);
        List pageList = cardMapper.showCardList(map);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }


    @Transactional
    public Map toCard(String purchaseNo) {
        Map result = new HashMap();
        //查询订单列表
        List<Map> orderList = cardMapper.selOrderList(purchaseNo);
        if (orderList == null || orderList.isEmpty()) {
            result.put("code", "909");
            result.put("msg", "批次是否有效");
            return result;
        }

        List<Map> cardList = null;
        String cateId = null;
        int num = 0;
        int totalC = 0;
        Map param = null;
        Set error = new HashSet();
        Set errCateid = new HashSet();
        //列表循环
        for (Map order : orderList) {
            // 当前订单商品cateid
            cateId = cardMapper.selGoodsCateid(String.valueOf(order.get("goods_id")));
            if (cateId == null) {
                errCateid.add(cateId);
                error.add(String.valueOf(order.get("goods_id")));
                continue;
            }
            if (errCateid.contains(cateId)) {
                continue;
            }
            //查询 卡列表
            cardList = cardMapper.selCateList(cateId);
            // 当前订单的卡数量
            num = Integer.valueOf(String.valueOf(order.get("num")));
            // 无卡
            if (cardList == null || cardList.isEmpty()) {
                errCateid.add(cateId);
                error.add(String.valueOf(order.get("goods_id")));
                continue;
                // 卡不足
            } else if (num > cardList.size()) {
                error.add(String.valueOf(order.get("goods_id")));
                continue;
            }
            //按数量发货
            for (int i = 0; i < num; i++) {
                //          #{order_no},#{goods_id},#{card_no},#{pwd},#{status},#{create_time},#{cate_id},#{ee_no},#{org_no}
                param = new Hashtable<>();
                param.put("order_no", order.get("order_no"));
                param.put("goods_id", order.get("goods_id"));

                param.put("card_no", cardList.get(i).get("card_no"));
                param.put("pwd", cardList.get(i).get("e_code_no"));
                param.put("expire_time", cardList.get(i).get("expire_time"));

                param.put("status", "1");
                param.put("cateid", cateId);
                param.put("ee_no", order.get("ee_no"));
                param.put("org_code", order.get("org_code"));
                int ins = cardMapper.insCardByorder(param);
                int updc = cardMapper.updCardStatus(String.valueOf(cardList.get(i).get("id")));

                if (ins + updc != 2) {
                    new RuntimeException("卡错误1");
                    return null;
                }
            }
            totalC++;
            int updo = cardMapper.updOrderStatus(String.valueOf(order.get("order_no")));
            if (updo != 1) {
                new RuntimeException("卡错误2");
                return null;
            }
        }

        if (totalC != orderList.size()) {
            result.put("errGoodsId", error);
            result.put("code", "999");
            result.put("msg", orderList.size() + "笔订单，已发" + totalC + "单，其余订单商品卡数量不足，商品编号:");
            return result;
        }

        result.put("code", "000");
        result.put("msg", orderList.size() + "笔订单，已发" + totalC + "单");
        return result;
    }


}
