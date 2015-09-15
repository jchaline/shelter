package fr.jchaline.shelter.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import fr.jchaline.shelter.domain.Room;
import fr.jchaline.shelter.domain.RoomType;
import fr.jchaline.shelter.service.RoomService;


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
public class RoomControllerTest {
	@Mock
    private RoomService service;
	
	@InjectMocks
	private RoomController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void upgrade() throws Exception {
		Room room = new Room(new RoomType("Water", 2, null, 100, 6), Stream.of(1, 2).collect(Collectors.toSet()));
		when( service.upgrade(1l)).thenReturn(room);
		
		String contentExpected = "{id:null, size:2, cells:[1, 2], level:1, roomType:{id:null, name:Water, size:2, cost:100, special:null, maxSize:6}}";
		this.mockMvc.perform(post("/room/upgrade/1")).andExpect(status().isOk())
		.andExpect(content().json(contentExpected));
	}
	
	@After
	public void downUp() {
		this.mockMvc = null;
	}

}
