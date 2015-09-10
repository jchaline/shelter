package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Game;
import fr.jchaline.shelter.service.GameService;

@RestController
@RequestMapping(value = "/game", method = RequestMethod.GET)
public class GameController {
	
	@Autowired
	private GameService service;
	
	@RequestMapping("/list")
	public List<Game> list() {
		return service.list();
	}

}
