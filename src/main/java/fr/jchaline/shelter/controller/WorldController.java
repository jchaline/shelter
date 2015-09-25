package fr.jchaline.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.service.WorldService;

@RestController
@RequestMapping(value = "/world", method = RequestMethod.GET)
public class WorldController {
	
	@Autowired
	private WorldService service;
	
	@RequestMapping("/get")
	public World get() {
		return service.get();
	}
}
