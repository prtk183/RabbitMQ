package com.rmq.sender.service;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rmq.sender.config.SenderConfig;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Sender sender;

    public Runner(Sender sender, RabbitTemplate rabbitTemplate) {
 
        this.sender = sender;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(SenderConfig.exchange, "key", "Hello from RabbitMQ sender!");
        sender.getLatch().await(1000, TimeUnit.MILLISECONDS);
    }
}
