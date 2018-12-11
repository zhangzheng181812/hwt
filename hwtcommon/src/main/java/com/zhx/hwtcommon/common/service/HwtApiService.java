package com.zhx.hwtcommon.common.service;

import com.zhx.hwtcommon.common.util.HttpsClient;
import com.zhx.hwtcommon.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * gaoxiang by 2018/11/6 16:51
 */

@Service
public class HwtApiService {

    private static Logger log = LoggerFactory.getLogger(HwtApiService.class);
    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");

    @Value("${hwt.api.url}")
    private String url;

    @Value("${hwt.api.client_id}")
    private String client_id;

    @Value("${hwt.api.client_secret}")
    private String client_secret;

    @Value("${hwt.api.tokenUrl}")
    private String tokenUrl;
    @Value("${hwt.api.userUrl}")
    private String userUrl;
    @Value("${hwt.api.orgUrl}")
    private String orgUrl;
    @Autowired
    private CacheService cacheService;

    @Autowired
    private CommonService commonService;
    /**
     * 获取 hwt api  凭证 token
     * @return
     * @throws IOException
     */
    public String doToken() throws IOException {
        Hashtable map = new Hashtable();

        map.put("client_id", client_id);
        map.put("client_secret", client_secret);
        map.put("grant_type", "client_credentials");
        map.put("scope", "");

//        String json = JsonUtils.toJsonNf(map);
        Date initDate = new Date();
        String result = HttpsClient.postSSL(url + tokenUrl, map, "");
        Date endDate = new Date();
        log.info(result);
        Map res = JsonUtils.toObject(result, HashMap.class);

        Map logMap = new HashMap();
        logMap.put("request_url", url + tokenUrl);
        logMap.put("request_time", initDate);
        logMap.put("response_time", endDate);
        logMap.put("request_parameter", "token");
        logMap.put("response_parameter", result);
        commonService.invokerLog(logMap, 8881);
        if("true".equals(String.valueOf(res.get("success")))){
            String token = String.valueOf(((Map)res.get("result")).get("access_token"));
            cacheService.set("HWTtoken",token,28800);
            return token;
        }else {

            return "";
        }
    }

    /**
     * 获取缓存的token
     * @return
     * @throws IOException
     */
    public String getRedisToken() throws IOException {
        String tokenR = cacheService.get("HWTtoken");
        if(StringUtils.isEmpty(tokenR)){
            return doToken();
        }else{
            return tokenR;
        }
    }


    /**
     * 获取 hwt 用户信息
     * @param code
     * @return  ee_no: 员工号
                org_code: 公司号
                user_id: 员工唯一标识id
                name: 员工姓名
                national_id: 身份证号
                language: 用户语言设置，可选项en/zh
                national_id_type: 身份证类型
                from_application_name: 跳转来源
     * @throws IOException
     */
    public Map getUser(String code) throws IOException {
        Hashtable map = new Hashtable();

        map.put("code", code);
//        String json = JsonUtils.toJsonNf(map);

        String token = getRedisToken();
        Date initDate = new Date();
        String result = HttpsClient.postSSL(url + userUrl, map, token);
        Date endDate = new Date();
        log.info(result);

        System.out.println(result);
        Map res = JsonUtils.toObject(result, HashMap.class);
        Map logMap = new HashMap();
        logMap.put("request_url", url + userUrl);
        logMap.put("request_time", initDate);
        logMap.put("response_time", endDate);
        logMap.put("request_parameter", "token");
        logMap.put("response_parameter", result);
        commonService.invokerLog(logMap, 8882);

        if("true".equals(String.valueOf(res.get("success")))){
            return (Map)res.get("result");
        }else {

            return null;
        }
    }

    /**
     * 获取 hwt 用户信息
     * @param code
     * @return  ee_no: 员工号
    org_code: 公司号
    user_id: 员工唯一标识id
    name: 员工姓名
    national_id: 身份证号
    language: 用户语言设置，可选项en/zh
    national_id_type: 身份证类型
    from_application_name: 跳转来源
     * @throws IOException
     */
    public Map getOrg(String code) throws IOException {
        Hashtable map = new Hashtable();

        map.put("org_code", code);
//        String json = JsonUtils.toJsonNf(map);

        String token = getRedisToken();
        Date initDate = new Date();
        String result = HttpsClient.postSSL(url + orgUrl, map, token);
        Date endDate = new Date();
        log.info(result);

        System.out.println(result);
        Map res = JsonUtils.toObject(result, HashMap.class);
        Map logMap = new HashMap();
        logMap.put("request_url", url + userUrl);
        logMap.put("request_time", initDate);
        logMap.put("response_time", endDate);
        logMap.put("request_parameter", "token");
        logMap.put("response_parameter", result);
        commonService.invokerLog(logMap, 8882);

        if("true".equals(String.valueOf(res.get("success")))){
            return (Map)res.get("result");
        }else {

            return null;
        }
    }
}
