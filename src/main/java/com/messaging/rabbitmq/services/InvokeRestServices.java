package com.messaging.rabbitmq.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.messaging.rabbitmq.entity.AddressLocated;
import com.messaging.rabbitmq.entity.AddressLocater;
import com.messaging.rabbitmq.entity.Addresses;
import com.messaging.rabbitmq.entity.PropertySearchCriteria;
import com.messaging.rabbitmq.property.entity.PropertyBulkEntity;

@Component
public class InvokeRestServices {
	@Autowired
	RestTemplate restTemplate;
	@Value("${google.location.uri}")
	private String googleLocUri;
	@Value("${simplyrets.uri}")
	private String simpleRetsUri;

	public Addresses receiveLocation(String message) {
		AddressLocater addressLocaterBean = new AddressLocater();
		addressLocaterBean.setQuery(message);
		List<AddressLocated> object = (List<AddressLocated>) restTemplate.postForObject(googleLocUri,
				addressLocaterBean, Object.class);
		Addresses addresses = new Addresses();
		addresses.setAddressLocateds(object);
		System.out.println("Received Locations----> <" + message + ">" + object);
		return addresses;
	}

	public PropertyBulkEntity receivePropertyDetails(int message) {
		PropertySearchCriteria propertySearchCriteria = new PropertySearchCriteria();
		propertySearchCriteria.setMlsId(message);
		PropertyBulkEntity object = (PropertyBulkEntity) restTemplate.postForObject(simpleRetsUri,
				propertySearchCriteria, PropertyBulkEntity.class);
		System.out.println("Received Property---> <" + message + ">" + object);
		return object;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
