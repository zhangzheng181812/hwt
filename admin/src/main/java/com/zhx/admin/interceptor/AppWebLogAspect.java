package com.zhx.admin.interceptor;

import com.zhx.admin.common.util.IpUtil;
import com.zhx.admin.common.util.JsonUtils;
import com.zhx.hwtcommon.common.service.CommonService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * create gaoxiang 2018/1/5 9:44
 */

@Aspect
@Configuration
public class AppWebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(AppWebLogAspect.class);

    @Autowired
    private CommonService commonService;

    @Pointcut("execution(* com.zhx.admin..*Controller.*(..))")
    public void controllerAspect() {
    }
    @AfterReturning(value = "controllerAspect()",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result)throws Throwable{
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        Map parameterMap = request.getParameterMap();
        Map reqMap = new HashMap();
        Set<Map.Entry<String,String[]>> entry = parameterMap.entrySet();
        Iterator<Map.Entry<String,String[]>> iterator = entry.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String[]> me = iterator.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            reqMap.put(key,value);
        }
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = JsonUtils.toJson(reqMap);
        String resultStr = JsonUtils.toJson(result);
        log.info("*********************Ci_Admin请求开始**********************");
        log.info("Admin请求url: {}", url);
        log.info("Admin请求方法: {}", method);
        log.info("Admin请求uri: {}", uri);
        log.info("Admin请求参数: {}", queryString);
        // result的值就是被拦截方法的返回值
        log.info("Admin返回值: " + resultStr);
        log.info("*********************Ci_Admin请求结束**********************");
        String ip = IpUtil.getIpAddr(request);
        Map map = new HashMap();
        map.put("request_path",url.length()>250?url.substring(0,250):url);
        map.put("request_key","BG");
        map.put("params",queryString);
        map.put("user_id","");
        map.put("user_name","");
        map.put("login_unit_no","");
        map.put("unit_no","");
        map.put("repStr",resultStr);
        map.put("request_ip",ip);
        String userId = String.valueOf(request.getSession().getAttribute(WebSecurityConfig.USER_ID));
        map.put("login_unit_id",userId);
        map.put("login_unit_name",String.valueOf(request.getSession().getAttribute(WebSecurityConfig.SESSION_KEY)));
        commonService.insertAdminRequestLog(map);
    }
}
