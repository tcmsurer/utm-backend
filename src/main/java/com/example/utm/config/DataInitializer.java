package com.example.utm.config;

import com.example.utm.model.AdminUser;
import com.example.utm.repository.AdminUserRepository;
// import lombok.RequiredArgsConstructor; // Bu import'u siliyoruz
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
// @RequiredArgsConstructor // Bu anotasyonu siliyoruz
public class DataInitializer implements CommandLineRunner {

  private final AdminUserRepository adminUserRepository;
  private final PasswordEncoder passwordEncoder;

  // Sadece manuel constructor kalıyor
  public DataInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
    this.adminUserRepository = adminUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    // eger 'utm' adinda bir admin yoksa, olustur.
    if (adminUserRepository.findByUsername("utm").isEmpty()) {
      AdminUser admin = new AdminUser();
      admin.setUsername("utm");
      admin.setPassword(passwordEncoder.encode("1234"));
      admin.setFullName("Admin User");
      admin.setEmail("admin@ustatedarikmerkezi.com");
      admin.setPhone("05000000000");
      admin.setAddress("Merkez Ofis");

      adminUserRepository.save(admin);
      System.out.println(">>> Default admin user 'utm' created.");
    }
  }
}