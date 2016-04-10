package fr.jchaline.shelter.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.dao.DutyDao;
import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.ItemDao;
import fr.jchaline.shelter.dao.TeamDao;
import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.exception.BusinessException;
import fr.jchaline.shelter.json.TeamUp;

@Transactional(readOnly = true)
@Service
public class TeamService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);
	
	@Value("${event.frequency.minutes}")
	private int frequency;
	
	@Autowired
	private DwellerDao dwellerDao;
	
	@Autowired
	private DutyDao dutyDao;
	
	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_EXPLORE)
	public void updateExploring() {
		LOGGER.debug("update exploring for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.EXPLORE)).stream().forEach(it -> {
			
			computeExplore(it);
			
			//TODO : update discoveredStreets for the player !
		});
	}

	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_RECRUITMENT)
	public void updateRecruitment() {
		LOGGER.debug("update recruitment for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.RECRUITMENT)).stream().forEach(it -> {
			computeRecruitment(it);
		});
	}

	private void computeRecruitment(Team team) {
		//LocalDateTime now = LocalDateTime.now();
		//first, move to the target cell. The farthest you will go, the luckiest you will be
		
		//when arrive, search to recruit
		
		//check if event should happen
	}

	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_FIGHT)
	public void updateFight() {
		LOGGER.debug("update fighting for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.FIGHT)).stream().forEach(it -> {
			
			//TODO : first step, move to the place,
			
			//TODO : second step, fight !
		});
	}
	
	/**
	 * 
	 * @param team
	 */
	@Transactional(readOnly = false)
	public void computeExplore(Team team) {
		LocalDateTime now = LocalDateTime.now();
		
		//check if event should happen
		if (team.getLastEvent().plusMinutes(frequency).isBefore(now)) {
			//random for battle event, loot event, ...
			double random = Math.random();
			if (random > 0.5) {
				happenFight(team);
			} else if (random > 0.2) {
				happenLoot(team);
			}
		}
	}
	
	/**
	 * This event aim to give a mighty (or not...) loot for the team :)
	 * @param team The team
	 */
	private void happenLoot(Team team) {
		//TODO : todo !
		//get items level min, max and avg, generate number, apply coeff, and find item with the closest level
		Integer maxLevel = itemDao.findMaxLevel();
		Integer minLevel = itemDao.findMinLevel();
		//TODO : ponderate with dweller avg level & dweller number
		int random = new Random().nextInt(maxLevel - minLevel)  + minLevel;
		LOGGER.debug("Current min level : ", minLevel);
		LOGGER.debug("Current max level : ", maxLevel);
		LOGGER.debug("Random result : ", random);
	}
	
	/**
	 * This event aim to start fight versus random bad guys
	 * @param team The team
	 */
	private void happenFight(Team team) {
		
	}
	
	/**
	 * Create team for mission
	 * Explore refresh by {@link WorldService#updateExploring()}
	 * @return the Team
	 */
	@Transactional(readOnly = false)
	public synchronized Team teamUp(TeamUp teamup) {
		Team team = new Team();
		List<Dweller> teammates = teamup.getDwellersId().stream()
										.map(dwellerDao::findOne)
										.filter(x -> x.getTeam() == null || x.getTeam().getDuty() == null)
										.collect(Collectors.toList());
		Optional<Dweller> firstTeammate = teammates.stream().findFirst();
		if (firstTeammate.isPresent()) {
			team.setCurrent(firstTeammate.get().getMapCell());
		} else {
			throw new BusinessException("Impossible to make team with no dwellers !");
		}
		
		team.setDwellers(teammates);
		teamDao.save(team);
		teammates.forEach(d -> {
			
			//if dweller has alreay a team, remove from it
			if (d.getTeam() != null) {
				d.getTeam().getDwellers().remove(d);
				teamDao.save(d.getTeam());
				
				//delete team if empty
				if (d.getTeam().getDwellers().size() == 0) {
					teamDao.delete(d.getTeam());
				}
			}
			d.setTeam(team);
			dwellerDao.save(d);	
		});
		
		return teamDao.save(team);
	}

	/**
	 * TODO : explain how synchronized work in this case
	 * Send the team to duty
	 * @param teamId The team id
	 * @param dutyId The duty id
	 * @return The Team sent
	 */
	@Transactional(readOnly = false)
	public synchronized Team sendDuty(Long teamId, Long dutyId) {
		Team team = teamDao.findOne(teamId);
		if (team.getDuty() == null) {
			LocalDateTime start = LocalDateTime.now();
			team.setBegin(start);
			team.setLastEvent(start);
			
			Duty duty = dutyDao.findOne(dutyId);
			team.setDuty(duty);
			return teamDao.save(team);
		} else {
			throw new BusinessException("Team already with duty !");
		}
	}

	public List<Team> list() {
		return teamDao.findAll();
	}
}
