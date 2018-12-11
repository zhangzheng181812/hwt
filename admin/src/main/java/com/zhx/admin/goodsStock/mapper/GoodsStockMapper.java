package com.zhx.admin.goodsStock.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author SimonHu
 * @Description:
 * @Created on 2018/11/8 15:30
 */
@Mapper
public interface GoodsStockMapper {
    /**
     * 查询产品配置列表
     * @param param
     * @return
     */
    List selectGoodsStock(Map param);

    int insGoodsStock(Map pararm);

    int updateGoodsStock(Map pararm);
}
