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
import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.GameDao;
import fr.jchaline.shelter.dao.ItemDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.Spot;
import fr.jchaline.shelter.domain.City;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Game;
import fr.jchaline.shelter.domain.Item;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.domain.Shelter;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.enums.ResourceEnum;
import fr.jchaline.shelter.enums.SpecialEnum;

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
	
	@Autowired
	private WorldDao worldDao;
	
	@Autowired
	private DwellerService dwellerService;
	
	@Autowired
	private SpecialService specialService;
	
	
	private static final int NB_FLOOR = 4;
	
	/**
	 * Initialize mandatory data
	 */
	public void initData() {
		initRoomType();
		initWorld();
	}
	
	private void initWorld() {
		City nantes = new City("Nantes");
		nantes.add(new Spot("School"), 5, 5);
		nantes.add(new Spot("Market"), 3, 4);
		nantes.add(new Spot("Tower"), 3, 4);
		
		World w = new World();
		w.getCities().add(nantes);
		worldDao.save(w);
	}

	/**
	 * Create data for a real game
	 */
	public void realCreateData() {
		//create a game with the player's shelter
		Game game = generateGame();
		
		World world = worldDao.findAll().get(0);
		world.getCities().stream().findFirst().ifPresent(c -> {
			//c.add(game.getShelter(), 1, 1);
		});
		
		//add rooms (split in about 3~4 floors)
		realCreateRooms(game);
		
		//add dwellers
		realCreateDwellers(game);
	}

	private void realCreateDwellers(Game game) {
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
			.forEach(game.getShelter().getDwellers()::add);
		LOGGER.info("Dweller generate over.");
	}

	private void realCreateRooms(Game game) {
		RoomType elevator = roomTypeDao.findByName(ShelterConstants.ELEVATOR);
		RoomType power = roomTypeDao.findByName(ShelterConstants.POWER);
		RoomType water = roomTypeDao.findByName(ShelterConstants.WATER);
		RoomType food = roomTypeDao.findByName(ShelterConstants.FOOD);

		int nbFloors = 4;
		Map<Integer, Floor> floors = game.getShelter().getFloors();
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
		gameDao.save(game);
	}

	/**
	 * Create huge data for test
	 */
	public void testGenerateData() {
		LOGGER.debug("start generate data");
		Game game = generateGame();
		testGenerateItems(game);
		testGenerateDwellers(game);
		testGenerateFloor(game, NB_FLOOR);
		testGenerateRoom(game);
		LOGGER.debug("generate data over");
	}
	
	public Game generateGame(){
		Game g = new Game("101");
		Shelter s = new Shelter();
		g.setShelter(s);
		return gameDao.save(g);
	}

	private void testGenerateFloor(Game game, int nbFloor) {
		for (int number=0; number<nbFloor; number++) {
			game.getShelter().getFloors().put(number, floorDao.save(new Floor(number, ShelterConstants.FLOOR_SIZE)));
		};
		gameDao.save(game);
	}
	
	private void initRoomType() {
		if(roomTypeDao.count() == 0){
			Stream.of(	new RoomType(ShelterConstants.ELEVATOR, null, 1, null, 150, 1),
						new RoomType(ShelterConstants.FOOD, ResourceEnum.FOOD, 2, SpecialEnum.A, 100, 6),
						new RoomType(ShelterConstants.WATER, ResourceEnum.WATER, 2, SpecialEnum.P, 100, 6),
						new RoomType(ShelterConstants.POWER, ResourceEnum.POWER, 2, SpecialEnum.S, 90, 6))
			.forEach(roomTypeDao::save);
		}
	}
	
	private void testGenerateRoom(Game game) {
		List<RoomType> allType = roomTypeDao.findAll();
		game.getShelter().getFloors().entrySet().stream()
		.forEach(entry -> {
			Floor floor = entry.getValue();
			int i = 0;
			for(RoomType type : allType) {
				Room entity = new Room(type, Stream.of(i+=2).collect(Collectors.toSet()));
				floor.getRooms().add(entity);
				roomDao.save(entity);
			}
			floorDao.save(floor);
		});
	}
	
	public void testGenerateItems(Game game) {
		if(itemDao.count() == 0){
			
			Arrays.asList("gun", "rifle").stream().forEach(w -> {
				Stream.iterate(1L, n  ->  n  + 1).limit(25)
						.map(s -> weaponDao.save(new Weapon(w + s, 20)))
						.collect(Collectors.toList());
			});

			Arrays.asList("plate", "coat").stream().forEach(w -> {
				Stream.iterate(1L, n  ->  n  + 1).limit(25)
						.map(s -> suitDao.save(new Suit(w + s, 20)))
						.collect(Collectors.toList());
			});
		}
	}
	
	public void testGenerateDwellers(Game game) {
		if(dwellerDao.count() == 0) {
			List<Dweller> dwellers = Stream.iterate(1L, n  ->  n  + 1).limit(25)
				.map(s -> dwellerDao.save(dwellerService.generate()))//add operation
				.collect(Collectors.toList());//perform operation on stream and collect result as list (terminate operation)
			
			dwellers.stream().findFirst()
			.ifPresent(x -> {
				List<Item> items = itemDao.findAll();
				List<Weapon> weapons = weaponDao.findAll();
				weapons.stream().findFirst().ifPresent(x::setWeapon);
				x.setItems(items);
				dwellerDao.save(x);
			});
			game.getShelter().setDwellers(dwellers);
			gameDao.save(game);
		}
	}
}
