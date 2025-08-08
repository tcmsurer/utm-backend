package com.example.utm.config;

import com.example.utm.repository.AdminUserRepository;
import com.example.utm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepository userRepository;
  private final AdminUserRepository adminUserRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return identifier -> {
      // Once normal kullanici tablosunda ara (hem username hem email)
      var user = userRepository.findByUsername(identifier)
          .or(() -> userRepository.findByEmail(identifier));
      if (user.isPresent()) {
        return new org.springframework.security.core.userdetails.User(
            user.get().getUsername(),
            user.get().getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
      }

      // Sonra admin tablosunda ara (hem username hem email)
      var adminUser = adminUserRepository.findByUsername(identifier)
          .or(() -> adminUserRepository.findByEmail(identifier));
      if (adminUser.isPresent()) {
        return new org.springframework.security.core.userdetails.User(
            adminUser.get().getUsername(),
            adminUser.get().getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
      }
      throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}