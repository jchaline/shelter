package fr.jchaline.shelter.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.Constant;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;

/**
 * @author jeremy
 *
 */
@Service
@Transactional(readOnly = true)
public class RoomService {
	
	@Autowired
	private FloorDao floorDao;

	@Autowired
	private RoomDao dao;

	@Autowired
	private RoomTypeDao roomTypeDao;
	
	public List<Room> list() {
		return dao.findAll();
	}
	
	/**
	 * Check if it's possible to add a room to the shelter.
	 * Vertical : only elevator (if enough place), Horizontal : only if adjoins left/right to another room and enough place.
	 * If you try to add room to cell, this method check right and left to find enough place (if the room take more than 1 cell).
	 * @param idType
	 * @param floor
	 * @param cell
	 * @return
	 */
	public Optional<Set<Integer>> canAddRoom(long idType, long idFloor, int cell) {
		if (isCellEmpty(idFloor, cell)) {
			
			Optional<Set<Integer>> okHorizontal = isOkHorizontal(idType, idFloor, cell);

			//first, it is an elevator ?
			if (roomTypeDao.findByName(Constant.ELEVATOR).getId().equals(idType)) {
				if(hasElevatorVertical(idFloor, cell) || okHorizontal.isPresent()) {
					return Optional.of(Stream.of(cell).collect(Collectors.toSet()));
				}
			}
			//else, another room adjoins left/right ?
			else {
				if (isCellEmpty(idFloor, cell) && okHorizontal.isPresent()) {
					return okHorizontal;
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * If a new room can be add
	 * @param idFloor
	 * @param cell
	 * @return
	 */
	public Optional<Set<Integer>> isOkHorizontal(long idType, long idFloor, int cell) {
		RoomType type = roomTypeDao.findOne(idType);
		Room right = dao.findOneByFloorAndCell(idFloor, cell + 1);
		Room left = dao.findOneByFloorAndCell(idFloor, cell - 1);
		boolean rightEmpty = right == null;
		boolean leftEmpty = left == null;
		
		if (type.getSize() == 1 && (!rightEmpty || !leftEmpty)) {
			return Optional.of(Stream.of(cell).collect(Collectors.toSet()));
		} else {
			if (leftEmpty ^ rightEmpty) {
				return Optional.of(Stream.of(cell, leftEmpty ? cell - 1 : cell + 1).collect(Collectors.toSet()));
			}
		}
		return Optional.empty();
	}
	
	public boolean isCellEmpty(long idFloor, int cell) {
		return dao.findByFloorAndCell(idFloor, cell).isEmpty();
	}
	
	public boolean hasElevatorVertical(long idFloor, int cell) {
		Floor floor = floorDao.findOne(idFloor);
		Floor downstair = floorDao.findByNumber(floor.getNumber() + 1);
		Floor upstair = floorDao.findByNumber(floor.getNumber() - 1);
		Room roomUp = upstair != null ? dao.findOneByFloorAndCell(upstair.getId(), cell) : null;
		Room roomDown = downstair != null ? dao.findOneByFloorAndCell(downstair.getId(), cell) : null;
		return roomUp != null && Constant.ELEVATOR.equals(roomUp.getRoomType().getName()) || roomDown != null && Constant.ELEVATOR.equals(roomDown.getRoomType().getName());
	}
	
	/**
	 * Merge two room if possible, and save in db
	 * @param idA The id of the first room
	 * @param idB The id of the second room
	 * @return the new merged room
	 */
	@Transactional
	public Room merge(Room keep, Room delete) {
		keep.setSize(keep.getSize() + delete.getSize());
		keep.getCells().addAll(delete.getCells());
		dao.save(keep);
		return keep;
	}
	
	@Transactional
	public Floor construct(int floorNumber, int cell, String typeName) {
		Floor floor = floorDao.findByNumber(floorNumber);
		RoomType type = roomTypeDao.findByName(typeName);
		
		Optional<Set<Integer>> canAddRoom = canAddRoom(type.getId(), floor.getId(), cell);
		canAddRoom.ifPresent(value -> {
			Room newRoom = new Room(type, value);
			
			Room right = dao.findOneByFloorAndCell(floor.getId(), cell + 1);
			Room left = dao.findOneByFloorAndCell(floor.getId(), cell - 1);
			if (isMergeable(newRoom, right)) {
				merge(right, newRoom);
			} else if (isMergeable(newRoom, left)) {
				merge(left, newRoom);
			} else {
				//if not mergeable, persist the new room
				floor.getRooms().add(newRoom);
				dao.save(newRoom);
			}
		});
		
		return floorDao.save(floor);
	}

	private boolean isMergeable(Room newRoom, Room oldRoom) {
		return newRoom != null && oldRoom != null 
				&& oldRoom.getRoomType().equals(newRoom.getRoomType())
				&& oldRoom.getLevel() == newRoom.getLevel()
				&& newRoom.getSize() + oldRoom.getSize() <= oldRoom.getRoomType().getMaxSize();
	}

	public Room find(long id) {
		return dao.findOne(id);
	}

	public List<RoomType> findAllType() {
		return roomTypeDao.findAll();
	}
	
	@Transactional
	public Room upgrade(long id){
		Room room = dao.findOne(id);
		room.setLevel(room.getLevel() + 1);
		dao.save(room);
		
		Optional<Integer> min = room.getCells().stream().min(Integer::min);
		Optional<Integer> max = room.getCells().stream().min(Integer::max);
		List<Room> findNeighbors = dao.findNeighbors(id, min.get() - 1, max.get() + 1);
		
		//stream of the room mergeables
		Optional<Room> mergeable = findNeighbors.stream()
				.filter(p -> isMergeable(room, p))
				.sorted((r1, r2) -> Integer.compare(r1.getSize(), r2.getSize())).findFirst();
		
		//TODO : merge if possible
		//mergeable.ifPresent(r -> {merge(room, r); dao.delete(r);});
		return room;
	}
	
}
