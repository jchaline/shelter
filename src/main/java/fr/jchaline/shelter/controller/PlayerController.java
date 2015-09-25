package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.service.PlayerService;

@RestController
@RequestMapping(value = "/player", method = RequestMethod.GET)
public class PlayerController {
	
	@Autowired
	private PlayerService service;
	
	@RequestMapping("/list")
	public List<Player> list() {
		return service.list();
	}
	
	@RequestMapping("/get")
	public Player get() {
		return service.get();
	}

}
