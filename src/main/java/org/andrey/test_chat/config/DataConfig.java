package org.andrey.test_chat.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.andrey.test_chat.domain.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean("messageMap")
	public ConcurrentMap<String, Message> messageMap(){
		return new ConcurrentHashMap<>();
	}
	
	@Bean("updateQueue")
	public BlockingQueue<Message> updateQueue(){
		return new LinkedBlockingDeque<>();
	}
	
	@Bean("createQueue")
	public BlockingQueue<Message> createQueue(){
		return new LinkedBlockingDeque<>();
	}
}
