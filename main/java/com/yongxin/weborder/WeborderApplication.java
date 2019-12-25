package com.yongxin.weborder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAsync
@EnableRetry
public class WeborderApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(WeborderApplication.class, args);
    }

}
