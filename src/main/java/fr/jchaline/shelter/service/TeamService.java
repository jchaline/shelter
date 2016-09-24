package fr.jchaline.shelter.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import fr.jchaline.shelter.dao.MapCellDao;
import fr.jchaline.shelter.dao.TeamDao;
import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.enums.SpecialEnum;
import fr.jchaline.shelter.exception.BusinessException;
import fr.jchaline.shelter.json.TeamUp;

@Transactional(readOnly = true)
@Service
public class TeamService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);
	
	@Value("${event.frequency.second}")
	private int frequency;
	
	@Autowired
	private DwellerDao dwellerDao;
	
	@Autowired
	private DutyDao dutyDao;
	
	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private MapService mapService;

	@Autowired
	private WorldService worldService;
	
	@Autowired
	private MapCellDao mapCellDao;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private BeastService beastService;
	
	@Autowired
	private FightService fightService;
	
	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_EXPLORE)
	public void scheduleExploring() {
		LOGGER.trace("update exploring for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.EXPLORE)).stream().forEach(it -> {
			computeExplore(it);
		});
	}
	
	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_EXPLORE)
	public void scheduleReturn() {
		LOGGER.trace("update return for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.RETURN)).stream().forEach(it -> {
			computeExplore(it);
		});
	}
	
	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_FIGHT)
	public void scheduleFight() {
		LOGGER.trace("update fighting for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.FIGHT)).stream().forEach(team -> {
			
			tryToMove(team, team.getTarget());
			if (team.getCurrent().equals(team.getTarget())) {
				team.setDuty(null);
				team.setTarget(null);
			}
			
			//TODO : second step, fight !
		});
	}
	
	@Transactional(readOnly = false)
	@Scheduled(fixedDelay = ShelterConstants.TEAM_RECRUITMENT)
	public void scheduleRecruitment() {
		LOGGER.trace("update recruitment for all team");
		
		//find team and compute event
		teamDao.findByDuty(dutyDao.findByName(Duty.RECRUITMENT)).stream().forEach(it -> {
			computeRecruitment(it);
		});
	}
	
	/**
	 * Move the team to the target cell
	 * @param team The team to move
	 * @param target The target cell
	 */
	private void tryToMove(Team team, MapCell target) {
		if (!team.getCurrent().equals(target)) {
			Stream<Integer> map = team.getDwellers().stream().map(d -> d.getSpecial().getValue(SpecialEnum.A));
			OptionalDouble averageSpeedOpt = map.mapToInt(i -> i.intValue()).average();
			averageSpeedOpt.ifPresent(speed -> {
				LocalDateTime now = LocalDateTime.now();
				//Coefficient for the speed, the greatest it is, the fastest the team is
				double cellFrequencyCoeff = speed / 10;
				//the real number of seconds to wait for the current team between each move
				long secondToWaitComputed = Math.round(1 / cellFrequencyCoeff * ShelterConstants.SECOND_BETWEEN_MOVE);
				
				int nbCellToMove = (int) Math.floor(Math.abs(Duration.between(team.getLastMove(), now).getSeconds() / secondToWaitComputed));
				
				//2:if can move, find path to target
				if (nbCellToMove > 0) {
					//3:move team & dwellers
					Optional<List<MapCell>> pathOpt = mapService.computePath(worldService.get(), team.getCurrent(), target);
					
					//TODO : manage case when team is enought fast to move twice or more cell by turn
					pathOpt.ifPresent(path -> {
						team.setCurrent(path.get(nbCellToMove - 1));
						team.setLastMove(now);
						team.getDwellers().forEach(dweller -> dweller.setMapCell(path.get(nbCellToMove - 1)));
					});
					
					if (!pathOpt.isPresent()) {
						messageService.push("No path to current target of team %d ...", team.getId());
					}
				}
			});
		}
	}

	private void computeRecruitment(Team team) {
		//LocalDateTime now = LocalDateTime.now();
		//first, move to the target cell. The farthest you will go, the luckiest you will be
		
		//when arrive, search to recruit
		
		//check if event should happen
	}
	
	/**
	 * 
	 * @param team
	 */
	@Transactional(readOnly = false)
	public void computeExplore(Team team) {
		LocalDateTime now = LocalDateTime.now();
		
		tryToMove(team, team.getTarget());
		
		if (team.getCurrent().equals(team.getTarget())) {
			team.setDuty(null);
			team.setTarget(null);
		}
		
		//check if event should happen
		if (team.getLastEvent().plusSeconds(frequency).isBefore(now)) {
			//random for battle event, loot event, ...
			double random = Math.random();
			if (random > 0.5) {
				happenFight(team);
			} else if (random < 0.15) {
				happenLoot(team);
			}
			team.setLastEvent(now);
		}
	}
	
	/**
	 * This event aim to give a mighty (or not...) loot for the team :)
	 * @param team The team
	 */
	private void happenLoot(Team team) {
		messageService.push("Team %d find loot !", team.getId());
		//fightService.loot(team);
	}
	
	/**
	 * This event aim to start fight versus random bad guys
	 * @param team The team
	 */
	private void happenFight(Team team) {
		List<Beast> beastGroup = beastService.makeGroup(team);
		
		IntSummaryStatistics summaryStatistics = beastGroup.stream().mapToInt(Beast::getLevel).summaryStatistics();
		messageService.push("Team %d meet a beasts group ! Size %d, from level %d to %d", team.getId(), beastGroup.size(), summaryStatistics.getMin(), summaryStatistics.getMax());

		fightService.fight(team, beastGroup);
		
		Iterator<Dweller> iterator = team.getDwellers().iterator();
		while (iterator.hasNext()) {
			Dweller next = iterator.next();
			if (next.getLife() <= 0) {
				dwellerDao.delete(next);
				iterator.remove();
			}
		};
		
		
		//if all dwellers dies, stop the move, remove the team
		if (team.getDwellers().isEmpty()) {
			messageService.push("The team %d loose the fight ...", team.getId());
			teamDao.delete(team);
		} else {
			int exp = beastGroup.stream().mapToInt(b -> b.computeExperience()).sum();
			int eachExp = (int) exp / team.getDwellers().size();
			team.getDwellers().forEach(d -> {
				d.addExperience(eachExp);
				d.setLife(d.getMaxLife());
				
				//TODO : improve this ...
				d.getPlayer().setMoney(d.getPlayer().getMoney() + eachExp * 10);
			});
			messageService.push("The team %d win the fight !", team.getId());
		}
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
	 * @param targetId The target cell id
	 * @return The Team sent
	 */
	@Transactional(readOnly = false)
	public synchronized Team sendDuty(Long teamId, Long dutyId, Long targetId) {
		Team team = teamDao.findOne(teamId);
		if (team.getDuty() != null) {
			throw new BusinessException("Team already with duty !");
		}
		
		MapCell targetCell = mapCellDao.findOne(targetId);
		if (targetCell == null) {
			throw new BusinessException("Target doesn't exist !");
		}
		
		LocalDateTime beforePath = LocalDateTime.now();
		Optional<List<MapCell>> pathOpt = mapService.computePath(worldService.get(), team.getCurrent(), targetCell);
		LocalDateTime afterPath = LocalDateTime.now();
		LOGGER.info("Find path ({}) for duty in {} seconds", pathOpt.isPresent(), Duration.between(beforePath, afterPath).getSeconds());

		LocalDateTime start = LocalDateTime.now();
		team.setBegin(start);
		team.setLastEvent(start);
		team.setLastMove(start);
		
		Duty duty = dutyDao.findOne(dutyId);
		team.setDuty(duty);
		team.setTarget(targetCell);
		team.setOrigin(team.getCurrent());
		LOGGER.debug("send team to duty !");
		return teamDao.save(team);
	}

	public List<Team> list() {
		return teamDao.findAll();
	}
	
	public Team get(long teamId) {
		return teamDao.findOne(teamId);
	}

	/**
	 * Cancel the current duty for a team
	 * @param teamId The team id
	 * @return The team
	 */
	@Transactional(readOnly = false)
	public Team cancelDuty(long teamId) {
		Duty dutyReturn = dutyDao.findByName(Duty.RETURN);
		Team team = teamDao.findOne(teamId);
		team.setDuty(null);
		team.setDuty(dutyReturn);
		team.setTarget(team.getOrigin());
		return team;
	}

	/**
	 * Disband team, to remove it from the view
	 * @param teamId The team id
	 * @return True if team disband
	 */
	@Transactional(readOnly = false)
	public boolean disband(long teamId) {
		Team team = teamDao.findOne(teamId);
		if (team != null && team.getDuty() == null) {
			team.getDwellers().forEach(d -> d.setTeam(null));
			teamDao.delete(team);
		} else {
			throw new BusinessException("Team with duty or not exist !"); 
		}
		return true;
	}
}
