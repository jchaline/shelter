package fr.jchaline.shelter.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.City;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.domain.Spot;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.enums.ResourceEnum;
import fr.jchaline.shelter.enums.SpecialEnum;
import fr.jchaline.shelter.utils.AlgoUtils;

/**
 * TODO : generate test data in dev mode only
 * @author jeremy
 *
 */
@Service
@Transactional
public class FactoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	@Autowired
	private FloorDao floorDao;
	
	@Autowired
	private WorldDao worldDao;
	
	@Autowired
	private SpecialService specialService;
	
	@Autowired
	private WeaponDao weaponDao;
	
	@Autowired
	private SuitDao suitDao;
	
	/**
	 * Initialize mandatory data
	 */
	public void initData() {
		LOGGER.info("Start init Data");
		initRoomType();
		initWorld();
		initItems();
		initBestiary();
		LOGGER.info("End init Data");
	}
	
	/**
	 * Initialisation des différentes "créatures" du jeu, humanoïdes ou non
	 */
	private void initBestiary() {
		LOGGER.info("init bestiary");
		//TODO : generate monsters and bad guys !
	}
	
	/**
	 * Initialisation des différents type de pièces constructibles
	 */
	private void initRoomType() {
		LOGGER.info("Start init Rooms Type");
		
		if(roomTypeDao.count() == 0){
			Stream.of(	new RoomType(ShelterConstants.ELEVATOR, null, 1, null, 150, 1),
						new RoomType(ShelterConstants.FOOD, ResourceEnum.FOOD, 2, SpecialEnum.A, 100, 6),
						new RoomType(ShelterConstants.WATER, ResourceEnum.WATER, 2, SpecialEnum.P, 100, 6),
						new RoomType(ShelterConstants.POWER, ResourceEnum.POWER, 2, SpecialEnum.S, 90, 6))
			.forEach(roomTypeDao::save);
		}
	}

	/**
	 * Initialization of the items : weapons & suits, with 3 levels of quality : normal, rare, epic
	 * Use suffix for quality
	 * Create legendary item ? specific name, special effect ?
	 */
	private void initItems() {
		//i18n file for weapon name, translate by client, only store i18n key
		 
		LOGGER.info("init weapons");
		Arrays.asList(new Weapon("knife", 4, 0), new Weapon("sword", 7, 0), new Weapon("katana", 10, 0), 
				new Weapon("gun", 3, 3), new Weapon("riffle", 7, 6), new Weapon("sniper", 10, 10), new Weapon("rocket_launcher", 15, 6))
		.stream()
		.forEach(it -> {
			weaponDao.save(it);
			weaponDao.save(new Weapon(it.getName() + "_rare", (int) Math.ceil(it.getDamage() * 3), it.getScope()));
			weaponDao.save(new Weapon(it.getName() + "_epic", (int) Math.ceil(it.getDamage() * 9), it.getScope()));
		});
		
		LOGGER.info("init suits");
		Arrays.asList(new Suit("suit", 1), new Suit("leather", 5), new Suit("kelvar", 10), new Suit("cortosis", 15))
		.stream()
		.forEach(it -> {
			suitDao.save(it);
			suitDao.save(new Suit(it.getName() + "_rare", (int) Math.ceil(it.getArmor() * 1.25)));
			suitDao.save(new Suit(it.getName() + "_epic", (int) Math.ceil(it.getArmor() * 1.5)));
		});
	}

	/**
	 * Initialisation du "Monde physique", création des villes et génération de bâtiments
	 */
	private void initWorld() {
		LOGGER.info("Start init World");
		
		World w = new World();
		
		Arrays.asList("Nantes", "Paris", "Metz", "Lyon", "Montpellier", "Barcelone", "Madrid", "Valence", "Seville", "Porto", "Lisonne", "Londre", "Manchester", "Glasgow", "Dublin", "Bruxelles", "Munich", "Berlin", "Moscou", "Pekin", "Tokyo", "Sydney", "New-York", "Los Angeles", "Washington", "Miami", "Seattle", "Rio", "Buenos Aires", "Pretoria", "Yaoundé", "Abuja", "Abidjan", "Dakar", "Rabat", "Alger", "Tunis", "Le Caire", "Bombay", "Seoul")
			.stream()
			.map(s -> new City(s))
			.forEach( c -> {
				//for each street
				Stream.iterate(1, n  ->  n  + 1).limit(10).forEach(idx -> {
					
					//each spot number 
					List<Integer> numbers = Stream.iterate(1, n  ->  n  + 1).limit(10).collect(Collectors.toList());
					
					c.add(new Spot("School"), idx, AlgoUtils.randPick(numbers));
					c.add(new Spot("Market"), idx, AlgoUtils.randPick(numbers));
					c.add(new Spot("Tower"), idx, AlgoUtils.randPick(numbers));
				});
				w.getCities().add(c);
			});
			
		worldDao.save(w);
	}

	/**
	 * Create data for a player
	 */
	public void createData() {
		LOGGER.info("create Data");
		
		//create a player with the player's shelter
		Player player = generatePlayerAndShelter();
		
		//add rooms (split in about 3~4 floors)
		createRooms(player);
		
		//add dwellers
		createDwellers(player);
	}

	/**
	 * Génération d'une population "type" pour l'abri
	 * @param player
	 */
	private void createDwellers(Player player) {
		Dweller simon = new Dweller(true, "Adebisi", "Simon", specialService.randForDweller(7));
		simon.setLevel(2);
		Dweller harley = new Dweller(false, "Quinn", "Harley", specialService.randForDweller(4));
		harley.setLevel(3);
		Arrays.asList( simon, harley,
				new Dweller(true, "Roronoa", "Zoro", specialService.randForDweller(3)),
				new Dweller(true, "Locke", "John", specialService.randForDweller(3)),
				new Dweller(true, "Troy", "Christian", specialService.randForDweller(3)),
				new Dweller(true, "Simpson", "Abraham", specialService.randForDweller(3)),
				new Dweller(true, "Lannister", "Tyrion", specialService.randForDweller(3)),
				new Dweller(true, "Stinson", "Barney", specialService.randForDweller(3)),
				new Dweller(true, "Phillip", "Fry", specialService.randForDweller(3)),
				new Dweller(true, "Bullock", "Harvey", specialService.randForDweller(3)),
				new Dweller(true, "Wilson", "James", specialService.randForDweller(3)),
				new Dweller(true, "Pope", "John", specialService.randForDweller(3)),
				new Dweller(false, "Nico", "Robin", specialService.randForDweller(3)),
				new Dweller(false, "McNamara", "Julia", specialService.randForDweller(3)),
				new Dweller(false, "Stark", "Arya", specialService.randForDweller(3)),
				new Dweller(false, "Kwon", "Sun", specialService.randForDweller(3)),
				new Dweller(false, "Scherbatsky", "Robin", specialService.randForDweller(3)),
				new Dweller(false, "Van Houten", "Luann", specialService.randForDweller(3)),
				new Dweller(false, "Kyle", "Selina", specialService.randForDweller(3)),
				new Dweller(false, "Turanga", "Leela", specialService.randForDweller(3)),
				new Dweller(false, "Luna", "Carmen", specialService.randForDweller(3)),
				new Dweller(false, "Dunham", "Olivia", specialService.randForDweller(3)),
				new Dweller(false, "Cudy", "Lisa", specialService.randForDweller(3)),
				new Dweller(true, "Pinkman", "Jesse", specialService.randForDweller(3)),
				new Dweller(false, "Ecureil", "Sandy", specialService.randForDweller(3)),
				new Dweller(true, "Rambo", "John", specialService.randForDweller(3))
				).stream()
			.forEach(player.getShelter().getDwellers()::add);
		LOGGER.info("Dweller generate over.");
	}

	/**
	 * Génération de pièces pour un joueur, afin de tester le jeu
	 * @param player Le joueur pour qui seront générés les pièces
	 */
	private void createRooms(Player player) {
		RoomType elevator = roomTypeDao.findByName(ShelterConstants.ELEVATOR);
		RoomType power = roomTypeDao.findByName(ShelterConstants.POWER);
		RoomType water = roomTypeDao.findByName(ShelterConstants.WATER);
		RoomType food = roomTypeDao.findByName(ShelterConstants.FOOD);

		int nbFloors = 4;
		Map<Integer, Floor> floors = player.getShelter().getFloors();
		for (int number = 0; number < nbFloors; number++) {
			Floor floor = new Floor(number, ShelterConstants.FLOOR_SIZE);
			floors.put(number, floorDao.save(floor));
			
			Arrays.asList(new Room(elevator, Stream.of(0).collect(Collectors.toSet())),
							new Room(power, Stream.of(1, 2).collect(Collectors.toSet())),
							new Room(water, Stream.of(3, 4).collect(Collectors.toSet())),
							new Room(food, Stream.of(5, 6).collect(Collectors.toSet())))
				.forEach(r -> {
					floor.getRooms().add(r);
					r.setFloor(floor);
					roomDao.save(r);
				});
			floorDao.save(floor);
		}
		playerDao.save(player);
	}

	/**
	 * Génération d'un joueur et de son abri 
	 * @return Le joueur généré
	 */
	private Player generatePlayerAndShelter() {
		Player p = new Player("user");
		Shelter s = new Shelter();
		p.setShelter(s);
		return playerDao.save(p);
	}
	
}
