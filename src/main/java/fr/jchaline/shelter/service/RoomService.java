package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.domain.Room;

/**
 * @author jeremy
 *
 */
@Service
@Transactional(readOnly = true)
public class RoomService {
	
	@Autowired
	private RoomDao dao;
	
	public List<Room> list(){
		return dao.findAll();
	}
	
	/**
	 * Merge two room if possible, and save in db
	 * @param idA The id of the first room
	 * @param idB The id of the second room
	 * @return the new merged room
	 */
	@Transactional
	public Room merge(long idA, long idB) {
		final Room right = dao.getOne(idA);
		final Room left = dao.getOne(idB);
		right.setSize(right.getSize()+left.getSize());
		dao.save(right);
		dao.delete(left);
		return right;
	}
	
}
