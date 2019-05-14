package com.renewable.centcontrol.common;

//选择logback的logger。

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @Description：用于保存期限token
 * @Author: jarry
 * @Date: 12/20/2018 15:23
 */
public class TokenCache {

    //token 作为前缀的常量，由于与TokenCache太紧密，所以放在这里
    public static final String TOKEN_PREFIX = "token_";

    //日志    logback
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    //声明一个静态的内存块-guavacache
    private static LoadingCache<String, String> localCache =
            CacheBuilder.
                    //CacheBuilder是一个调用链的形式
                            newBuilder().
                    //初始化，其中表示cache容量
                            initialCapacity(1000).
                    //缓存的最大容量，当超过时，将通过LRU算法，来清除缓存
                            maximumSize(10000).
                    //设置缓存的有效期  最后通过 12小时
                            expireAfterAccess(12, TimeUnit.HOURS).
                    //选择build(CacheLoader<? super K1,V1> loader)。抽象类，需要写一个匿名实现（当然也可以在别处写实现，这里引用）
                            build(new CacheLoader<String, String>() {
                        //默认的数据加载实现，当调用get取值时，如果key没有对应的值，就调用该方法进行加载。（即key值没有命中）
                        @Override
                        public String load(String s) throws Exception {
//                            return null;  //默认的写法，在调用时需要注意xx==key.value和xx.equals（key）。前者会引发nullpointError。故改写
                            return "null";
                        }
                    });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        //由于这里可能出现异常，所以需要使用try。（
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error("LocalCache get error", e);
        }
        return null;
    }
}
