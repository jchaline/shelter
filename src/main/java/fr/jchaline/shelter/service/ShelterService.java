package fr.jchaline.shelter.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.ShelterDao;
import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.enums.ResourceEnum;

@Transactional(readOnly = true)
@Service
public class ShelterService {
	
	private static final int MINIMUM_AMOUNT_RESOURCE_EARN = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);
	
	@Autowired
	private ShelterDao dao;
	
	@Autowired
	private RoomService roomService;
	
	public List<Shelter> list() {
		return dao.findAll();
	}
	
	@Transactional
	@Scheduled(fixedDelay = 2*60*1000)
	public void computeAll() {
		LOGGER.debug("Run computeAll for Shelters");
		dao.findAll().stream().forEach(it -> {
			computeAll(it);
		});
	}
	
	/**
	 * Compute money, food & water earn
	 * @param shelter
	 * @return
	 */
	@Transactional
	public Shelter computeAll(Shelter shelter) {
		LocalDateTime lastCompute = shelter.getLastCompute();
		LocalDateTime now = LocalDateTime.now();
		
		long seconds = lastCompute.until( now, ChronoUnit.SECONDS);
		
		long food = computeCoeff(shelter, ResourceEnum.FOOD);
		long water = computeCoeff(shelter, ResourceEnum.WATER);
		
		shelter.setFood(shelter.getFood() + seconds * food);
		shelter.setWater(shelter.getWater() + seconds * water);
		shelter.setLastCompute(now);
		dao.save(shelter);
		return shelter;
	}

	/**
	 * TODO : comment this method
	 * @param shelter
	 * @param resource
	 * @return
	 */
	public long computeCoeff(Shelter shelter, ResourceEnum resource) {
		return shelter.getFloors().values().stream()
				.mapToInt(floor -> floor.getRooms()
							.stream()
							.filter(r -> resource.equals(r.getRoomType().getResource()))
							.collect( Collectors.summingInt( roomService::earnPerSecond ))
				)
				.sum() + MINIMUM_AMOUNT_RESOURCE_EARN;
	}

	public Map<String, Long> getCoeff(long id) {
		Map<String, Long> res = new HashMap<String,Long>();
		Shelter shelter = dao.findOne(id);
		res.put("water", computeCoeff(shelter, ResourceEnum.WATER));
		res.put("money", computeCoeff(shelter, ResourceEnum.MONEY));
		res.put("food", computeCoeff(shelter, ResourceEnum.FOOD));
		res.put("power", computeCoeff(shelter, ResourceEnum.POWER));
		res.put("powerRequired", computePowerRequired(shelter));
		return res;
	}

	private long computePowerRequired(Shelter shelter) {
		return 100l;
	}

}
