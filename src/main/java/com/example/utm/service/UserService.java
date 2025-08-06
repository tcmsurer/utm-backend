package com.example.utm.service;

import com.example.utm.dto.UserProfileDto;
import com.example.utm.model.AdminUser;
import com.example.utm.model.User;
import com.example.utm.repository.AdminUserRepository;
import com.example.utm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AdminUserRepository adminUserRepository;

  @Transactional(readOnly = true)
  public Page<UserProfileDto> findAllUsers(Pageable pageable) {
    Page<User> usersPage = userRepository.findAll(pageable);
    // Page<User> objesini Page<UserProfileDto> objesine çeviriyoruz
    return usersPage.map(user -> new UserProfileDto(
        user.getId(),
        user.getFullName(),
        user.getUsername(),
        user.getEmail(),
        user.getPhone(),
        user.getAddress()
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
          user.getEmail(), user.getPhone(), user.getAddress()
      );
    }

    Optional<AdminUser> adminOptional = adminUserRepository.findByUsername(username);
    if (adminOptional.isPresent()) {
      AdminUser admin = adminOptional.get();
      return new UserProfileDto(
          admin.getId(), admin.getFullName(), admin.getUsername(),
          admin.getEmail(), admin.getPhone(), admin.getAddress()
      );
    }

    throw new UsernameNotFoundException("User not found with username: " + username);
  }

  @Transactional
  public UserProfileDto updateUserProfile(String username, UserProfileDto profileDto) {
    User userToUpdate = findByUsername(username);

    userToUpdate.setFullName(profileDto.fullName());
    userToUpdate.setPhone(profileDto.phone());
    userToUpdate.setAddress(profileDto.address());

    User savedUser = userRepository.save(userToUpdate);

    return new UserProfileDto(
        savedUser.getId(), savedUser.getFullName(), savedUser.getUsername(),
        savedUser.getEmail(), savedUser.getPhone(), savedUser.getAddress()
    );
  }
}