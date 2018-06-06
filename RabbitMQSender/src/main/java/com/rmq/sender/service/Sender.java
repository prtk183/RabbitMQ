package com.rmq.sender.service;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rmq.sender.model.Employee;

@Service
@Component
public class Sender {

	    private CountDownLatch latch = new CountDownLatch(1);
	    
	    public void sendMessage(String message) {
	       // System.out.println("Received <" + message + ">");
	    	
	        latch.countDown();
	    }

	    public CountDownLatch getLatch() {
	        return latch;
	    }

	}
