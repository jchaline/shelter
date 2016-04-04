package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Item;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;
import fr.jchaline.shelter.service.ItemService;

@RestController
@RequestMapping(value = "/item", method = RequestMethod.GET)
public class ItemController extends AbstractShelterController {
	
	@Autowired
	private ItemService service;
	
	@RequestMapping("/list")
	public List<Item> list(){
		return service.list();
	}

	@RequestMapping("/weapons")
	public List<Weapon> weapons(){
		return service.listWeapons();
	}

	@RequestMapping("/suits")
	public List<Suit> suits(){
		return service.listSuits();
	}
	
}
