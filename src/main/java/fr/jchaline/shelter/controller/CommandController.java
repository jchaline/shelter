package fr.jchaline.shelter.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Message;
import fr.jchaline.shelter.service.CommandService;

@RestController
@RequestMapping(value = "/command", method = RequestMethod.GET)
public class CommandController extends AbstractShelterController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandController.class);
	
	@Autowired
	private CommandService service;
	
	/**
	 * Transactionnal true or false manage by service.
	 *
	 * @param activeUser The current logged user for request
	 * @param command the command sent
	 * @return The messages result
	 */
	@RequestMapping(value="/ask", method = RequestMethod.POST)
	public List<Message> ask(@AuthenticationPrincipal User activeUser, @RequestParam String command) {
		LOGGER.debug("command : {}", command);
		List<Message> result = null;
		try {
			String[] split = command.split(" ");
			switch (split[0]) {
			case "echo" : result = service.echo(command); break;
			case "ls" : result = service.ls(activeUser.getUsername(), command); break;
			case "ts" : result = service.ts(activeUser.getUsername(), command); break;
			case "go" : result = service.go(activeUser.getUsername(), command); break;
			default: result = new ArrayList<Message>();
			}
		} catch (Exception e) {
			result = Arrays.asList(new Message(e.getMessage()));
		}
		return result;
	}
}
