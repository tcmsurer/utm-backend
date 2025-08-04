package com.example.utm.config;

import com.example.utm.model.AdminUser;
import com.example.utm.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final AdminUserRepository adminUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // eger 'utm' adinda bir admin yoksa, olustur.
    if (adminUserRepository.findByUsername("utm").isEmpty()) {
      AdminUser admin = new AdminUser();
      admin.setUsername("utm");
      admin.setPassword(passwordEncoder.encode("1234")); // sifre: 1234
      adminUserRepository.save(admin);
      System.out.println(">>> Default admin user 'utm' created.");
    }
  }
}