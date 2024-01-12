package com.jdk.jdk21;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedList;
import java.util.Optional;
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
        Gson gson = new Gson();

        LinkedList<String> strings = new LinkedList<>();
        strings.add("2");
        strings.add("3");
        strings.addFirst("1");
        strings.addLast("4");
        strings.add(name);
        strings.add(gson.toJson(temp));
        Runnable fn=()->{
            System.out.println("fn");
        };
        Thread.ofVirtual().start(fn);
        Thread.startVirtualThread(()->{
            System.out.println("startVirtualThread");
            System.out.println(gson.toJson(strings.add("5")));
        });
        Thread.startVirtualThread(()->{
            System.out.println("startVirtualThread2");
            System.out.println(gson.toJson(strings.add("6")));
        });
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> {
                System.out.println("executorService");
                System.out.println(gson.toJson(strings));
            });
            executorService.shutdown();
        }
        Optional.of(temp).ifPresentOrElse(System.out::println,()-> System.out.println("null"));
        strings.stream().flatMap(s-> Optional.ofNullable(s).stream()).forEach(System.out::println);
    }

    @Test
    void Test() {

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
