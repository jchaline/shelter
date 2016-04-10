package fr.jchaline.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Message;
import fr.jchaline.shelter.service.MessageService;

@RestController
@RequestMapping(value = "/message", method = RequestMethod.GET)
public class MessageController extends AbstractShelterController {
	
	@Autowired
	private MessageService service;
	
	@RequestMapping("/last")
	public Page<Message> last(@RequestParam int page, @RequestParam int size) {
		return service.last(page, size);
	}
}
