package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.domain.Player;

@Transactional(readOnly = true)
@Service
public class PlayerService {
	
	@Autowired
	private PlayerDao dao;
	
	public List<Player> list() {
		return dao.findAll();
	}
	
	public Player get(String name) {
		return dao.findByName(name);
	}

}
