package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.service.PlayerService;

@RestController
@RequestMapping(value = "/player", method = RequestMethod.GET)
public class PlayerController extends AbstractShelterController {
	
	@Autowired
	private PlayerService service;
	
	@RequestMapping("/list")
	public List<Player> list() {
		return service.list();
	}
	
	/**
	 * Get the current player connected, with User injected in this endpoint
	 * @param activeUser the current user connected
	 * @return the Player data
	 */
	@RequestMapping("/get")
	@Secured("ROLE_USER")
	public Player get(@AuthenticationPrincipal User activeUser) {
		return service.get(activeUser.getUsername());
	}

}
