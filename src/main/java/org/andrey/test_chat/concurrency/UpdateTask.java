package org.andrey.test_chat.concurrency;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.MessageDataService;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateTask implements Runnable {

	private Message message;
	private MessageDataService messageDataService;

	public UpdateTask(Message message, MessageDataService messageDataService) {
		super();
		this.message = message;
		this.messageDataService = messageDataService;
	}

	@Override
	public void run() {
		messageDataService.updateMessageFromDb(message);
	}

}
