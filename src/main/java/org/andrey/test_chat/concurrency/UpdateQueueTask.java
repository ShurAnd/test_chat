package org.andrey.test_chat.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.andrey.test_chat.domain.Message;
import org.andrey.test_chat.service.MessageDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateQueueTask implements Runnable {

	private static Logger log = LoggerFactory.getLogger(UpdateQueueTask.class);
	
	private BlockingQueue<Message> updateQueue;
	private ExecutorService executor;
	private MessageDataService messageDataService;

	public UpdateQueueTask(BlockingQueue<Message> updateQueue, ExecutorService executor,
			MessageDataService messageDataService) {
		this.updateQueue = updateQueue;
		this.executor = executor;
		this.messageDataService = messageDataService;
	}

	@Override
	public void run() {
		while (true) {
			Message message = null;
			try {
				log.info("waiting message from update queue...");
				message = updateQueue.take();
				log.info("got message from update queue! " + message);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			log.info("creating update task...");
			UpdateTask updateTask = new UpdateTask(message, messageDataService);
			log.info("task created!");
			executor.execute(updateTask);
			log.info("task sended to execute...");
		}

	}

}
