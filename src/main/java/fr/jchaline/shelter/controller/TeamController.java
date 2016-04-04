package fr.jchaline.shelter.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.json.TeamUp;
import fr.jchaline.shelter.service.TeamService;

@RestController
@RequestMapping(value = "/team", method = RequestMethod.GET)
public class TeamController {
	
	@Autowired
	private TeamService service;
	
	@RequestMapping("/list")
	public List<Team> list() {
		return service.list();
	}
	
	/**
	 * Create team before send it to mission
	 * @return the Team
	 */
	@RequestMapping(value = "/teamup", method = RequestMethod.POST)
	public Team teamUp(@RequestBody @Valid TeamUp team) {
		try{
			return service.teamUp(team);
		} catch (Exception e){
			throw e;
		}
	}
	
	@RequestMapping(value = "sendDuty/{teamId}/{dutyId}", method = RequestMethod.POST)
	public Team sendDuty(@PathVariable long teamId, @PathVariable long dutyId) {
		return service.sendDuty(teamId, dutyId);
	}
}
