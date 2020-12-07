package org.andrey.test_chat.rabbit.listener;

import java.util.List;
import java.util.UUID;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.MessageDataService;
import org.andrey.test_chat.service.UpdatesService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChatListener {

	private MessageDataService messageDataService;
	private RabbitTemplate rabbitTemplate;
	private UpdatesService updatesService;
	
	@Autowired
	public ChatListener(MessageDataService messageDataService, RabbitTemplate rabbitTemplate, UpdatesService updatesService) {
		this.messageDataService = messageDataService;
		this.rabbitTemplate = rabbitTemplate;
		this.updatesService = updatesService;
	}
	
	@RabbitListener(queues="test-chat-queue")
	public void chatMessage(Message message) {
		System.out.println("got message " + message);
		System.out.println("preparing message for sending to db...");
		System.out.println("creating temporary id...");
		String tempId = UUID.randomUUID().toString();
		System.out.println("temp id is " + tempId);
		message.setId(tempId);
		System.out.println("creating update message object...");
		updatesService.createNewMessageUpdate(message);
		System.out.println("sending message back to chat");
		rabbitTemplate.convertAndSend("response-chat-key", message);
	}
}
