package com.example.utm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://ustatedarikmerkezi.com"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          configuration.setAllowedHeaders(List.of("*"));
          configuration.setAllowCredentials(true);
          return configuration;
        }))
        .authorizeHttpRequests(auth -> auth
            // Herkesin erişebileceği public endpoint'ler
            .requestMatchers(
                "/api/auth/**",
                "/api/ustalar",
                "/api/sorular/usta/**",
                "/api/hizmetler", // DİKKAT: Bu satır eklendi
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()
            // Sadece giriş yapmış kullanıcıların erişebileceği endpoint'ler
            .requestMatchers(
                "/api/me/**",
                "/api/requests/**"
            ).authenticated()
            // Sadece ADMIN rolüne sahip kullanıcıların erişebileceği endpoint'ler
            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
            // Geriye kalan tüm istekler için kimlik doğrulaması iste
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}