package fr.jchaline.shelter.service;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.RoomDao;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Room;

@RunWith( MockitoJUnitRunner.class )
public class DwellerServiceTest {
	
	@Mock
	private DwellerDao dao;

	@Mock
	private RoomDao roomDao;
	
	@InjectMocks
	private DwellerService service = new DwellerService();
	
	@Test
	public void assign(){
		Dweller dweller = new Dweller();
		Room room = new Room();
		
		when( dao.getOne(1l) ).thenReturn( dweller );
		when( roomDao.getOne(2l) ).thenReturn( room );
		
		service.assign(1l,  2l);
	}

}
