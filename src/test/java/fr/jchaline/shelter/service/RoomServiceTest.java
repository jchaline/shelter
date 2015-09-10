package fr.jchaline.shelter.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.dao.RoomTypeDao;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.utils.SpecialEnum;

@RunWith( MockitoJUnitRunner.class )
public class RoomServiceTest {
	
	@Mock
	private RoomDao dao;

	@Mock
	private RoomTypeDao roomTypeDao;

	@InjectMocks
	private RoomService service = new RoomService();
	
	@Test
	public void canAddRoom() {
		long idType = 1l;
		RoomType type = new RoomType("power", 2, SpecialEnum.S);
		type.setId(idType);
		when( roomTypeDao.findByName(any(String.class))).thenReturn( type );
		service.canAddRoom(idType, 2, 3);
	}
	
	@Test
	public void merge(){
		RoomType type = new RoomType("power", 2, SpecialEnum.S);
		Room right = new Room(type, null);
		Room left = new Room(type, null);
		when( dao.getOne(1l) ).thenReturn( right );
		when( dao.getOne(2l) ).thenReturn( left );
		
		Room merge = service.merge(1l, 2l);
		assertEquals(4, merge.getSize());
	}
}
