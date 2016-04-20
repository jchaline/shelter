package fr.jchaline.shelter.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.json.TeamUp;
import fr.jchaline.shelter.service.TeamService;

@RestController
@RequestMapping(value = "/team", method = RequestMethod.GET)
public class TeamController extends AbstractShelterController {
	
	@Autowired
	private TeamService service;
	
	@RequestMapping("/list")
	public List<Team> list() {
		return service.list();
	}

	//TODO : requestParam vs @PathVariable, who's the best for GET http method ?
	@RequestMapping("/get")
	public Team get(@RequestParam long teamId) {
		return service.get(teamId);
	}
	
	/**
	 * Create team before send it to mission
	 * @return the Team
	 */
	@RequestMapping(value = "/teamup", method = RequestMethod.POST)
	public Team teamUp(@RequestBody @Valid TeamUp team) {
		return service.teamUp(team);
	}
	
	@RequestMapping(value = "/sendDuty", method = RequestMethod.POST)
	public Team sendDuty(@RequestParam long teamId, @RequestParam long dutyId, @RequestParam long target) {
		return service.sendDuty(teamId, dutyId, target);
	}

	@RequestMapping(value = "/disband", method = RequestMethod.POST)
	public boolean disband(@RequestParam long teamId) {
		return service.disband(teamId);
	}

	@RequestMapping(value = "/cancelDuty", method = RequestMethod.POST)
	public Team cancelDuty(@RequestParam long teamId) {
		return service.cancelDuty(teamId);
	}
	
}
