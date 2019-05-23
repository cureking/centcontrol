package com.renewable.centcontrol.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description：
 * @Author: jarry
 */
@Component("RedisTemplateUtil")
public class RedisTemplateUtil {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public RedisTemplate<Object,Object> getInstance(){
        return redisTemplate;
    }


    /**
     * 设置 String 类型 key-value
     * @param key
     * @param value
     */
    public void set(String key,String value){
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 获取 String 类型 key-value
     * @param key
     * @return
     */
    public String get(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (毫秒单位)
     * @param key
     * @param value
     * @param time 过期时间,毫秒单位
     */
    public void setForTimeMS(String key,String value,long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     * @param key
     * @param value
     * @param time 过期时间,分钟单位
     */
    public void setForTimeMIN(String key,String value,long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     * @param key
     * @param value
     * @param time 过期时间,分钟单位
     */
    public void setForTimeCustom(String key,String value,long time,TimeUnit type){
        redisTemplate.opsForValue().set(key, value, time, type);
    }

    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并添加
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key,String value){
        return (String) redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 删除 key-value
     * @param key
     * @return
     */
    public boolean delete(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 给一个指定的 key 值附加过期时间
     * @param key
     * @param time
     * @param type
     * @return
     */
    public boolean expire(String key,long time,TimeUnit type){
        return redisTemplate.boundValueOps(key).expire(time, type);
    }

    /**
     * 移除指定key 的过期时间
     * @param key
     * @return
     */
    public boolean persist(String key){
        return redisTemplate.boundValueOps(key).persist();
    }


    /**
     * 获取指定key 的过期时间
     * @param key
     * @return
     */
    public Long getExpire(String key){
        return redisTemplate.boundValueOps(key).getExpire();
    }




    // 利用适配器模式，适应原有代码API，降低代码转换工作与出错可能
    /**
     *  设定key-value，并确定过期时间（单位：秒）
     * @param key
     * @param value
     * @param time
     */
    public void setEx(String key,String value,long time){
        this.setForTimeCustom(key,value,time,TimeUnit.SECONDS);
    }

    /**
     * 重新设定key所对应的数据的过期时间（单位：秒）
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key,long time){
        return redisTemplate.boundValueOps(key).expire(time, TimeUnit.SECONDS);
    }
}
