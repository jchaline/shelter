package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.DutyDao;
import fr.jchaline.shelter.domain.Duty;

@Transactional(readOnly = true)
@Service
public class DutyService {
	
	@Autowired
	private DutyDao dutyDao;

	public List<Duty> list() {
		return dutyDao.findAll();
	}

	/**
	 * Find all the action duties
	 * @return The action duties
	 */
	public List<Duty> listAction() {
		return dutyDao.findByAction(true);
	}
}
