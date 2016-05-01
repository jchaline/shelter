package fr.jchaline.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.service.WorldService;

@RestController
@RequestMapping(value = "/world", method = RequestMethod.GET)
public class WorldController extends AbstractShelterController {

	@Autowired
	private WorldService service;

	@RequestMapping("/get")
	public World get(@AuthenticationPrincipal User activeUser, @RequestParam int xcenter, @RequestParam int ycenter) {
		return service.get(activeUser.getUsername(), xcenter, ycenter);
	}

	@RequestMapping("/cell/{cellId}")
	public MapCell cell(@PathVariable long cellId) {
		return service.findCell(cellId);
	}
}
