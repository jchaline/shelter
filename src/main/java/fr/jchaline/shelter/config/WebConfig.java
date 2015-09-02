package fr.jchaline.shelter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	
	/**
	 * Access allowed for territory controller list method
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/territory/list");
		registry.addMapping("/road/list");
	}
}
