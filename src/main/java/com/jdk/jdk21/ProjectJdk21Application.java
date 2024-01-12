package com.jdk.jdk21;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.jdk.jdk21")
@EnableAsync
public class ProjectJdk21Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectJdk21Application.class, args);
    }

}
