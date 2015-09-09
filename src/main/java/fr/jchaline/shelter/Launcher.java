package fr.jchaline.shelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import fr.jchaline.shelter.service.FactoryService;

@SpringBootApplication
public class Launcher {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Launcher.class, args);
		
		FactoryService factory = ctx.getBean(FactoryService.class);
		
		factory.initData();
		factory.testGenerateData();
		
	}
}
