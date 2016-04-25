package fr.jchaline.shelter.service;

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
	
	@Autowired
	private MessageDao messageDao;
	
	public Page<Message> last(int page, int size) {
		PageRequest pageRequest = new PageRequest(page, size, Direction.DESC, "dateCreate");
		
		return messageDao.findAll(pageRequest);
	}
	
	@Transactional(readOnly = false)
	public Message push(String message, Object...args) {
		return messageDao.save(new Message(String.format(message, args)));
	}
}
