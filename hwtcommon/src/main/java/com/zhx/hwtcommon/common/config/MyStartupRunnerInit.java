package com.zhx.hwtcommon.common.config;

import com.zhx.hwtcommon.common.service.HwtApiService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * gaoxiang by 2018/11/6 15:32
 */

@Component
public class MyStartupRunnerInit implements InitializingBean {


    @Autowired
    private HwtApiService hwtApiService;

    /**
     * 容器启动时 获取token
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        String token = hwtApiService.getRedisToken();
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------" + token);
        System.out.println("--------------------------------------");
        System.out.println("--------------------------------------");
    }
}
