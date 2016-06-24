package fr.jchaline.shelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.MessageDao;
import fr.jchaline.shelter.domain.Message;

@Transactional(readOnly = true)
@Service
public class MessageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	private MessageDao messageDao;
	
	public Page<Message> last(int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "dateCreate");
		
		return messageDao.findAll(pageRequest);
	}
	
	/**
	 * Use %d and %s to format the message
	 * @param message
	 * @param args
	 * @return
	 */
	@Transactional(readOnly = false)
	public Message push(String message, Object...args) {
		String msg = String.format(message, args);
		LOGGER.debug(msg);
		return messageDao.save(new Message(msg));
	}
}
