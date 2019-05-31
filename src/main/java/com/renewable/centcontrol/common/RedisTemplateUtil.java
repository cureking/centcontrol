package com.renewable.centcontrol.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description：
 * @Author: jarry
 */
@Component("RedisTemplateUtil")
public class RedisTemplateUtil {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public RedisTemplate<Object, Object> getInstance() {
        return redisTemplate;
    }


    /**
     * 设置 String 类型 key-value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 获取 String 类型 key-value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (毫秒单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,毫秒单位
     */
    public void setForTimeMS(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,分钟单位
     */
    public void setForTimeMIN(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,分钟单位
     */
    public void setForTimeCustom(String key, String value, long time, TimeUnit type) {
        redisTemplate.opsForValue().set(key, value, time, type);
    }

    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并添加
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return (String) redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 删除 key-value
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @param type
     * @return
     */
    public boolean expire(String key, long time, TimeUnit type) {
        return redisTemplate.boundValueOps(key).expire(time, type);
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key
     * @return
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }


    /**
     * 获取指定key 的过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.boundValueOps(key).getExpire();
    }


    // 利用适配器模式，适应原有代码API，降低代码转换工作与出错可能

    /**
     * 设定key-value，并确定过期时间（单位：秒）
     *
     * @param key
     * @param value
     * @param time
     */
    public void setEx(String key, String value, long time) {
        this.setForTimeCustom(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 重新设定key所对应的数据的过期时间（单位：秒）
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        return redisTemplate.boundValueOps(key).expire(time, TimeUnit.SECONDS);
    }


    //set 操作  无序不重复集合   // 摘取了部分

    /**
     * 添加 set 元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long add(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 删除一个或多个集合中的指定值
     *
     * @param key
     * @param values
     * @return 成功删除数量
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机移除一个元素,并返回出来
     *
     * @param key
     * @return
     */
    public Object randomSetPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机获取一个元素
     *
     * @param key
     * @return
     */
    public Object randomSet(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取指定数量的元素,同一个元素可能会选中两次
     *
     * @param key
     * @param count
     * @return
     */
    public List<Object> randomSet(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
     *
     * @param key
     * @param count
     * @return
     */
    public Set<Object> randomSetDistinct(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 将 key 中的 value 转入到 destKey 中
     *
     * @param key
     * @param value
     * @param destKey
     * @return 返回成功与否
     */
    public boolean moveSet(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 无序集合的大小
     *
     * @param key
     * @return
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断 set 集合中 是否有 value
     *
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回集合中所有元素
     *
     * @param key
     * @return
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}
