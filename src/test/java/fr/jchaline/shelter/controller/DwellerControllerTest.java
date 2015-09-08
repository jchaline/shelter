package fr.jchaline.shelter.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.jchaline.shelter.config.WebConfig;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.service.DwellerService;


/**
 * Unit testing for controller layer
 * 
 * @see http://stackoverflow.com/questions/16170572/unable-to-mock-service-class-in-spring-mvc-controller-tests
 * @see  http://blog.trifork.com/2012/12/11/properly-testing-spring-mvc-controllers/
 * @see integration test : http://www.jayway.com/2014/07/04/integration-testing-a-spring-boot-application/
 * @author jChaline
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class DwellerControllerTest {

	@Mock
    private DwellerService service;
	
	@InjectMocks
	private DwellerController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testSampleController() throws Exception {
		when( service.list() ).thenReturn( Arrays.asList(new Dweller()) );
		String contentExpected = "[{\"id\":null,\"male\":null,\"name\":null,\"firstname\":null,\"level\":0,\"experience\":0,\"items\":null,\"weapon\":null,\"room\":null,\"special\":null}]";
		
		this.mockMvc.perform(get("/dweller/list")).andExpect(status().isOk())
		.andExpect(content().string(contentExpected));
	}

	@After
	public void downUp() {
		this.mockMvc = null;
	}

}
