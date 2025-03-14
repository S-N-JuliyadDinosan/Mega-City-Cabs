package com.dino.Mega_City_Cabs.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // Add this
import org.springframework.web.cors.CorsConfigurationSource; // Add this
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Add this
import java.util.Arrays; // Add this

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(customizer -> customizer.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add CORS
				.authorizeHttpRequests(request -> {
					System.out.println("Configuring security rules");
					request.requestMatchers("/api/v1/users/login", "/api/v1/customer/register").permitAll()
							.requestMatchers("/api/v1/users/logout").authenticated()
							.requestMatchers("/api/v1/admins/**", "/api/v1/drivers/**", "/api/v1/cars/**").hasRole("ADMIN")
							.requestMatchers("/api/v1/customer").hasRole("ADMIN")
							.requestMatchers(HttpMethod.PUT, "/api/v1/customer/{id}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
							.requestMatchers(HttpMethod.DELETE, "/api/v1/customer/{id}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/v1/customer/{id}").hasAnyAuthority("ROLE_ADMIN")
							.requestMatchers("/api/v1/booking/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_DRIVER")
							.requestMatchers("/api/v1/pricing/**").hasRole("ADMIN")
							.requestMatchers(HttpMethod.POST, "/api/v1/help").hasRole("ADMIN")
							.requestMatchers(HttpMethod.PUT, "/api/v1/help/**").hasRole("ADMIN")
							.requestMatchers(HttpMethod.DELETE, "/api/v1/help/**").hasRole("ADMIN")
							.requestMatchers(HttpMethod.GET, "/api/v1/help/**").permitAll()
							.requestMatchers("/api/v1/logs/**").hasRole("ADMIN")
							.requestMatchers("/ws/**").permitAll()
							.anyRequest().authenticated();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		System.out.println("SecurityFilterChain built");
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Vite frontend
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true); // For JWT cookies if needed
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
}