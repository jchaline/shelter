package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.DutyDao;
import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.MapCellDao;
import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.dao.TeamDao;
import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.Message;
import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.domain.Team;
import fr.jchaline.shelter.json.TeamUp;

@Transactional(readOnly = true)
@Service
public class CommandService {
	
	@Autowired
	private PlayerDao playerDao;
	
	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private DwellerDao dwellerDao;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private DutyDao dutyDao;
	
	@Autowired
	private MapCellDao mapCellDao;
	
	/**
	 * Send dweller to target, create team with dweller on same cell
	 * Example : go 2;3;4;5 3_4
	 * @param user the user
	 * @param command the command
	 * @return List of the team create for the go
	 */
	@Transactional(readOnly = false)
	public List<Message> go(String user, String command) {
		Player player = playerDao.findByName(user);
		
		String[] split = command.split(" ");
		
		String[] teamates = split[1].split(";");
		String targetName = split[2];
		final MapCell targetCell = mapCellDao.findByName(targetName);
		
		final Duty dutyExplore = dutyDao.findByName(Duty.EXPLORE);
		
		//make team with each dweller on same cell
		Stream<Dweller> dwellerStream = Stream.of(teamates).map(id -> dwellerDao.getOne(Long.valueOf(id)));
		Stream<Dweller> dwellerStreamFilter = dwellerStream.filter(d -> player.equals(d.getPlayer()) && d.getTeam() == null);
		Map<MapCell, List<Dweller>> groupByCell = dwellerStreamFilter.collect(Collectors.groupingBy(Dweller::getMapCell, Collectors.toList()));
		
		List<Message> messages = new ArrayList<Message>();
		
		groupByCell.entrySet().forEach(
				e -> {
				TeamUp t = new TeamUp();
				t.setDwellersId(e.getValue().stream().map(d->d.getId()).collect(Collectors.toList()));
				Team team = teamService.teamUp(t);
				teamService.sendDuty(team.getId(), dutyExplore.getId(), targetCell.getId());
				messages.add(new Message("Send Team " + team.getId() + " to " + targetCell.getName()));
		});
		
		return messages;
	}
	
	/**
	 * liste des dwellers
	 * Exemple : ls, ls 3
	 * @param user the user
	 * @param command the command
	 * @return List of player's dwellers
	 */
	public List<Message> ls(String user, String command) {
		String[] split = command.split(" ");
		
		Player currentPlayer = playerDao.findByName(user);
		List<Dweller> playerDwellers = dwellerDao.findAllByPlayer(currentPlayer);
		List<Message> collect = playerDwellers.stream().map(d -> new Message(d.toString())).collect(Collectors.toList());

		if (split.length>1 && StringUtils.isNumeric(split[1])) {
			Integer fuse = Integer.valueOf(split[1]);
			collect = merge(collect, fuse);
		}
		
		return collect;
	}

	/**
	 * liste des team.
	 * Exemple : ts, ts 3
	 * @param user the user
	 * @param command the command
	 * @return List of player's teams
	 */
	public List<Message> ts(String user, String command) {
		String[] split = command.split(" ");
		
		Player currentPlayer = playerDao.findByName(user);
		List<Team> playerTeams = teamDao.findAllByPlayer(currentPlayer);
		List<Message> collect = playerTeams.stream().sorted((a,b) -> a.getId().compareTo(b.getId())).map(t -> new Message(t.toString())).collect(Collectors.toList());
		
		if (split.length>1 && StringUtils.isNumeric(split[1])) {
			Integer fuse = Integer.valueOf(split[1]);
			collect = merge(collect, fuse);
		}
		
		return collect;
	}
	
	/**
	 * Test command, return the string arg
	 * @param command the command
	 * @return the message 
	 */
	public List<Message> echo(String command) {
		String[] split = command.split(" ");
		ArrayList<Message> list = new ArrayList<Message>();
		list.add(new Message(split[1]));
		return list;
	}
	
	/**
	 * Merge messages by group of n
	 * @param messages The messages to merge
	 * @param size the size of each group merged
	 * @return the new list of merged messages
	 */
	public List<Message> merge(List<Message> messages, int size) {
		int i = 1;
		Message add = null;
		List<Message> fused = new ArrayList<Message>();
		for (Message m : messages) {
			if (i == 1) {
				add = new Message(m.getContent());
			} else  if (i <= size) {
				add.setContent(StringUtils.join(Arrays.asList(add.getContent(), m.getContent()), " | "));
			}
			if (i == size) {
				fused.add(add);
				i = 0;
			}
			i++;
		}
		if (i != 1) {
			fused.add(add);
		}
		
		return fused;
	}
}
