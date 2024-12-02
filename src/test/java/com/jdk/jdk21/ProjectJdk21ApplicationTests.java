package com.jdk.jdk21;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
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
    @Async
    @Lazy
    /**
     * 测试异步执行和虚拟线程的使用.
     * 该方法演示了如何在Spring测试环境中使用异步方法和虚拟线程，
     * 并执行一些基本的操作，如添加元素到LinkedList和使用Gson进行序列化。
     */
    void contextLoads() {

        // 启动一个新的虚拟线程并执行任务
        Thread.ofVirtual().start(() -> System.out.println("NEW"));

        // 初始化临时字符串变量
        String temp = "temp";
        // 初始化名称变量
        String name = "Lokesh";
        // 创建Gson实例用于JSON序列化和反序列化
        Gson gson = new Gson();

        // 创建一个LinkedList来存储字符串
        LinkedList<String> strings = new LinkedList<>();
        // 向列表中添加元素
        strings.add("2");
        strings.add("3");
        // 在列表开头添加元素
        strings.addFirst("1");
        // 在列表末尾添加元素
        strings.addLast("4");
        // 添加名称变量到列表中
        strings.add(name);
        // 将临时变量序列化后添加到列表中
        strings.add(gson.toJson(temp));

        // 创建并启动一个虚拟线程来执行Runnable任务
        Runnable fn = () -> {
            System.out.println("fn");
        };
        Thread.ofVirtual().start(fn);

        // 启动虚拟线程执行任务
        Thread.startVirtualThread(() -> {
            System.out.println("startVirtualThread");
            // 序列化并打印列表的当前状态后添加新元素
            System.out.println(gson.toJson(strings.add("5")));
        });

        // 启动另一个虚拟线程执行任务
        Thread.startVirtualThread(() -> {
            System.out.println("startVirtualThread2");
            // 序列化并打印列表的当前状态后添加新元素
            System.out.println(gson.toJson(strings.add("6")));
        });

        // 使用ExecutorService提交任务以在虚拟线程中执行
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> {
                System.out.println("executorService");
                // 序列化并打印列表的当前状态
                System.out.println(gson.toJson(strings));
            });
            // 关闭ExecutorService以释放资源
            executorService.shutdown();
        }

        // 使用Optional检查临时变量是否非空，并相应地打印
        Optional.of(temp).ifPresentOrElse(System.out::println, () -> System.out.println("null"));

        // 使用流处理列表中的每个元素，并打印
        strings.stream().flatMap(s -> Optional.ofNullable(s).stream()).forEach(System.out::println);
    }

    @Test
    void Test() {

    }

    @Test
    void Test2() {

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
