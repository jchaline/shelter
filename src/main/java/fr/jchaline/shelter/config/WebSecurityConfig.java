package fr.jchaline.shelter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//    private AuthenticationProvider authenticationProvider;

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
		//auth.authenticationProvider(authenticationProvider);
    }

	@Configuration
	@Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{

		@Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                    .anyRequest().hasAnyAuthority("ROLE_USER")
                    .and()
                .httpBasic();
        }
    }
}
