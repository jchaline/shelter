package fr.jchaline.shelter.service;

import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.MapCellDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.World;

@Service
@Transactional(readOnly = true)
public class WorldService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldService.class);
	
	public static final String TERRE_1 = "Terre1";
	
	@Autowired
	private WorldDao dao;
	
	@Autowired
	private MapCellDao mapCellDao;
	
	/**
	 * TODO : hide cities or spot in cities
	 * Get the world object, with removing unknow places => unknow cities or cities' spots
	 * @param username
	 * @return
	 */
	public World get(String username, int xcenter, int ycenter) {
		LOGGER.trace("Load world for {}", username);
		World world = dao.findByName(TERRE_1);
		return limit(world, xcenter, ycenter);
	}

	/**
	 * Remove element to the world to limit the data transfert
	 * @param world The world to limit
	 * @param x The map view center xaxis
	 * @param y The map view center yaxis
	 * @return
	 */
	public World limit(World world, int x, int y) {
		int width = 40;
		int height = 40;//TODO : to constant
		
		LOGGER.info("world size before : {}", world.getMap().size());
		
		Iterator<Entry<String, MapCell>> iterator = world.getMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, MapCell> entry = iterator.next();
			if (Math.abs(entry.getValue().getXaxis() - x) > width / 2 || Math.abs(entry.getValue().getYaxis() - y) > height / 2) {
				iterator.remove();
			}
		}
		
		//unused by client
		world.setEdges(null);

		LOGGER.info("world size after : {}", world.getMap().size());
		
		return world;
	}
	
	public World get() {
		return dao.findByName(TERRE_1);
	}

	public MapCell findCell(long cellId) {
		return mapCellDao.findOne(cellId);
	}

}
