package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.service.ShelterService;

@RestController
@RequestMapping(value = "/shelter", method = RequestMethod.GET)
public class ShelterController {
	
	@Autowired
	private ShelterService service;
	
	@RequestMapping("/list")
	public List<Shelter> list(){
		return service.list();
	}
	
}
