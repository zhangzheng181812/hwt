package com.zhx.server.order.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OrderMapper {

    Map selOrderByEE(Map param);

    List selOrderCardList(Map param);

    List selGoodsInfo(String goodsId);


}
