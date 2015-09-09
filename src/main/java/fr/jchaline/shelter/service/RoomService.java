package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.Constant;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
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

	@Autowired
	private RoomTypeDao roomTypeDao;
	
	public List<Room> list(){
		return dao.findAll();
	}
	
	/**
	 * Add a room if possible
	 * @param floor
	 * @param pos
	 */
	public void addRoom(long idType, int floor, int pos) {
		
	}
	
	/**
	 * Check if it's possible to add a room to the shelter.
	 * Vertical : only elevator (if enough place), Horizontal : only if adjoins left/right to another room and enough place.
	 * If you try to add room to cell, this method check right and left to find enough place (if the room take more than 1 cell).
	 * @param idType
	 * @param floor
	 * @param pos
	 * @return
	 */
	public boolean canAddRoom(long idType, int floor, int pos) {
		//first, it is an elevator ?
		if (roomTypeDao.findByName(Constant.ELEVATOR).getId().equals(idType)) {
			//room adjoins left/right or elevator at top/bottom ?
		}
		//else, another room adjoins left/right ?
		else {
			
		}
		return false;
	}
	
	public boolean hasRoomAdjoins(int floor, int pos) {
		return false;
	}
	
	public boolean hasElevatorAdjoins(int floor, int pos) {
		return false;
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
