package multi_tenant.db.navigation.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import multi_tenant.db.navigation.JWT.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // disable CSRF				
				.cors(Customizer.withDefaults()) // Enable CORS with default config
				.authorizeHttpRequests((requests) -> requests			
						.requestMatchers("/api/user/profile").hasAnyRole("GUEST", "ADMIN")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/owner/**").hasRole("OWNER")
						.requestMatchers("/api/tenant/**").hasAnyRole("STAFF_LEVEL_1", "STAFF_LEVEL_2","ADMIN","SUPERVISOR")											
						.anyRequest().permitAll()
				).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}
}
