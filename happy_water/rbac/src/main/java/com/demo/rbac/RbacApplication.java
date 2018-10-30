package com.demo.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.PlatformTransactionManager;

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.demo.rbac.mapper")//将项目中对应的mapper类的路径加进来就可以了
public class RbacApplication {
    //事务
    @Autowired
    PlatformTransactionManager transactionManager=null;
    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }
}
