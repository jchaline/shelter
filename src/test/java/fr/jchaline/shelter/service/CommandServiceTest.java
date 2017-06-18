package fr.jchaline.shelter.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.dao.PlayerDao;
import fr.jchaline.shelter.domain.Message;

@RunWith( MockitoJUnitRunner.class )
public class CommandServiceTest {
	
	@Mock
	private PlayerDao playerDao;
	
	@Mock
	private DwellerDao dwellerDao;
	
	@InjectMocks
	private CommandService service = new CommandService();
	
	@Test
	public void fuse() {
		List<Message> list = Arrays.asList(new Message("toto1"), new Message("toto2"), new Message("toto3"), new Message("toto4"), new Message("toto5"), new Message("toto6"));
		
		List<Message> fuse2 = service.merge(list, 2);
		List<Message> fuse3 = service.merge(list, 3);
		
		Assert.assertEquals("toto1 | toto2", fuse2.get(0).getContent());
		Assert.assertEquals("toto1 | toto2 | toto3", fuse3.get(0).getContent());
	}

}
