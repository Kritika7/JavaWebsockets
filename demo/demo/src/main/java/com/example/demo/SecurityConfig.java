package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private DataSource dataSource;

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	            .requestMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()
	            .anyRequest().authenticated()
	            .and()
	            .formLogin()
	            .loginPage("/login")
	            .defaultSuccessUrl("/chat")
	            .permitAll()
	            .and()
	            .logout()
	            .permitAll();
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
	    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
	    userDetailsManager.setDataSource(dataSource);
	    return userDetailsManager;
	}

}

