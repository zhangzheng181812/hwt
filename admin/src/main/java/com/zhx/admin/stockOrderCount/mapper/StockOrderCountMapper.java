package com.zhx.admin.stockOrderCount.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName StockOrederCountMapper
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/22
 * @Time 11:16
 * @Viersion 1.0
 **/
@Mapper
public interface StockOrderCountMapper {

    List stockOrderCount(Map param);
}
