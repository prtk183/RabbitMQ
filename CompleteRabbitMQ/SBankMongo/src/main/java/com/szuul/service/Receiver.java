package com.szuul.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szuul.exception.HandleException;
import com.szuul.model.Audit;

import ch.qos.logback.core.net.SyslogOutputStream;
import ds.AuditJar.AuditJar;

@Component
public class Receiver {

	@Autowired
	AuditServiceImpl auditServiceImpl;

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String messag) {
		System.out.println("inside recived message*********" + messag);
		AuditJar message=null;
		try {
			
			ObjectMapper jsonMapper = new ObjectMapper();
			 message = (AuditJar) jsonMapper.readValue(messag, AuditJar.class);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Audit object = new Audit();
		
		object.setEventDate(message.getEventDate());
		object.setEventId(message.getEventId());
		object.setEventName(message.getEventName());
		object.setEventType( message.getEventType());
		object.setNewValue(message.getNewValue());
		object.setOldValue( message.getOldValue());
		object.setUserId(message.getUserId());
		System.out.println("Received <" + message.toString() + ">");
		System.out.println("passing audit object to create audit impl" + message + ">");
		
		try {
			auditServiceImpl.createAudit(object);
		} catch (HandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}