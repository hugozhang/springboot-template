package com.winning.hmap.container;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath*:config/spring.xml")
@MapperScan(basePackages = "com.winning.hmap.**.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
@SpringBootApplication(scanBasePackages = {"com.winning.hmap"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        System.out.println("Hello Hmap Application");
    }
}