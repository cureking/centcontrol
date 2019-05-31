package com.renewable.centcontrol.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description：
 * @Author: jarry
 */
@Component
@Slf4j
public class TaskDemo {

//    @Autowired
//    private RedisTemplateUtil redisTemplateUtil;
//
//    static final Long LOCK_TIMEOUT = 5000L;
//    static final String TASK_DEMO_LOCK = "TASK_DEMO_LOCK";
//
//    @Scheduled(cron="0 */1 * * * ?")
//    public void taskDemoTaskV3(){
//        log.info("Demo定时任务启动");
//
//        Long setnxResult = redisTemplateUtil.setnx(TASK_DEMO_LOCK,String.valueOf(System.currentTimeMillis()+LOCK_TIMEOUT));
//        if(setnxResult != null && setnxResult.intValue() == 1){
//            taskDemo(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        }else{
//            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
//            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){  // 现有时间已经超过了锁时间     // 解决V2只能运行一次的问题
//                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
//                //再次用当前时间戳getset。
//                //返回给定的key的旧值，->旧值判断，是否可以获取锁
//                //当key没有旧值时，即key不存在时，返回nil ->获取锁
//                //这里我们set了一个新的value值，获取旧的值。
//                if(getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr,getSetResult))){    // 其实我不大理解这里为什么要进行一个相等判断（至于null问题，我觉得是因为redis的一些机制）
//                    //真正获取到锁
//                    taskDemo(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//                }else{
//                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//                }
//            }else{
//                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//            }
//        }
//        log.info("Demo定时任务结束");
//    }
//
//    //    @Scheduled(cron="0 */1 * * * ?")
//    public void taskDemoTaskV4(){
//        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        boolean getLock = false;
//        try {
//            if(getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){
//                log.info("Redisson获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//                // 业务逻辑
//
//            }else{
//                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//            }
//        } catch (InterruptedException e) {
//            log.error("Redisson分布式锁获取异常",e);
//        } finally {
//            if(!getLock){
//                return;
//            }
//            lock.unlock();
//            log.info("Redisson分布式锁释放锁");
//        }
//    }


//    private void taskDemo(String lockName){
//        RedisShardedPoolUtil.expire(lockName,5);//有效期50秒，防止死锁
//        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//        iOrderService.taskDemo(hour);
//        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//        log.info("===============================");
//    }

}
