package org.andrey.test_chat.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public SimpleRabbitListenerContainerFactory listenerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory listenerFactory = new SimpleRabbitListenerContainerFactory();
		listenerFactory.setConnectionFactory(connectionFactory);
		listenerFactory.setMessageConverter(messageConverter());
		
		return listenerFactory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		
		return rabbitTemplate;
	}
}
