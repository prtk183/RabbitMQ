package com.rmq.sender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rmq.sender.model.Employee;
import com.rmq.sender.service.Sender;

@RestController
@RequestMapping(value = "/javainuse-rabbitmq/")
public class RabbitMQWebController {

	@Autowired
	Sender rabbitMQSender;

	@GetMapping(value = "/producer")
	public String producer(@RequestParam("empName") String empName,@RequestParam("empId") String empId) {
	
	Employee emp=new Employee();
	emp.setEmpId(empId);
	emp.setEmpName(empName);
		rabbitMQSender.sendMessage("");

		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}

}

