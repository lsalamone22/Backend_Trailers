package com.sistema.trailers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean//esto es importante para que el metodo se añada el USERbEAN conteiner de spring para que despues lo podamos usar
	protected UserDetailsService userDetailsService() {//vamos a guardar nuestros usuarios en memoria y colocamos el password para que se registre
		UserDetails usuario1 = User
				.withUsername("loreley")
				.password("$2a$10$nbEeWk104eSdRikNGnK2BuAhvssJV/aBW6tQl0GY5qeia9ciNHnn.")
				.roles("USER")
				.build();
		
		UserDetails usuario2 = User
				.withUsername("admin")
				.password("$2a$10$nbEeWk104eSdRikNGnK2BuAhvssJV/aBW6tQl0GY5qeia9ciNHnn.")
				.roles("ADMIN")	
				.build();
		
		return new InMemoryUserDetailsManager(usuario1,usuario2);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()//cuando cualquier usuario entre aqui, lo puede hacer cualquiera
		
		.antMatchers("/peliculas/{id}/editar", "/peliculas/{id}/eliminar").hasRole("ADMIN")//solos los de admin
		.anyRequest().authenticated()
		.and()
		.formLogin()
				.loginPage("/login")//que todos entren al login
				.permitAll()
		.and()
		.logout().permitAll();//esten permitidos todos los usuarios
			
		    
	}
}
