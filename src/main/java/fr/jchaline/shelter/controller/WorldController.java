package fr.jchaline.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.World;

@RestController
@RequestMapping(value = "/world", method = RequestMethod.GET)
public class WorldController {
	
	@Autowired
	private WorldDao dao;
	
	@RequestMapping("/get")
	public World get() {
		return dao.findAll().stream().findFirst().get();
	}
}
