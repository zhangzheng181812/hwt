package com.zhx.admin.goodsStock.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.common.CommonResult;
import com.zhx.admin.goodsStock.mapper.GoodsStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SimonHu
 * @Description:
 * @Created on 2018/11/8 15:32
 */
@Service("goodsStockService")
public class GoodsStockService {
    private Logger log = LoggerFactory.getLogger(String.valueOf(GoodsStockService.class));
    @Autowired
    private GoodsStockMapper goodsStockMapper;

    /**
     * 查询产品配置信息
     *
     * @param map
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult selectGoodsStock(Map map, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List pageList = goodsStockMapper.selectGoodsStock(map);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    public int insGoodsDetail(Map param) {
        return goodsStockMapper.insGoodsStock(param);
    }

    /**
     * 更新
     *
     * @param param
     * @return
     */
    public int updateGoodsDetail(Map param) {
        return goodsStockMapper.updateGoodsStock(param);
    }
}
