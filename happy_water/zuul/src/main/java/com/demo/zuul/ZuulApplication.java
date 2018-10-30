package com.demo.zuul;

import com.demo.zuul.accessfilter.BackstageFilter;
import com.demo.zuul.accessfilter.ReceptionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZuulApplication {
    @Bean
    public BackstageFilter accessFilter(){
        return new BackstageFilter();
    }
    @Bean
    public ReceptionFilter receptionFilter(){
        return new ReceptionFilter();
    }
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
