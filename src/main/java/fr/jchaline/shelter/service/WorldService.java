package fr.jchaline.shelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.domain.World;

@Service
@Transactional(readOnly = true)
public class WorldService {
	
	@Autowired
	private WorldDao dao;
	
	@Autowired
	private PlayerDao playerDao;
	
	public World get() {
		//TODO : improve, split with cities list & city get
		Player player = playerDao.findAll().get(0);
		
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

}
