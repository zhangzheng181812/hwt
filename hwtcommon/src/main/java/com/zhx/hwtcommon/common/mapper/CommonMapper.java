package com.zhx.hwtcommon.common.mapper;

import com.zhx.hwtcommon.common.config.AppLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface CommonMapper {


    /**
     * 生成流水号
     * @param paramMap
     * @return
     */
    int buildOrderNo(Map paramMap);
    /**
     * 加入访问外部接口日志
     * @param map
     */
    void invokerLog(Map map);

    void insertAppLog(AppLog appLog);

    void insertAdminRequestLog(Map pararm);
}