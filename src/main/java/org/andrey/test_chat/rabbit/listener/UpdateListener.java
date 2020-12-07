package org.andrey.test_chat.rabbit.listener;

import java.util.List;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.UpdatesService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateListener {

	private RabbitTemplate rabbitTemplate;
	private UpdatesService updatesService;

	@Autowired
	public UpdateListener(RabbitTemplate rabbitTemplate,
							UpdatesService updatesService) {
		this.rabbitTemplate = rabbitTemplate;
		this.updatesService = updatesService;
	}

	
	@RabbitListener(queues="test-chat-update-queue")
	public void listenUpdates(Message message) {
		System.out.println("got message for update..." + message);
		System.out.println("sending message back to chat..");
		rabbitTemplate.convertAndSend("chat-update-response-key", message);
		updatesService.sendMessageForUpdate(message);
	}
}
