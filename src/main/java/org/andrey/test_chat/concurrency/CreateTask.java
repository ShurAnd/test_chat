package org.andrey.test_chat.concurrency;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.MessageDataService;

public class CreateTask implements Runnable {

	private Message message;
	private MessageDataService messageDataService;

	public CreateTask(Message message, MessageDataService messageDataService) {
		this.message = message;
		this.messageDataService = messageDataService;
	}

	@Override
	public void run() {
			messageDataService.sendNewMessageToDb(message);
	}

}
