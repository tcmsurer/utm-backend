package com.example.utm.service;

import com.example.utm.model.AdminUser;
import com.example.utm.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

  private final AdminUserRepository adminUserRepository;

  /**
   * Kullanıcı adına göre bir admin kullanıcısı bulur.
   * @param username Aranacak adminin kullanıcı adı.
   * @return Bulunan AdminUser nesnesi.
   */
  public AdminUser findByUsername(String username) {
    return adminUserRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Admin user not found with username: " + username));
  }
}