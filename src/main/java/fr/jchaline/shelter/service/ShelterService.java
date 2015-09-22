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

@Transactional(readOnly = true)
@Service
public class ShelterService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);
	
	@Autowired
	private ShelterDao dao;
	
	public List<Shelter> list() {
		return dao.findAll();
	}
	
	@Transactional
	@Scheduled(fixedDelay=10000)
	public void computeAll() {
		LOGGER.debug("Run computeAll for Shelters");
		dao.findAll().parallelStream().forEach(it -> {
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
		long coeff = computeCoeff(shelter);
		long res = coeff * seconds;
		
		shelter.setFood(shelter.getFood() + res);
		shelter.setWater(shelter.getWater() + res);
		shelter.setMoney(shelter.getMoney() + res);
		shelter.setLastCompute(now);
		dao.save(shelter);
		return shelter;
	}

	public long computeCoeff(Shelter shelter) {
		return shelter.getFloors().values().parallelStream()
				.mapToInt(floor -> floor.getRooms().parallelStream().collect( Collectors.summingInt( RoomService::earnPerSecond ) ))
				.sum();
	}

	public Map<String, Long> getCoeff(long id) {
		Map<String, Long> res = new HashMap<String,Long>();
		Shelter shelter = dao.findOne(id);
		long coeff = computeCoeff(shelter);
		res.put("water", coeff);
		res.put("power", coeff);
		res.put("food", coeff);
		return res;
	}

}
