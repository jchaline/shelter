package fr.jchaline.shelter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import fr.jchaline.shelter.service.FactoryService;

@SpringBootApplication
public class ShelterLauncher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShelterLauncher.class);

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(ShelterLauncher.class, args);
		
		FactoryService factory = ctx.getBean(FactoryService.class);
		
		factory.initData();
		factory.createData();
		
		LOGGER.info("Shelter Start successfull, wait for http layer ...");
	}
}
