package com.security.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	// Create bean of SecurityFilterChain, it is responsible for managing all
	// filters

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // disable csrf
				.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
						authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
								.requestMatchers("/admin/**").hasAnyRole("ADMIN")
								.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
								.requestMatchers("/api/v1/auth/**").permitAll()
								.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
				http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build(); // Add exception in method signature
	}

}
