package com.xinghai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication()
@MapperScan("com.xinghai.mapper")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StarseaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarseaApplication.class, args);
    }

}
