package fr.jchaline.shelter.service;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.jchaline.shelter.config.Constant;
import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.GameDao;
import fr.jchaline.shelter.dao.ItemDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Game;
import fr.jchaline.shelter.domain.Item;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;

/**
 * TODO : generate test data in dev mode only
 * @author jeremy
 *
 */
@Service
public class FactoryService {
	
	@Autowired
	private DwellerDao dwellerDao;
	
	@Autowired
	private ItemDao itemDao;

	@Autowired
	private WeaponDao weaponDao;

	@Autowired
	private SuitDao suitDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private FloorDao floorDao;
	
	private static final int NB_FLOOR = 4;
	
	public void initData() {
		initRoomType();
	}
	
	public Game generateGame(){
		Game g = new Game("101");
		Shelter s = new Shelter();
		g.setShelter(s);
		return gameDao.save(g);
	}

	public void generateData() {
		Game game = generateGame();
		generateItems();
		generateDwellers();
		generateFloor(game, NB_FLOOR);
		generateRoom();
	}
	
	private void generateFloor(Game game, int nbFloor) {
		for(int number=0; number<nbFloor; number++) {
			game.getShelter().setFloors(new HashMap<Integer, Floor>());
			game.getShelter().getFloors().put(number, floorDao.save(new Floor(number)));
			gameDao.save(game);
		};
	}
	
	private void initRoomType() {
		if(roomTypeDao.count() == 0){
			Stream.of(new SimpleEntry<>(Constant.ELEVATOR, 1),
					new SimpleEntry<>(Constant.FOOD, 2),
					new SimpleEntry<>(Constant.WATER, 2),
					new SimpleEntry<>(Constant.POWER, 2))
			.forEach(e -> roomTypeDao.save(new RoomType(e.getKey(), e.getValue())));
		}
	}
	
	private void generateRoom() {
		floorDao.findAll().stream().filter(f -> f.getNumber() == 1).findFirst().ifPresent(floor -> {
			floor.setRooms(new ArrayList<Room>());
			roomTypeDao.findAll().stream()
			.forEach(type -> {
				floor.getRooms().add(roomDao.save(new Room(type)));
			});
			floorDao.save(floor);
		});
	}
	
	public void generateItems() {
		if(itemDao.count() == 0){
			List<Item> items = new ArrayList<Item>();
			IntStream.rangeClosed(1, 1).forEach( n -> {
				items.addAll(Arrays.asList("gun" + n, "rifle" + n).stream()
					.map(String::toUpperCase)
					.map(s -> weaponDao.save(new Weapon(s, 20)))
					.collect(Collectors.toList()));//terminate operation
			});

			IntStream.rangeClosed(1, 1).forEach( n -> {
				items.addAll(Arrays.asList("plate" + n, "coat" + n).stream()
					.map(String::toUpperCase)
					.map(s -> suitDao.save(new Suit(s, 20)))
					.collect(Collectors.toList()));//terminate operation
			});
		}
	}
	
	public void generateDwellers() {
		if(dwellerDao.count() == 0){
			IntStream.rangeClosed(1, 1).forEach( n -> {
				List<Dweller> dwellers = Arrays.asList("john" + n, "jack" + n, "jim" + n).stream()
					.map(String::toUpperCase)//add operation to the stream
					.map(s -> dwellerDao.save(new Dweller(s)))//add operation
					.collect(Collectors.toList());//perform operation on stream and collect result as list (terminate operation)
				
				dwellers.stream()
					.findFirst().ifPresent(x -> {
						List<Item> items = itemDao.findAll();
						List<Weapon> weapons = weaponDao.findAll();
						weapons.stream().findFirst().ifPresent(x::setWeapon);
						x.setItems(items);
						dwellerDao.save(x);
					});
			});
		}
	}
}
