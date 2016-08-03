package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Fighter;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.domain.Weapon;

@Transactional(readOnly = true)
@Service
public class FightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FightService.class);
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private WeaponDao weaponDao;

	@Autowired
	private SuitDao suitDao;
	
	/**
	 * Compute the fight
	 * @param team The player team
	 * @param badGuys The beast group
	 */
	public void fight(Team team, List<Beast> badGuys) {
		LOGGER.info("Fight between {} and {}", team, badGuys);
		
		//fight stop when one of the opponents is down
		List<Fighter> smashBrawl = new ArrayList<Fighter>();
		smashBrawl.addAll(team.getDwellers());
		smashBrawl.addAll(badGuys);
		while (!alives(team.getDwellers()).isEmpty() && !alives(badGuys).isEmpty()) {
			List<Fighter> alives = alives(smashBrawl);
			alives.sort((x,y) -> x.getSpeed() - y.getSpeed());
			alives.forEach(johnDoe -> {
				if (johnDoe.getLife() > 0) {
					//TODO : use map to store opponent list of each fighter instead Dweller Vs Beast
					List<? extends Fighter> list = (johnDoe instanceof Dweller) ? badGuys : team.getDwellers();
					Optional<Fighter> optTarget = chooseTarget(johnDoe, list);
					optTarget.ifPresent(target -> {
						processActions(johnDoe, target);
					});
				}
			});
		}
	}
	
	private void processActions(Fighter johnDoe, Fighter target) {
		for (int i=0; i<johnDoe.attackPerTurn(); i++) {
			int damage = johnDoe.computeDamage(target);
			double accuracy = johnDoe.computeAccuracy(target);
			if (target.getLife() > 0 && new Random().nextDouble() <= accuracy) {
				LOGGER.debug(String.format("%s attack %s with %d damage", johnDoe.toString(), target.toString(), damage));
				target.takeDamage(damage);
				LOGGER.debug(String.format("%s remain %d hp", target.toString(), target.getLife()));
			}
		}
	}
	
	/**
	 * Find the target !
	 * @param theGood
	 * @param badGuys
	 * @return
	 */
	public Optional<Fighter> chooseTarget(Fighter theGood, List<? extends Fighter> badGuys) {
		return alives(badGuys).stream().findAny();
	}
	
	/**
	 * Find the alives fighter's
	 * @param fighter
	 * @return
	 */
	public List<Fighter> alives(List<? extends Fighter> fighter) {
		return fighter.stream().filter(x->x.getLife()>0).collect(Collectors.toList());
	}
	
	
	/**
	 * Loot an item for a team, and add it to random teammate
	 * @param team The team
	 */
	public void loot(Team team) {
		int avgLevel = (int) Math.ceil(team.getDwellers().stream().mapToInt(Dweller::getLevel).summaryStatistics().getAverage());
		Suit randSuit = itemService.randSuit(avgLevel);
		Weapon randWeapon = itemService.randWeapon(avgLevel);
		team.getDwellers().stream().findAny().ifPresent(dweller -> {
			dweller.getItems().add(suitDao.save(randSuit));
			dweller.getItems().add(weaponDao.save(randWeapon));
		});
	}
	
	/**
	 * Compute the fight
	 * @param team The attack team
	 * @param team The defense team
	 */
	public void fight(Team aTeam, Team dTeam) {
		LOGGER.info("Fight between {} and {}", aTeam, dTeam);
	}

}
