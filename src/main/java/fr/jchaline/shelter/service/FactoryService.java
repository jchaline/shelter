package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.dao.BeastDao;
import fr.jchaline.shelter.dao.DutyDao;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.MapCellDao;
import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.domain.CellOccupant;
import fr.jchaline.shelter.domain.City;
import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.Player;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.domain.Spot;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.enums.CellEnum;
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
	
	//private static final List<String> CITIES_LIST = Arrays.asList("Nantes", "Paris", "Metz", "Lyon", "Montpellier", "Barcelone", "Madrid", "Valence", "Seville", "Porto", "Lisonne", "Londre", "Manchester", "Glasgow", "Dublin", "Bruxelles", "Munich", "Berlin", "Moscou", "Pekin", "Tokyo", "Sydney", "New-York", "Los Angeles", "Washington", "Miami", "Seattle", "Rio", "Buenos Aires", "Pretoria", "Yaoundé", "Abuja", "Abidjan", "Dakar", "Rabat", "Alger", "Tunis", "Le Caire", "Bombay", "Seoul");
	private static final List<String> CITIES_LIST_DEV = Arrays.asList("Nantes", "Barcelone", "Londre", "Munich", "Moscou", "Pekin", "Tokyo", "Sydney", "Buenos Aires", "Dakar");

	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	@Autowired
	private DutyDao dutyDao;
	
	@Autowired
	private FloorDao floorDao;
	
	@Autowired
	private WorldDao worldDao;
	
	@Autowired
	private SpecialService specialService;
	
	@Autowired
	private MapCellDao mapCellDao;

	@Autowired
	private MapService mapService;
	
	@Autowired
	private BeastDao beastDao;
	
	/**
	 * Initialize mandatory data
	 */
	public void initData() {
		LOGGER.info("Start init Data");
		initRoomType();
		initWorld();
		initItems();
		initBestiary();
		initDuty();
		LOGGER.info("End init Data");
	}
	
	/**
	 * Initialization of different types of duty
	 */
	private void initDuty() {
		Arrays
			.asList(new Duty(Duty.EXPLORE, "Exploration", true), new Duty(Duty.FIGHT, "Combat", true), new Duty(Duty.RECRUITMENT, "Recrutement", true), new Duty(Duty.RETURN, "Retour", false))
			.stream()
			.forEach(dutyDao::save);
	}

	/**
	 * Initialisation des différentes "créatures" du jeu, humanoïdes ou non
	 */
	private void initBestiary() {
		LOGGER.info("init bestiary");
		List<Beast> beasts = Arrays.asList(new Beast("walker", 5, 2, 10, 3), new Beast("runner", 5, 5, 20, 11), new Beast("tanker", 10, 1, 50, 50));
		
		beasts.forEach(b -> {
			beastDao.save(b);
		});
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
		//weapons model in ItemService
	}

	/**
	 * Create world map, randomize, with cities, water, and rock
	 */
	private World initWorld() {
		LOGGER.info("Start init World");
		
		World w = new World(WorldService.TERRE_1, CITIES_LIST_DEV.size() * 2, CITIES_LIST_DEV.size() * 2);
		
		//generate empty cells
		for (int x = 0; x < w.getWidth(); x++) {
			for (int y = 0; y < w.getHeight(); y++) {
				CellOccupant empty = new CellOccupant("empty", CellEnum.EMPTY);
				w.setCell(x, y, new MapCell(x + "_" + y, x, y));
				w.getCell(x, y).setOccupant(empty);
			}
		}
		
		List<Integer> xAxisPick = Stream.iterate(0, n  ->  n  + 1).limit(w.getWidth()).collect(Collectors.toList());
		List<Integer> yAxisPick = Stream.iterate(0, n  ->  n  + 1).limit(w.getHeight()).collect(Collectors.toList());

		//get list of each cells
		List<Pair<Integer, Integer>> listPos = new ArrayList<>();
		xAxisPick.forEach(x -> {
			yAxisPick.forEach(y -> {
				listPos.add(Pair.of(x, y));
			});
		});
		
		addCities(w, listPos, CITIES_LIST_DEV);
		addWaterAndRocks(w, listPos, 30, 10);
		
		//create edges between vertex
		w.setEdges(mapService.createEdges(w));
		
		return worldDao.save(w);
	}

	/**
	 * Add water and rock to the world
	 * @param world The world
	 * @param listPos The available positions
	 * @param percentWater The water percent
	 * @param percentRock The rock percent
	 */
	private void addWaterAndRocks(World world, List<Pair<Integer, Integer>> listPos, int percentWater, int percentRock) {
		for (int i = 0; i < world.getHeight() * world.getHeight() * percentRock / 100; i++) {
			Pair<Integer, Integer> pos = AlgoUtils.randPick(listPos);
			CellOccupant rock = new CellOccupant("rock" + i, CellEnum.ROCK);
			world.getCell(pos.getLeft(), pos.getRight()).setOccupant(rock);
		}

		for (int i = 0; i < world.getHeight() * world.getHeight() * percentWater / 100; i++) {
			Pair<Integer, Integer> pos = AlgoUtils.randPick(listPos);
			CellOccupant water = new CellOccupant("water" + i, CellEnum.WATER);
			world.getCell(pos.getLeft(), pos.getRight()).setOccupant(water);
		}
	}

	/**
	 * Add cities to the world
	 * @param world The world
	 * @param listPos The available positions
	 * @param cities The cities
	 */
	private void addCities(World world, List<Pair<Integer, Integer>> listPos, List<String> cities) {
		cities
			.stream()
			.map(s -> new City(s))
			.forEach( c -> {
				Arrays.asList("School", "Market", "Tower").forEach( s -> {
					c.addSpot(new Spot(s));
				});
				Pair<Integer, Integer> pos = AlgoUtils.randPick(listPos);
				world.getCell(pos.getLeft(), pos.getRight()).setOccupant(c);;
				world.getCities().add(c);
			});
	}

	/**
	 * Create data for a player
	 * @throws Exception 
	 */
	public void createData() {
		LOGGER.info("begin create Data");
		
		//create a player with the player's shelter
		Player player = generatePlayerAndShelter();
		
		//add rooms (split in about 3~4 floors)
		createRooms(player);
		
		//add dwellers
		createDwellers(player);
		LOGGER.info("end create Data");
	}

	/**
	 * Génération d'une population "type" pour l'abri
	 * @param player
	 * @throws Exception 
	 */
	private void createDwellers(Player player) {
		List<MapCell> cellCities = mapCellDao.findAll().stream().filter(c -> CellEnum.CITY.equals(c.getOccupant().getType())).collect(Collectors.toList());
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
			.forEach(d -> {
				d.setMapCell(AlgoUtils.rand(cellCities));
				player.getShelter().getDwellers().add(d);
			});
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
