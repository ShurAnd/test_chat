package org.andrey.test_chat.service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import org.andrey.test_chat.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UpdatesService {
	
	private Logger log = LoggerFactory.getLogger(UpdatesService.class);

	@Qualifier("createQueue")
	private BlockingQueue<Message> createQueue;
	@Qualifier("updateQueue")
	private BlockingQueue<Message> updateQueue;
	@Qualifier("messageMap")
	private ConcurrentMap<String, Message> messageMap;

	@Autowired
	public UpdatesService(BlockingQueue<Message> createQueue, BlockingQueue<Message> updateQueue,
			ConcurrentMap<String, Message> messageMap) {
		this.createQueue = createQueue;
		this.updateQueue = updateQueue;
		this.messageMap = messageMap;
	}

	public void confirmUpdate(String id, Message message) {

		log.info("message is " + message + "\n" +
				 "message's id is " + message.getId() + "\n" +
				 "arg's id is " + id);
		
		
		messageMap.computeIfPresent(id, (a, b) -> {
			log.info("checking ids and neg it: " + a + " and " + message.getId() + " = " + a.equals(message.getId()));
			if (!a.equals(message.getId())) {
				b.setId(message.getId());
			}
			log.info("checking messages for equality: " + b + " and " + message + " = " + b.equals(message));
			if (!b.equals(message)) {
				try {
					updateQueue.put(b);
					return b;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			return null;
		});
	}

	public void sendMessageForUpdate(Message message) {
		
		log.info("message is " + message + "\n" +
				 "message's id is " + message.getId());
		log.info("check id for null value");
		if (message.getId() == null) return;
		log.info("check if message present in map");
		Message check = messageMap.computeIfPresent(message.getId(),
									(a, b) -> { 
										if (b.getCreatedAt().isBefore(message.getCreatedAt())) {
												return message;
										}else {
											return b;
										}
									});
		if (check == null) {
			check = messageMap.putIfAbsent(message.getId(), message);
			try {
				updateQueue.put(message);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void createNewMessageUpdate(Message message) {
		log.info("getting message " + message + " at " + LocalDateTime.now());
		log.info("map is " + messageMap);
		try {
			messageMap.put(message.getId(), message);
			log.info("putting message into queue...");
			createQueue.put(message);
			log.info("done!");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
