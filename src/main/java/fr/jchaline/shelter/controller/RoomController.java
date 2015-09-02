package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.service.RoomService;

@RestController
@RequestMapping(value = "/room", method = RequestMethod.GET)
public class RoomController {
	
	@Autowired
	private RoomService service;
	
	@RequestMapping("/list")
	public List<Room> list(){
		List<Room> list = service.list();
		return list;
	}
	
	
}
