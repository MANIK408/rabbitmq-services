package com.messaging.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.messaging.rabbitmq.services.InvokeRestServices;

@Configuration
public class RabbitMQConfig {

	final static String queueLocation = "location";
	final static String queueProperties = "properties";

	@Bean
	Queue queueLoc() {
		return new Queue(queueLocation, false);
	}

	@Bean
	Queue queueProp() {
		return new Queue(queueProperties, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding bindingLocation(Queue queueLoc, TopicExchange exchange) {
		return BindingBuilder.bind(queueLoc).to(exchange).with("x1");
	}

	@Bean
	Binding queueProperties(Queue queueProp, TopicExchange exchange) {
		return BindingBuilder.bind(queueProp).to(exchange).with("x1");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public InvokeRestServices receiver() {
		return new InvokeRestServices();
	}

}
