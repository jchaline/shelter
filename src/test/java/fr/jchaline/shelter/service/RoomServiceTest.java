package fr.jchaline.shelter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.utils.SpecialEnum;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith( MockitoJUnitRunner.class )
public class RoomServiceTest {
	
	@Mock
	private RoomDao dao;

	@InjectMocks
	private RoomService service = new RoomService();
	
	@Test
	public void canAddRoom() {
		service.canAddRoom(1l, 2, 3);
	}
	
	@Test
	public void merge(){
		RoomType type = new RoomType("power", 2, SpecialEnum.S);
		Room right = new Room(type);
		Room left = new Room(type);
		when( dao.getOne(1l) ).thenReturn( right );
		when( dao.getOne(2l) ).thenReturn( left );
		
		Room merge = service.merge(1l, 2l);
		assertEquals(4, merge.getSize());
	}
}
