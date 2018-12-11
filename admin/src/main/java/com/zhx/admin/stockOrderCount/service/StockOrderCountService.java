package com.zhx.admin.stockOrderCount.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhx.admin.common.CommonResult;
import com.zhx.admin.common.util.JsonUtils;
import com.zhx.admin.stockOrderCount.mapper.StockOrderCountMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StockOrederCountService
 * @Description 描述信息...
 * @Author HeCheng
 * @Date 2018/11/22
 * @Time 11:16
 * @Viersion 1.0
 **/
@Service
public class StockOrderCountService {

    @Resource
    private StockOrderCountMapper stockOrderCountMapper;

    /**
     * 查询统计
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    public CommonResult stockOrderCount(String param, int page, int pageSize){
        Map paramMap = new HashMap();
        if(StringUtils.isNotEmpty(param)){
            paramMap= JsonUtils.toObject(param,HashMap.class);
        }
        paramMap.put("param",param);
        PageHelper.startPage(page,pageSize);
        List pageList = stockOrderCountMapper.stockOrderCount(paramMap);
        PageInfo pageInfo = new PageInfo(pageList);
        Map result = new HashMap();
        result.put("rows", pageInfo.getList());
        result.put("totalCount", pageInfo.getTotal());
        return CommonResult.success(result);
    }

}
