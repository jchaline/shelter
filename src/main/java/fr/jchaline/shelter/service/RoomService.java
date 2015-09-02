package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.domain.Room;

/**
 * TODO : generate test data in dev mode only
 * @author jeremy
 *
 */
@Service
public class RoomService {
	
	@Autowired
	private RoomDao dao;
	
	public List<Room> list(){
		return dao.findAll();
	}
	
}
