package fr.jchaline.shelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.MapCellDao;
import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.domain.World;

@Service
@Transactional(readOnly = true)
public class WorldService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldService.class);
	
	@Autowired
	private WorldDao dao;
	
	@Autowired
	private MapCellDao mapCellDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	/**
	 * TODO : hide cities or sport in cities
	 * Get the world object, with removing unknow places => unknow cities or cities' spots
	 * @param username
	 * @return
	 */
	public World get(String username) {
		LOGGER.info("Load world for ", username);
		Player player = playerDao.findByName(username);
		
		World world = dao.findAll().get(0);
		world.getCities().stream().forEach(city -> {
			city.getStreets().values().stream().forEach(street -> {
				if (!player.getDiscoveredStreets().contains(street.getNumber())) {
					street.getSpots().clear();
				}
			});
		});
		return world;
	}

	public MapCell findCell(long cellId) {
		return mapCellDao.findOne(cellId);
	}

}
