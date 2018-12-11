package com.zhx.hwtcommon.common.service;

import com.zhx.hwtcommon.common.config.AppLog;
import com.zhx.hwtcommon.common.mapper.CommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 公共服务service
 */
@Service
public class CommonService {
    private final Logger log = LoggerFactory.getLogger(CommonService.class);
    @Autowired
    private CommonMapper commonMapper;


    /**
     * 加入访问外部接口日志
     */
    @Async("logExecutor")
    public void invokerLog(Map logMap, int logtype) {
        try {
            logMap.put("server_ip", String.valueOf(InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        logMap.put("log_type", logtype);
        commonMapper.invokerLog(logMap);
    }

    /**
     * 记录Ci_Admin项目Request日志
     */
    @Async("logExecutor")
    public void insertAdminRequestLog(Map map) {
        commonMapper.insertAdminRequestLog(map);
    }

    /**
     * 记录AppLog日志
     */
    @Async("logExecutor")
    public void insertAppLog(AppLog appLog) {
        commonMapper.insertAppLog(appLog);
    }

    /**
     * 生成订单号(20位 yyyy+后3位流水+mm+6位随机+前3位流水+dd)
     *
     * @return
     */
    public String buildOrderNo() {
        Map paramMap = new HashMap();
        paramMap.put("genOrderNo", "");
        commonMapper.buildOrderNo(paramMap);
        //流水号
        String ordernoSerial = paramMap.get("genOrderNo").toString();
        //只取后6位
        ordernoSerial=ordernoSerial.substring(ordernoSerial.length()-6,ordernoSerial.length());
        String ordernoRandom = String.valueOf((int) (Math.random() * (99999 - 10000 + 1)) + 100000);// 获取6位随机数);
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String dates[] = sd.format(date).split("-");
        StringBuffer sb = new StringBuffer();
        String start = ordernoSerial.substring(0, 3);    //前三位流水
        String end = ordernoSerial.substring(3, 6);    //后三位流水
        //组装订单号
        sb.append(dates[0]);
        sb.append(end);
        sb.append(dates[1]);
        sb.append(ordernoRandom);
        sb.append(start);
        sb.append(dates[2]);
        return sb.toString();
    }
}
