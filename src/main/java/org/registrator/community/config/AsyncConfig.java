package org.registrator.community.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
@PropertySource(value = "classpath:asyncpool.properties", ignoreResourceNotFound=true)
public class AsyncConfig implements AsyncConfigurer {
    private static final int DEFAULT_SIZE = 5;
    private static final int DEFAULT_AWAIT_SECS = 300; // 300s = 5 minutes
    private static final String DEFAULT_PREFIX = "async";
    
    @Autowired
    private Environment env;
 
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        // load configuration from properties file if it exists, else load default
        threadPool.setCorePoolSize(env.getProperty("core.pool.size", Integer.class) != null
                ? env.getProperty("core.pool.size", Integer.class) : DEFAULT_SIZE);
        threadPool.setMaxPoolSize(env.getProperty("max.pool.size", Integer.class) != null
                ? env.getProperty("max.pool.size", Integer.class) : DEFAULT_SIZE);
        threadPool.setAwaitTerminationSeconds(env.getProperty("await.seconds", Integer.class) != null
                ? env.getProperty("await.seconds", Integer.class) : DEFAULT_AWAIT_SECS);
        threadPool.setThreadNamePrefix(
                env.getProperty("thread.prefix") != null ? env.getProperty("thread.prefix") : DEFAULT_PREFIX);
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
