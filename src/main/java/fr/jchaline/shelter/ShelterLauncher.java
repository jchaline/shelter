package fr.jchaline.shelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import fr.jchaline.shelter.service.FactoryService;

@SpringBootApplication
public class ShelterLauncher {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ShelterLauncher.class, args);
		
		FactoryService factory = ctx.getBean(FactoryService.class);
		
		factory.initData();
		factory.realCreateData();
	}
}
