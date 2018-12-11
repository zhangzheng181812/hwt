package com.zhx.hwtcommon.common.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JsonUtils {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final ObjectMapper JSON_NOFORMAT = new ObjectMapper();

    static {
        //Include.NON_NULL 属性为NULL 不序列化
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //是否缩放排列输出
        JSON.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
        //不解析未知的字符串
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //Include.NON_NULL 属性为NULL 不序列化
        JSON_NOFORMAT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //是否缩放排列输出
        JSON_NOFORMAT.configure(SerializationFeature.INDENT_OUTPUT, Boolean.FALSE);

        JSON_NOFORMAT.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public static <T> String toJsonNf(T t) {
        try {
            return JSON_NOFORMAT.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toJson(T t) {
        try {
            return JSON.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String str, Class<T> clazz) {
        try {
            return (T) JSON.readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T cast2(Class<T> clazz, Map map){
        return (T)map;
    }



    public static void main(String args[]){
        Map map = new HashMap();
        map.put("aaa","我");
        map.put("bbb","他");
        Map map1 = new HashMap();
        map1.putAll(map);
        map.put("ccc",map1);
        String str = JsonUtils.toJson(map);
        String str1 = JsonUtils.toJsonNf(map);
        System.out.println(str);
        System.out.println("************************");
        System.out.println(str1);
    }














}
