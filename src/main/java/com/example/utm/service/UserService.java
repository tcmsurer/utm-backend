package com.example.utm.service;

import com.example.utm.dto.ChangePasswordRequest;
import com.example.utm.dto.UserProfileDto;
import com.example.utm.model.AdminUser;
import com.example.utm.model.User;
import com.example.utm.repository.AdminUserRepository;
import com.example.utm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AdminUserRepository adminUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public Page<UserProfileDto> findAllUsers(Pageable pageable) {
    Page<User> usersPage = userRepository.findAll(pageable);
    return usersPage.map(user -> new UserProfileDto(
        user.getId(), user.getFullName(), user.getUsername(),
        user.getEmail(), user.getPhone(), user.getAddress(),
        user.isEmailVerified()
    ));
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  @Transactional(readOnly = true)
  public UserProfileDto findProfileByUsername(String username) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return new UserProfileDto(
          user.getId(), user.getFullName(), user.getUsername(),
          user.getEmail(), user.getPhone(), user.getAddress(),
          user.isEmailVerified()
      );
    }

    Optional<AdminUser> adminOptional = adminUserRepository.findByUsername(username);
    if (adminOptional.isPresent()) {
      AdminUser admin = adminOptional.get();
      return new UserProfileDto(
          admin.getId(), admin.getFullName(), admin.getUsername(),
          admin.getEmail(), admin.getPhone(), admin.getAddress(),
          true // Admin kullanıcıları her zaman doğrulanmış kabul edelim
      );
    }

    throw new UsernameNotFoundException("User not found with username: " + username);
  }

  @Transactional
  public UserProfileDto updateUserProfile(String username, UserProfileDto profileDto) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User userToUpdate = userOptional.get();
      userToUpdate.setFullName(profileDto.fullName());
      userToUpdate.setPhone(profileDto.phone());
      userToUpdate.setAddress(profileDto.address());
      User savedUser = userRepository.save(userToUpdate);
      return new UserProfileDto(savedUser.getId(), savedUser.getFullName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPhone(), savedUser.getAddress(), savedUser.isEmailVerified());
    }

    Optional<AdminUser> adminOptional = adminUserRepository.findByUsername(username);
    if (adminOptional.isPresent()) {
      AdminUser adminToUpdate = adminOptional.get();
      adminToUpdate.setFullName(profileDto.fullName());
      adminToUpdate.setPhone(profileDto.phone());
      adminToUpdate.setAddress(profileDto.address());
      AdminUser savedAdmin = adminUserRepository.save(adminToUpdate);
      return new UserProfileDto(savedAdmin.getId(), savedAdmin.getFullName(), savedAdmin.getUsername(), savedAdmin.getEmail(), savedAdmin.getPhone(), savedAdmin.getAddress(),  true); // Admin kullanıcıları her zaman doğrulanmış kabul edelim
    }

    throw new UsernameNotFoundException("User not found with username: " + username);
  }

  @Transactional
  public void changePassword(String username, ChangePasswordRequest request) {
    // Önce normal kullanıcı tablosunda ara
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
        throw new IllegalStateException("Mevcut şifre yanlış.");
      }
      user.setPassword(passwordEncoder.encode(request.newPassword()));
      userRepository.save(user);
      return; // İşlem bitti, çık
    }

    // Eğer normal kullanıcı değilse, admin tablosunda ara
    Optional<AdminUser> adminOptional = adminUserRepository.findByUsername(username);
    if (adminOptional.isPresent()) {
      AdminUser admin = adminOptional.get();
      if (!passwordEncoder.matches(request.oldPassword(), admin.getPassword())) {
        throw new IllegalStateException("Mevcut şifre yanlış.");
      }
      admin.setPassword(passwordEncoder.encode(request.newPassword()));
      adminUserRepository.save(admin);
      return; // İşlem bitti, çık
    }

    // Hiçbir yerde bulunamazsa
    throw new UsernameNotFoundException("User not found with username: " + username);
  }
}