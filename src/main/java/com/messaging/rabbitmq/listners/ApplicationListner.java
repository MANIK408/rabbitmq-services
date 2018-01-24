package com.messaging.rabbitmq.listners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.messaging.rabbitmq.entity.Addresses;
import com.messaging.rabbitmq.entity.TransactionEntity;
import com.messaging.rabbitmq.entity.TxnAddress;
import com.messaging.rabbitmq.property.entity.PropertyBulkEntity;
import com.messaging.rabbitmq.property.entity.TxnProperty;
import com.messaging.rabbitmq.services.InvokeRestServices;

@Component
public class ApplicationListner {
	@Autowired
	InvokeRestServices invokeRestServices;
	// @Autowired
	// private RabbitTemplate rabbitTemplate;
	@Autowired
	RestTemplate restTemplate;

	@RabbitListener(queues = "location")
	public void receiveLoc(TransactionEntity criteria) throws InterruptedException {
		Addresses addresses = invokeRestServices.receiveLocation(criteria.getSearchLocation());
		// Update the status by calling Rest Service
		// rabbitTemplate.convertAndSend("locationResult", addresses);
		TxnAddress txnAddress = new TxnAddress();
		txnAddress.setAddresses(addresses);
		txnAddress.setTxnNumber(criteria.getTxnNumber());
		restTemplate.postForObject("http://localhost:8090/update-address", txnAddress, TxnAddress.class);
	}

	@RabbitListener(queues = "properties")
	public void receiveProperty(TransactionEntity criteria) throws InterruptedException {
		PropertyBulkEntity object = invokeRestServices.receivePropertyDetails(criteria.getSearchMls());
		// Update the status by calling Rest Service
		// rabbitTemplate.convertAndSend("propertiesResult", object);
		TxnProperty txnProperty = new TxnProperty();
		txnProperty.setBulkBean(object);
		txnProperty.setTxnNumber(criteria.getTxnNumber());
		restTemplate.postForObject("http://localhost:8090/update-property", txnProperty, TxnProperty.class);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
