package com.ls.shiroboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.ls.shiroboot.mapper")
public class ShiroBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroBootApplication.class, args);
    }

}
