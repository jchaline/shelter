package fr.jchaline.shelter.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.FloorDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.domain.Floor;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
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

	@InjectMocks
	private RoomService service = new RoomService();
	
	@Test
	public void earnPerSecond() {
		RoomType type = new RoomType("power", ResourceEnum.POWER, 2, SpecialEnum.S, 0, 6);
		Room room2 = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		Room room4 = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		Room room6 = new Room(type, Stream.of(1, 2).collect(Collectors.toSet()));
		
		int earnR2 = RoomService.earnPerSecond(room2);
		int earnR4 = RoomService.earnPerSecond(room4);
		int earnR6 = RoomService.earnPerSecond(room6);
		
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
}
