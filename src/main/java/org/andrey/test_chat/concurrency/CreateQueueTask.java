package org.andrey.test_chat.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.MessageDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

public class CreateQueueTask implements Runnable{

	private static Logger log = LoggerFactory.getLogger(CreateQueueTask.class);
	
	private BlockingQueue<Message> createQueue;
	private ExecutorService executor;
	private MessageDataService messageDataService;
	
	
	
	public CreateQueueTask(BlockingQueue<Message> updateQueue, ExecutorService executor, MessageDataService messageDataService) {
		this.createQueue = updateQueue;
		this.executor = executor;
		this.messageDataService = messageDataService;
	}



	@Override
	public void run() {
		while(true) {
			Message message = null;
			try {
				log.info("taking message from queue...");
				message = createQueue.take();
				log.info("message " + message + " taken!");
			}catch(InterruptedException ex) {
				ex.printStackTrace();
			}
			
			log.info("create task for saving message into db...");
			CreateTask createTask = new CreateTask(message, messageDataService);
			log.info("task created!");
			executor.execute(createTask);
			log.info("task sended to executor!");
		}
		
	}

}
