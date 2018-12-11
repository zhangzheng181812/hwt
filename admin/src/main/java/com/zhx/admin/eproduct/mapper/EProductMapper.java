package com.zhx.admin.eproduct.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface EProductMapper {


    /**
     * 商品码进货单--查询list
     * @param map
     * @return
     */
    List selStockLogList(Map map);

    /**
     * 商品码进货单--insert
     * @param map
     * @return
     */
    int insStockLog(Map map);

    /**
     * 商品码进货单--update
     * @param map
     * @return
     */
    int updateStockLog(Map map);

    /**
     * 商品码进货单--根据id查询
     * @param id
     * @return
     */
    Map selStockLogById(String id);

    /**
     *
     * @param list
     * @return
     */
    int insHwtXshStock(List list);

    /**
     * 产品详情列表
     * @param param
     * @return
     */
     List selGoodsDetail(Map param);

    int insGoodsDetail(Map pararm);

    int updateGoodsDetail(Map pararm);

    List selGoodsInfoId(Map pararm);

    int updateGoodsDetailStatus(Map param);

}