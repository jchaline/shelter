package fr.jchaline.shelter.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.BeastDao;
import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.utils.AlgoUtils;

@Transactional(readOnly = true)
@Service
public class BeastService {
	
	@Autowired
	private BeastDao beastDao;
	
	/**
	 * TODO : rules for making beasts group.... The more time passe, the more level grow
	 * Create a group (List) of beast
	 * @param teamSize The size of the team which confront the beasts group
	 * @param avgLevel The avg level of the team
	 * @return The group
	 */
	public List<Beast> makeGroup(Team team) {
		//1 : compute level
		double avgLevel = team.getDwellers().stream().mapToInt(Dweller::getLevel).summaryStatistics().getAverage();
		
		//2 : compute group size
		int teamSize = team.getDwellers().size();
		
		//3 : find beast
		int plusLevel = (int) Duration.between(team.getBegin(), LocalDateTime.now()).getSeconds() / 120;
		List<Beast> range = beastDao.findByLevelBetween(1, (int) avgLevel + plusLevel);
		
		List<Beast> asList = new ArrayList<>();
		for (int i = 0; i < teamSize + new Random().nextInt(teamSize); i++) {
			Beast clone = new Beast();
			BeanUtils.copyProperties(AlgoUtils.rand(range), clone);
			asList.add(clone);
			//TODO : assert modify clone doesn't modify origin
		}
		
		return asList;
	}

}
