package com.yongxin.weborder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class CommonPool
{

    @Bean(name = "leftTicketsThreadPool")
    public ThreadPoolTaskExecutor leftTicketsThreadPool(){
        return getCommonPool();
    }


    @Bean(name = "buyTicketsThreadPool")
    public ThreadPoolTaskExecutor buyTicketsThreadPool(){
        return getCommonPool();
    }

    @Bean(name = "proxyIpPool")
    public ThreadPoolTaskExecutor proxyIpPool(){
        return getCommonPool();
    }


    private ThreadPoolTaskExecutor getCommonPool()
    {
        ThreadPoolTaskExecutor pool=new ThreadPoolTaskExecutor();
        // 线程池维护线程的最少数量
        pool.setCorePoolSize(5);
        // 线程池维护线程的最大数量
        pool.setMaxPoolSize(20);
        pool.setKeepAliveSeconds(3000);
        pool.setQueueCapacity(1000);//队列容量
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setAwaitTerminationSeconds(60);
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //队列满，线程被拒绝执行策略
        pool.initialize();
        return pool;
    }
}
