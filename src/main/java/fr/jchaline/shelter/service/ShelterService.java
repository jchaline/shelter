package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.ShelterDao;
import fr.jchaline.shelter.domain.Shelter;

@Transactional(readOnly = true)
@Service
public class ShelterService {
	
	@Autowired
	private ShelterDao dao;
	
	public List<Shelter> list() {
		return dao.findAll();
	}

}
