package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import fr.jchaline.shelter.dao.DwellerDao;
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

	private static final int WATER_NEIGHBORS_PERCENT = 20;
	private static final int WATER_PERCENT = 5;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryService.class);
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private PlayerDao playerDao;

	@Autowired
	private DwellerDao dwellerDao;
	
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
			.asList(new Duty(Duty.EXPLORE, "Exploration", true), new Duty(Duty.FIGHT, "Combat", false), new Duty(Duty.RECRUITMENT, "Recrutement", false), new Duty(Duty.RETURN, "Retour", false))
			.stream()
			.forEach(dutyDao::save);
	}

	/**
	 * Initialisation des différentes "créatures" du jeu, humanoïdes ou non
	 */
	private void initBestiary() {
		LOGGER.info("init bestiary");
		List<Beast> beasts = Arrays.asList(new Beast("walker hurt", 5, 2, 1), new Beast("walker", 5, 2, 3), new Beast("walker armored", 5, 2, 5), new Beast("runner hurt", 5, 5, 8), new Beast("runner", 5, 5, 11), new Beast("tanker", 10, 1, 50));
		
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
		
		World w = new World(WorldService.TERRE_1, CITIES_LIST_DEV.size() * 6, CITIES_LIST_DEV.size() * 6);
		
		//generate empty cells
		for (int x = 0; x < w.getWidth(); x++) {
			for (int y = 0; y < w.getHeight(); y++) {
				CellOccupant empty = new CellOccupant("empty", CellEnum.EMPTY);
				w.setCell(x, y, new MapCell(x + "_" + y, x, y));
				w.getCell(x, y).setOccupant(empty);
			}
		}
		

		//get list of each cells
		//random city, water & rock positions
		//randomizeMapOccupants(w);
		
		computeMapOccupants(w);
		
		//create edges between vertex
		w.setEdges(mapService.createEdges(w));
		
		return worldDao.save(w);
	}
	
	/**
	 * Compute cell for the neighbor x,y
	 * @param world
	 * @param x
	 * @param y
	 */
	private void computeForNeighbor(World world, CellEnum type, int neighboursPercent, int x, int y) {
		if (x < world.getWidth() && x >= 0 && y < world.getHeight() && y >= 0 && new Random().nextInt(100) < neighboursPercent) {
			CellOccupant water = new CellOccupant(type.toString(), type);
			world.getCell(x, y).setOccupant(water);
			computeForNeighbors(world, type, neighboursPercent, x, y);
		}
	}
	
	/**
	 * Compute cells for the neighbors of x,y
	 * @param world
	 * @param x
	 * @param y
	 */
	private void computeForNeighbors(World world, CellEnum type, int neighboursPerent, int x, int y) {
		computeForNeighbor(world, type, neighboursPerent, x, y + 1);
		computeForNeighbor(world, type, neighboursPerent, x, y - 1);
		computeForNeighbor(world, type, neighboursPerent, x + 1, y);
		computeForNeighbor(world, type, neighboursPerent, x - 1, y);
	}

	private void computeMapOccupants(World world) {
		//first, create water
		createGroupCell(world, CellEnum.WATER, WATER_PERCENT, WATER_NEIGHBORS_PERCENT);
		createGroupCell(world, CellEnum.ROCK, 2, 10);
		
		//then, create rock
		
		//then, create city
		List<Pair<Integer, Integer>> listPositions = new ArrayList<>();
		
		List<Integer> xAxisPick = Stream.iterate(0, n  ->  n  + 1).limit(world.getWidth()).collect(Collectors.toList());
		List<Integer> yAxisPick = Stream.iterate(0, n  ->  n  + 1).limit(world.getHeight()).collect(Collectors.toList());
		
		xAxisPick.forEach(x -> {
			yAxisPick.forEach(y -> {
				listPositions.add(Pair.of(x, y));
			});
		});
		addCities(world, listPositions, CITIES_LIST_DEV);
	}

	private void createGroupCell(World world, CellEnum type, int percent, int neighboursPercent) {
		for (int y = 0; y < world.getHeight(); y++ ) {
			for (int x = 0; x < world.getWidth(); x++ ) {
				if (new Random().nextInt(100) < percent) {
					CellOccupant cell = new CellOccupant(type.toString(), type);
					world.getCell(x, y).setOccupant(cell);
					computeForNeighbors(world, type, neighboursPercent, x, y);
				}
			}
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
				world.getCell(pos.getLeft(), pos.getRight()).setOccupant(c);
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
				d.setPlayer(player);
				dwellerDao.save(d);
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
