package fr.jchaline.shelter.service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.enums.SpecialEnum;

/**
 * @author jeremy
 *
 */
@Service
@Transactional(readOnly = true)
public class RoomService {
	
	private static final int LAMBDA_AMOUNT_RESOURCES_EARN = 100;
	private static final double COEFF_ROOM_SIZE = 2.2;
	private static final double COEFF_ROOM_LEVEL = 1.3;

	@Autowired
	private FloorDao floorDao;

	@Autowired
	private DwellerDao dwellerDao;

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
			if (roomTypeDao.findByName(ShelterConstants.ELEVATOR).getId().equals(idType)) {
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
		return roomUp != null && ShelterConstants.ELEVATOR.equals(roomUp.getRoomType().getName()) || roomDown != null && ShelterConstants.ELEVATOR.equals(roomDown.getRoomType().getName());
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
			newRoom.setFloor(floor);
			
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
		
		OptionalInt min = room.getCells().stream().mapToInt(i -> i).min();
		OptionalInt max = room.getCells().stream().mapToInt(i -> i).max();
		List<Room> findNeighbors = dao.findNeighbors(id, min.getAsInt() - 1, max.getAsInt() + 1);
		
		//stream of the room mergeables
		Optional<Room> mergeable = findNeighbors.stream()
				.filter(p -> isMergeable(room, p))
				.sorted((r1, r2) -> Integer.compare(r1.getSize(), r2.getSize())).findFirst();
		
		mergeable.ifPresent(r -> {merge(room, r);r.getFloor().getRooms().remove(r); dao.delete(r);});
		return room;
	}
	
	@Transactional
	public Room assign(long id, long idDweller) {
		Room room = dao.findOne(id);
		Dweller dweller = dwellerDao.findOne(idDweller);
		
		//if room full, exchange one dweller with current
		if( room.getDwellers().size() >= room.getSize() * 2 ) {
			
			//find the dweller with lowest SPECIAL link with room type
			SpecialEnum special = room.getRoomType().getSpecial();
			room.getDwellers().stream()
				.sorted((d1, d2) -> {
					return Integer.compare(d1.getSpecial().getValue(special), d2.getSpecial().getValue(special));
				})
				.findFirst()
				.ifPresent(lowest -> {
					lowest.getRoom().getDwellers().remove(lowest);
					lowest.setRoom(dweller.getRoom());
					if (lowest.getRoom() != null) {
						lowest.getRoom().getDwellers().add(lowest);
					}
			});
		}
		
		dweller.setRoom(room);
		room.getDwellers().add(dweller);
		
		return room;
	}

	/**
	 * Compute the amount of resources earn per second
	 * @param room
	 * @return
	 */
	public int earnPerSecond(Room room) {
		int earn = 0;
		if (!room.getDwellers().isEmpty()) {
			SpecialEnum specialForRoom = room.getRoomType().getSpecial();
			int specialsOfDwellers = room.getDwellers()
					.parallelStream()
					.mapToInt(d -> d.getSpecial().getValue(specialForRoom))
					.sum();
			
			//todo : improve this, it must be better to have 2 dwellers with 5 than one with 10
			double coeff = specialsOfDwellers * 1.0 / (room.getSize() * ShelterConstants.SPECIAL_MAX);
			
			earn = (int) Math.floor(Math.pow(COEFF_ROOM_LEVEL, room.getLevel()) * Math.pow(COEFF_ROOM_SIZE, room.getSize()) * LAMBDA_AMOUNT_RESOURCES_EARN * coeff);
		}
		return earn;
	}
}
