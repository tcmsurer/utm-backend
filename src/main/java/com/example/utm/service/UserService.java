package com.example.utm.service;

import com.example.utm.dto.UserProfileDto; // Yeni bir DTO (aşağıda örneği var)
import com.example.utm.model.User;
import com.example.utm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * Admin tarafından tüm kullanıcıları listelemek için kullanılır.
   * @return Tüm kullanıcıların listesi.
   */
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Kullanıcı adıyla tek bir kullanıcıyı bulur.
   * @param username Aranacak kullanıcı adı.
   * @return Bulunan kullanıcı.
   */
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  /**
   * Bir kullanıcının profil bilgilerini güncellemek için kullanılır.
   * @param username Güncellenecek kullanıcının adı.
   * @param profileDto Yeni profil bilgileri.
   * @return Güncellenmiş kullanıcı.
   */
  public User updateUserProfile(String username, UserProfileDto profileDto) {
    User userToUpdate = findByUsername(username);
    userToUpdate.setPhone(profileDto.phone());
    userToUpdate.setAddress(profileDto.address());
    // Email gibi diğer alanlar da güncellenebilir.

    return userRepository.save(userToUpdate);
  }
}