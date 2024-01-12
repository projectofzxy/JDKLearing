package com.jdk.jdk21;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


@SpringBootTest
class ProjectJdk21ApplicationTests {
    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;

    @Test
    void contextLoads() {

        Thread.ofVirtual().start(() -> System.out.println("NEW"));
        String temp = "temp";
        String name = "Lokesh";

        ArrayList<String> strings = new ArrayList<>();
        strings.addFirst("1");
        Runnable fn=()->{
            System.out.println("fn");
        };
        Thread.ofVirtual().start(fn);
        Thread.startVirtualThread(()->{
            System.out.println("startVirtualThread");
        });
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        executorService.submit(()->{
            System.out.println("executorService");
        });
        executorService.shutdown();
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }






}
