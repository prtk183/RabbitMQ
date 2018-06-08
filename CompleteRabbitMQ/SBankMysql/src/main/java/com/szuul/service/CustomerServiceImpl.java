 package com.szuul.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szuul.SBankApplication;
import com.szuul.controller.CustomerController;
import com.szuul.dao.BankRepository;
import com.szuul.dao.CustomerRepository;
import com.szuul.exception.HandleException;
import ds.AuditJar.*;
import com.szuul.model.Bank;
import com.szuul.model.Customer;
import com.szuul.model.EnumClass;
import com.szuul.wrappers.WrapperClass;
import com.szuul.wrappers.WrapperUpdateCustomer;



@Service
public class CustomerServiceImpl implements CustomerService {

  public static int auditCount=1;
  /**------------customerRepository object-----------------.*/
	@Autowired
	private CustomerRepository customerRepository;
	
	/**----------Log---------------.*/
  Logger Log = Logger.getLogger(CustomerServiceImpl.class.getName());
  
  /**------------environment-----------------.*/
  @Autowired
  private Environment environment;
  
	/**----------------bankServiceImpl--------------------------.*/ 
  @Autowired
	 private BankServiceImpl bankServiceImpl;
  
  RabbitTemplate rabbitTemplate;
  
  
  public CustomerServiceImpl(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * @param rabbitTemplate
   * @return 
   */
 
  
	/**/
	/* crating customer with id, pi, name and bankid
	 * @see com.szuul.service.CustomerService#createCustomer(com.szuul.model.Customer)
	 */
	@SuppressWarnings("unlikely-arg-type")
  @Override
	public Customer createCustomer(WrapperClass wrapperClass) throws HandleException {
	  
	 
	  Log.info("in customer service calling createCustomer");
	  
	  if(wrapperClass!=null && wrapperClass.getBankId()!=null && wrapperClass.customer!=null )
	  {
	    
	  if(( bankServiceImpl.getBank(wrapperClass.getBankId()).getBankId()
	      .equals(wrapperClass.getBankId()) )) //cheking valid bank in argument 
	    {
	        if( wrapperClass.customer.getCustomerName().isEmpty()
	            && wrapperClass.customer.getPin()==null )  //checking valid customer data
	          {
	              throw new HandleException(environment.getProperty("201"));
	          } else {
	            Customer addNewCustomer = new Customer(wrapperClass.customer.getCustomerName(),  wrapperClass.customer.getPin(), bankServiceImpl.getBank(wrapperClass.getBankId()));
	                      addNewCustomer =  customerRepository.save(addNewCustomer);
	                      return addNewCustomer;
	         }
	    } else {
	      throw new HandleException(environment.getProperty("202")); 
	    }
	  } else {
      throw new HandleException(environment.getProperty("7777")); 

	  }
	}



	/*
   * showing customers
   * 
	 * @see com.szuul.service.CustomerService#getCustomerdetails()
	 */
	@Override
	public List<Customer> getCustomerdetails() throws HandleException {
		
	  Log.info("in customer service calling getCustomerdetails");
		List<Customer> custlist;
		
		custlist=customerRepository.findAll();
		
		if(custlist.size()==0)
		{
		  throw new HandleException(environment.getProperty("200"));
		}
		else
		{
		  return custlist;
		}
	}


	/**/
  /* get customer by id
   * @see com.szuul.service.CustomerService#getCustomer(java.lang.Long)
   */
  @Override
  public Customer getCustomer(final Long customerId) throws HandleException {
    Log.info("in customer service calling getCustomer");
    Optional op;
    Customer customer=null;
    
    op= customerRepository.findBycustomerId(customerId);
    if(op.isPresent())
    {
        customer = customerRepository.findBycustomerId(customerId).get();
        return customer;
    }
    else
    {
      throw new HandleException(environment.getProperty("200"));
    }
    
   
  }



  @Override
  public Customer updateCustomer(final WrapperUpdateCustomer object) throws HandleException, CloneNotSupportedException {
    Log.info("in customer service calling updateCustomer");
   
    if( object.getCustomerBankId()!=null & object.getCustomerName()!=null)
    {
      
      final Customer customer = customerRepository.findBycustomerId(object.getId()).get();
      
      Log.info("in customer service bindig audit");
      customer.setBank(bankServiceImpl.getBank(object.getCustomerBankId()));
      
      final AuditJar audit = new AuditJar();    //auditjar object binding
      Calendar cal= Calendar.getInstance();
      Date time=cal.getTime();
      
      audit.setEventDate(time);
      audit.setEventId(auditCount);
      audit.setEventName(EnumClass.eventName.CUSTOMER.toString());
      audit.setEventType(EnumClass.eventType.UPDATED.toString());
     
      Customer clonedCustomer = (Customer) customer.clone();
      
      audit.setOldValue(clonedCustomer);
      
      customer.setCustomerName(object.getCustomerName());
      Customer newValue = customerRepository.save(customer);
      
      audit.setNewValue(newValue);
      audit.setAuditId(Integer.toString(auditCount));
      audit.setUserId(Integer.toString(auditCount));
      System.out.println("sending "+ audit +" mesg into RBMQ");
      

      String value="";
      ObjectMapper mapper = new ObjectMapper();
      try {
        value= mapper.writeValueAsString( audit);
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      rabbitTemplate.convertAndSend(SBankApplication.MESSAGE_QUEUE, value); //to put in RBMQ
      System.out.println("sent "+value+"mesg into RBMQ");
   //   ctx.close();
      return newValue;
    }
    else
    {
      throw new HandleException(environment.getProperty("7777"));
    }
  }
		

}
