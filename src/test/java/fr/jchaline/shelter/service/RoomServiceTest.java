package fr.jchaline.shelter.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.domain.Special;
import fr.jchaline.shelter.enums.ResourceEnum;
import fr.jchaline.shelter.enums.SpecialEnum;

@RunWith( MockitoJUnitRunner.class )
public class RoomServiceTest {
	
	@Mock
	private RoomDao dao;

	@Mock
	private FloorDao floorDao;

	@Mock
	private RoomTypeDao roomTypeDao;

	@Mock
	private DwellerDao dwellerDao;

	@InjectMocks
	private RoomService service = new RoomService();
	
	@Test
	public void earnPerSecond() {
		List<Dweller> dwellers = Arrays.asList(new Dweller(true, "John", "David", new Special(Arrays.asList(1, 2, 3, 1, 2, 3, 1))));
		RoomType type = new RoomType("power", ResourceEnum.POWER, 2, SpecialEnum.S, 0, 6);
		Room room2 = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		Room room4 = new Room(type, Stream.of(1, 2, 3, 4).collect(Collectors.toSet()));
		Room room6 = new Room(type, Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.toSet()));
		
		room2.setDwellers(dwellers);
		room4.setDwellers(dwellers);
		room6.setDwellers(dwellers);
		
		int earnR2 = service.earnPerSecond(room2);
		int earnR4 = service.earnPerSecond(room4);
		int earnR6 = service.earnPerSecond(room6);
		
		Assert.assertTrue(earnR2*2 < earnR4);
		Assert.assertTrue(earnR2*3 < earnR6);
		Assert.assertTrue(earnR4*6 < earnR6*4);
	}
	
	@Test
	public void canAddRoom() {
		long idType = 1l;
		long idFloor = 2l;
		RoomType type = new RoomType("power", ResourceEnum.POWER, 2, SpecialEnum.S, 0, 6);
		type.setId(idType);
		when( roomTypeDao.findByName(any(String.class))).thenReturn( type );
		
		when( roomTypeDao.findOne(idType) ).thenReturn(type);
		when( dao.findOneByFloorAndCell(idFloor, 3)).thenReturn(null);
		when( dao.findOneByFloorAndCell(idFloor, 1)).thenReturn(null);
		
		Floor floor = new Floor(2, 18);
		floor.setId(idFloor);
		
		when( floorDao.findOne(idFloor) ).thenReturn(floor);
		when( floorDao.findByNumber(3)).thenReturn(null);
		when( floorDao.findByNumber(1)).thenReturn(null);
		
		Optional<Set<Integer>> canAddRoom = service.canAddRoom(idType, idFloor, 3);
		Assert.assertFalse(canAddRoom.isPresent());
	}
	
	@Test
	public void merge() {
		RoomType type = new RoomType("power", ResourceEnum.POWER, 2, SpecialEnum.S, 0, 6);
		Room right = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		Room left = new Room(type, Stream.of(3, 4).collect(Collectors.toSet()));
		when( dao.save(left) ).thenReturn( left );
		
		Room merge = service.merge(left, right);
		Assert.assertEquals(4, merge.getSize());
	}
	
	@Test
	public void assign() {
		
		RoomType type = new RoomType("power", ResourceEnum.POWER, 2, SpecialEnum.S, 0, 6);
		
		Dweller dweller = new Dweller();
		Room room = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		
		when( dwellerDao.findOne(Matchers.any(Long.class)) ).thenReturn( dweller );
		when( dao.findOne(Matchers.any(Long.class)) ).thenReturn( room );
		
		service.assign(2l,  1l);
	}
}
