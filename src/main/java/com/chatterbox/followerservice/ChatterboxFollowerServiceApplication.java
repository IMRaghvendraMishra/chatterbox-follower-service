package com.chatterbox.followerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ChatterboxFollowerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatterboxFollowerServiceApplication.class, args);
    }
}