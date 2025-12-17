package com.baiye.baiyeaiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class BaiyeAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiyeAiAgentApplication.class, args);
    }

}


