package com.zhx.admin.orderInfo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderInfoMapper
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/7
 * @Time 15:02
 * @Viersion 1.0
 **/
@Mapper
public interface OrderInfoMapper {

    int importHwtXshStock(List list);

    List selectOrderList(Map param);

    List orderCount(Map param);

    /**
     * 查询采购单位订单列表
     * @param param
     * @return
     */
    List selectPurchaseOrgOrder(Map param);

    /**
     * 确认到账信息
     * @param param
     * @return
     */
    int insertOrderAmount(Map param);

    /**
     * 确认收款修改订单状态值
     * @param param
     * @return
     */
    int updateOrderStatusInfo(Map param);

    /**
     * 查询采购明细表商品订单信息
     * @param orgCode
     * @return
     */
    List selectOrderTableList(String[] orgCode);

    /**
     * 查询采购明细单位列表信息
     * @param orgCode
     * @return
     */
    List selectOrderOrgList(String[] orgCode);

    /**
     * 生成采购明细单后修改机构订单状态值和采购单编号
     * @param orgCode 机构编码
     * @param orderNo   采购单编号
     * @return
     */
    boolean updateOrderStatus(@Param("orgCode")String[] orgCode,@Param("orderNo")String orderNo);

    /**
     * 修改机构汇款表
     * @param orgCode
     * @param orderNo
     */
    boolean updateOrderAmount(@Param("orgCode")String[] orgCode,@Param("orderNo")String orderNo);


}
