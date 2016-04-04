package fr.jchaline.shelter.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.domain.Dweller;

@RunWith( MockitoJUnitRunner.class )
public class DwellerServiceTest {
	
	@Mock
	private DwellerDao dao;
	
	@InjectMocks
	private DwellerService service = new DwellerService();
	
	@Test
	public void paginate() {
		Dweller dweller = new Dweller();
		PageImpl<Dweller> expected = new PageImpl<>(Arrays.asList(dweller));
		
		when( dao.findAll(Matchers.any(PageRequest.class)) ).thenReturn( expected );
		
		Page<Dweller> paginate = service.paginate(1, 10);
		
		Assert.assertSame(expected, paginate);
	}

}
