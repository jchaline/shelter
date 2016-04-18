package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.service.DutyService;

@RestController
@RequestMapping(value = "/duty", method = RequestMethod.GET)
public class DutyController extends AbstractShelterController {
	
	@Autowired
	private DutyService service;
	
	@RequestMapping("/listAction")
	public List<Duty> listAction() {
		return service.listAction();
	}
	
}
