package org.andrey.test_chat.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.andrey.test_chat.domain.IDRegDTO;
import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.domain.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageDataService {

	private Logger log = LoggerFactory.getLogger(MessageDataService.class);
	
	private RestTemplate restTemplate;
	private UpdatesService updatesService;
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	public MessageDataService(RestTemplate restTemplate,
								UpdatesService updatesService,
								RabbitTemplate rabbitTemplate) {
		this.restTemplate = restTemplate;
		this.updatesService = updatesService;
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void sendNewMessageToDb(Message message) {
		try {
			log.info("send new message to db method inviked...");
			String oldId = message.getId();
			log.info("old id is " + oldId);
			RequestEntity<?> requestEntity = new RequestEntity<>(new MessageDTO(message), HttpMethod.POST, new URI("http://localhost:8081/messages"));
			ResponseEntity<Message> response = restTemplate.exchange(requestEntity, Message.class);
			message = response.getBody();
			log.info("message is " + message);
			rabbitTemplate.convertAndSend("test-chat-confirmed-key", new IDRegDTO(message.getId(), oldId));
			updatesService.confirmUpdate(oldId, message);
		}catch(URISyntaxException ex) {
			ex.printStackTrace();
		}
	}
	
	public Message getMessageFromDb(Long id) {
		ResponseEntity<Message> response = restTemplate.getForEntity("http://localhost:8081/messages/{id}", Message.class, id);
		Message message = response.getBody();
		
		return message;
	}
	
	public List<Message> getAllMessagesFromDb(){
		ResponseEntity<List<Message>> response = restTemplate.exchange("http://localhost:8081",HttpMethod.GET, null, new ParameterizedTypeReference<List<Message>>() {});
		List<Message> messages = response.getBody();
		
		return messages;
	}
	
	public void deleteMessageFromDb(Long id) {
		restTemplate.delete("http://localhost:8081/messages/{id}", id);
	}
	
	public void updateMessageFromDb(Message message) {
		try {
			RequestEntity<?> requestEntity = new RequestEntity<>(message, HttpMethod.PUT, new URI("http://localhost:8081/messages/" + message.getId()));
			ResponseEntity<Message> response = restTemplate.exchange(requestEntity, Message.class);
			message = response.getBody();
			updatesService.confirmUpdate(message.getId(), message);
		}catch(URISyntaxException ex) {
			ex.printStackTrace();
		}
	}
}
