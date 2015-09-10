package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.GameDao;
import fr.jchaline.shelter.domain.Game;

@Transactional(readOnly = true)
@Service
public class GameService {
	
	@Autowired
	private GameDao dao;
	
	public List<Game> list(){
		return dao.findAll();
	}

}
