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
import fr.jchaline.shelter.domain.Special;
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
	public void list() throws Exception {
		Special special = new Special(Arrays.asList(1,2,3,2,1,3,1));
		when( service.list() ).thenReturn( Arrays.asList(new Dweller(true, "Hardy", "Tom", special)) );
		String contentExpected = "["
				+ "{id:null, male:true, name:Hardy, firstname:Tom, level:1, experience:0, items:null, weapon:null, room:null, special:{id:null, values:{P:2,I:1,C:2,E:3,A:3,L:1,S:1}}}"
				+ "]";
		
		this.mockMvc.perform(get("/dweller/list")).andExpect(status().isOk()).andExpect(content().json(contentExpected));
	}

	@After
	public void downUp() {
		this.mockMvc = null;
	}

}
