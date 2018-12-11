package com.zhx.server.interceptor;

import com.zhx.hwtcommon.common.config.AppLog;
import com.zhx.hwtcommon.common.service.CommonService;
import com.zhx.hwtcommon.common.util.IpUtil;
import com.zhx.hwtcommon.common.util.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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
    /**
     * 切入点：表示在哪个类的哪个方法进行切入。配置有切入点表达式
     */
    @Pointcut("execution(* com.zhx.*.*.controller.*.*(..))")
    public void controllerAspect() {
    }

    @AfterReturning(value = "controllerAspect()", returning = "result")
    public void afterReturnning(JoinPoint joinpoint, Object result) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Map ParameterMap =  request.getParameterMap();
        Map reqMap = new HashMap();
        Set<Map.Entry<String,String[]>> entry = ParameterMap.entrySet();
        Iterator<Map.Entry<String,String[]>> it = entry.iterator();
        while (it.hasNext()){
            Map.Entry<String,String[]>  me = it.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            reqMap.put(key,value);
        }

        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = JsonUtils.toJson(reqMap);
        String resultStr = JsonUtils.toJson(result);
        log.info("*********************请求开始**********************");
        log.info("请求url: {}", url);
        log.info("请求方法: {}", method);
        log.info("请求uri: {}", uri);
        log.info("请求参数: {}", queryString);
        // result的值就是被拦截方法的返回值
        log.info("返回值: " + resultStr);
        log.info("*********************请求结束**********************");
        //记录queryid
        AppLog appLog = new AppLog();
        appLog.setQueryid(String.valueOf(reqMap.get("queryid")));
        appLog.setInterfaceName(url);
        if(!url.endsWith("ideaBack")){//意见反馈不记录请求参数
            appLog.setRequestStr(queryString);
         }
        appLog.setRequestTime(new Date());
        appLog.setResponseStr(resultStr);
        appLog.setResponseIp(IpUtil.getLocalIP());
        appLog.setResponseTime(new Date());
        appLog.setOs("011");
        appLog.setPhoneNo(String.valueOf(reqMap.get("phoneNo")));
        appLog.setDeviceid(String.valueOf(reqMap.get("deviceId")));
        appLog.setMac(String.valueOf(reqMap.get("mac")));
        appLog.setPhoneModel(String.valueOf(reqMap.get("phoneModel")));
        appLog.setImei(String.valueOf(reqMap.get("imei")));
        appLog.setAndroidId(String.valueOf(reqMap.get("androidId")));
        String ip = IpUtil.getIpAddr(request,String.valueOf(reqMap.get("os")));
        appLog.setIp(ip);
        appLog.setRequestIp(ip);
        String userid = String.valueOf(reqMap.get("code"));
        appLog.setUserId(userid);
        appLog.setToken(String.valueOf(reqMap.get("token")));
        appLog.setOpenid(String.valueOf(reqMap.get("openid")));
        appLog.setReqSource(String.valueOf(reqMap.get("reqSource")));
        //记录到数据库
        Map resultMap = new HashMap();
        resultMap.put("result",result);
        commonService.insertAppLog(appLog);

    }




    /**
     * web异常捕获
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void webPointcut() {
    }

    @AfterThrowing(pointcut = "webPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) throws Exception {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Map ParameterMap = request.getParameterMap();
        Map reqMap = new HashMap();
        Set<Map.Entry<String, String[]>> entry = ParameterMap.entrySet();
        Iterator<Map.Entry<String, String[]>> it = entry.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> me = it.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            reqMap.put(key, value);
        }

        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = JsonUtils.toJson(reqMap);
        String result = e.toString();
        log.info("*********************请求开始**********************");
        log.info("请求url: {}", url);
        log.info("请求方法: {}", method);
        log.info("请求uri: {}", uri);
        log.info("请求参数: {}", queryString);

        //记录前6行错误信息
        int line = 6;
        StringBuffer errorInfo = new StringBuffer("");
        //打印异常信息
        StackTraceElement[] st = e.getStackTrace();
        for (StackTraceElement stackTraceElement : st) {
            if(line<1){
                break;
            }
            String exclass = stackTraceElement.getClassName();
            String exceptionMethod = stackTraceElement.getMethodName();
            int lineNumber = stackTraceElement.getLineNumber();
            errorInfo.append("EXCEPTION_CLASS：");
            errorInfo.append(exclass);
            errorInfo.append(",");
            errorInfo.append("EXCEPTION_METHOD：");
            errorInfo.append(exceptionMethod);
            errorInfo.append(",");
            errorInfo.append("EXCEPTION_ERROR_LINE：");
            errorInfo.append(lineNumber);
            errorInfo.append(";");
            line =line-1;
        }
        log.info("异常信息: {}", errorInfo);
        log.info("*********************请求结束**********************");
        AppLog appLog = new AppLog();
        appLog.setQueryid(String.valueOf(reqMap.get("queryid")));
        appLog.setInterfaceName(uri);
        appLog.setRequestStr(queryString);
        appLog.setRequestTime(new Date());
        appLog.setResponseStr(errorInfo.toString());
        appLog.setResponseIp(IpUtil.getLocalIP());
        appLog.setResponseTime(new Date());
        appLog.setOs("011");
        appLog.setPhoneNo(String.valueOf(reqMap.get("phoneNo")));
        appLog.setDeviceid(String.valueOf(reqMap.get("deviceId")));
        appLog.setMac(String.valueOf(reqMap.get("mac")));
        appLog.setPhoneModel(String.valueOf(reqMap.get("phoneModel")));
        appLog.setImei(String.valueOf(reqMap.get("imei")));
        appLog.setAndroidId(String.valueOf(reqMap.get("androidId")));
        String ip = IpUtil.getIpAddr(request,String.valueOf(reqMap.get("os")));
        appLog.setIp(ip);
        appLog.setRequestIp(ip);
        String userid = String.valueOf(reqMap.get("code"));
        appLog.setUserId(userid);
        appLog.setToken(String.valueOf(reqMap.get("token")));
        appLog.setOpenid(String.valueOf(reqMap.get("openid")));
        appLog.setReqSource(String.valueOf(reqMap.get("reqSource")));
        //记录到数据库
        Map resultMap = new HashMap();
        resultMap.put("result",errorInfo);
        commonService.insertAppLog(appLog);
    }
}
