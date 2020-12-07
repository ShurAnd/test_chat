package org.andrey.test_chat.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.andrey.test_chat.concurrency.CreateQueueTask;
import org.andrey.test_chat.concurrency.UpdateQueueTask;
import org.andrey.test_chat.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UpdateExecutor {

	private static Logger log = LoggerFactory.getLogger(UpdateExecutor.class);
	private ExecutorService executorService;
	@Qualifier("createQueue")
	private BlockingQueue<Message> createQueue;
	@Qualifier("updateQueue")
	private BlockingQueue<Message> updateQueue;

	@Autowired
	public UpdateExecutor(BlockingQueue<Message> updateQueue, BlockingQueue<Message> createQueue, MessageDataService messageDataService) {
		log.info("creating executor for updates and creations");
		this.executorService = Executors.newCachedThreadPool();
		log.info("executorService created!");
		this.executorService.execute(new UpdateQueueTask(updateQueue, executorService, messageDataService));
		log.info("update task started!");
		this.executorService.execute(new CreateQueueTask(createQueue, executorService, messageDataService));
		log.info("create task started!");
	}

}
