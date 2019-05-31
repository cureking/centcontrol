package com.renewable.centcontrol.common.constant;

/**
 * @Description：
 * @Author: jarry
 */
public class RedisConstant {


    public interface RedisCacheExtime {
        int REDIS_SESSION_EXTIME = 60 * 30;//30分钟
    }


    public interface REDIS_LOCK {
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
    }
}
