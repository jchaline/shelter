package fr.jchaline.shelter.service;

import java.util.IntSummaryStatistics;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.domain.Weapon;

@Transactional(readOnly = true)
@Service
public class FightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FightService.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * compute the fight
	 * @param team The player team
	 * @param opponent The beast group
	 */
	public void fight(Team team, List<Beast> opponent) {
		LOGGER.info("Fight between {} and {}", team, opponent);
		IntSummaryStatistics summaryStatistics = opponent.stream().mapToInt(Beast::getLevel).summaryStatistics();
		messageService.push("Team %d meet a beasts group ! Size %d, from level %d to %d", team.getId(), opponent.size(), summaryStatistics.getMin(), summaryStatistics.getMax());
	}
	
	public void loot(Team team) {
		int avgLevel = (int)team.getDwellers().stream().mapToInt(Dweller::getLevel).summaryStatistics().getAverage();
		Suit randSuit = itemService.randSuit(avgLevel);
		Weapon randWeapon = itemService.randWeapon(avgLevel);
		team.getDwellers().stream().findAny().ifPresent(dweller -> {
			dweller.getItems().add(randSuit);
			dweller.getItems().add(randWeapon);
		});
	}
	
	/**
	 * 
	 * @param aTeam The attack Team
	 * @param dTeam The defense Team
	 */
	public void fight(Team aTeam, Team dTeam) {
		LOGGER.info("Fight between {} and {}", aTeam, dTeam);
	}

}
