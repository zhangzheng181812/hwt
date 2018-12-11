package com.zhx.admin.common.config;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author SimonHu
 * @Description:
 * @Created on 2018/11/6 15:18
 */
@Configuration
public class MyBatisConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MyBatisConfiguration.class);

    @Bean
    public PageHelper pageHelper() {
        log.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
