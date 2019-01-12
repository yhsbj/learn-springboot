package com.line;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* @EnableAutoConfiguration springboot的注解 会这个注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring上下文。
* 由于spring-boot-starter-web添加了Tomcat和Spring MVC，所以auto-configuration将假定你正在开发一个web应用，
* 并对Spring* 进行相应地设置
* @SpringBootApplication包含@EnableAutoConfiguration、@ComponentScan
* 其中@ComponentScan扫描注册范围，使@bean等其他注入大盘spring容器，默认扫描当前包及子包
*/
@SpringBootApplication
public class Starter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Starter.class, args);
    }
}