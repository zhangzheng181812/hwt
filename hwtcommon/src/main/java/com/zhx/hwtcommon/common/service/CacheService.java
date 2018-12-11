package com.zhx.hwtcommon.common.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * appserver操作redis公共服务类
 * Created by admin on 2017/08/17.
 */
@Service
public class CacheService {

    @Resource(name = "stringRedisTemplate")
    RedisTemplate redisTemplate;

    /**
     * key - value 操作
     */
    @Resource(name = "stringRedisTemplate")
    ValueOperations<String,String> valueOperations;

    /**
     * hash 操作
     */
    @Resource(name = "stringRedisTemplate")
    HashOperations<String, String, String> hashOperations;

    /**
     * redis中添加
     * @param key
     * @param value
     */
    public void set(String key , String value){
        //        redisTemplate.opsForValue().set();
        valueOperations.set(key,value);
    }

    /**
     * redis中添加,如果key已经存在，返回false
     * @param key
     * @param value
     */
    public boolean setnx(String key , String value){
        return valueOperations.setIfAbsent(key,value);
    }

    /**
     * redis中添加
     * @param key
     * @param value
     * @param seconds 有效时间
     */
    public void set(String key , String value, long seconds){
        valueOperations.set(key,value,seconds, TimeUnit.SECONDS);
    }

    /**
     * 设置超时时间
     * @param key
     * @param seconds
     */
    public void expire(String key, long seconds){
        redisTemplate.expire(key,seconds, TimeUnit.SECONDS);
    }

    /**
     * redis中读取
     * @param key
     * @return
     */
    public String get(String key ){
        return valueOperations.get(key);
    }

    /**
     * 删除
     * @param object
     */
    public void delete(Object object){
        redisTemplate.delete(object);
    }


    /**
     * 插入hash值
     * @param key
     * @param map
     */
    public void hset(String key, Map<String,String> map){
        hashOperations.putAll(key,map);
    }

    /**
     * 插入hash值
     * @param key
     * @param filed
     * @param value
     */
    public void hset(String key, String filed, String value){
        hashOperations.put(key,filed,value);
    }

    /**
     * 获取hash值
     * @param key
     * @param filed
     */
    public String hget(String key, String filed){
       return  hashOperations.get(key,filed);
    }

    /**
     * 获取所有hash值
     * @param key
     * @return
     */
    public Map hgetall(String key) {
        return hashOperations.entries(key);
    }

    /**
     * 删除hash值
     * @param key
     * @param filed
     */
    public long hdelete(String key, String... filed){
        return  hashOperations.delete(key,filed);
    }


}
