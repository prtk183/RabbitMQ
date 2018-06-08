package com.szuul;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;



//mysql
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
public class SBankApplication {
 
public final static String MESSAGE_QUEUE = "my-message-queue";
  
  @Bean
  TopicExchange exchange() {
    return new TopicExchange("spring-boot-exchange");
  }

  RabbitTemplate rabbitTemplate;
  
 
  public SBankApplication(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }
   
	public static void main(String[] args) {
		SpringApplication.run(SBankApplication.class, args);
	}
	
	public SBankApplication(){};
	

}
