package fr.jchaline.shelter.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
		
		long food = computeFood(shelter, seconds);
		long water = computeWater(shelter, seconds);
		long money = computeMoney(shelter, seconds);

		shelter.setFood(shelter.getFood() + food);
		shelter.setWater(shelter.getWater() + water);
		shelter.setMoney(shelter.getMoney() + money);
		shelter.setLastCompute(now);
		dao.save(shelter);
		return shelter;
	}

	private long computeMoney(Shelter shelter, long time) {
		return 4500 * time;
	}

	private long computeWater(Shelter shelter, long time) {
		return 10500 * time;
	}

	private long computeFood(Shelter shelter, long time) {
		return 75000 * time;
	}

}
