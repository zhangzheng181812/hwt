package com.zhx.admin.card.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CardMapper {

    List showCardList(Map param);

    List selOrderList(String purchaseNo);

    String selGoodsCateid(String goodsId);

    List selCateList(String cateId);

    int insCardByorder(Map param);

    int updCardStatus(String id);

    int updOrderStatus(String order_no);
}
