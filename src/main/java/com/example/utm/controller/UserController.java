package com.example.utm.controller;

import com.example.utm.dto.UserProfileDto;
import com.example.utm.model.User;
import com.example.utm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // --- Kullanıcının Kendi İşlemleri (Giriş Yapmış Olmalı) ---

  @GetMapping("/me")
  public ResponseEntity<User> getCurrentUserProfile(Principal principal) {
    // Principal.getName() bize JWT'den gelen kullanıcı adını (username) verir.
    User user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(user);
  }

  @PutMapping("/me")
  public ResponseEntity<User> updateCurrentUserProfile(Principal principal, @RequestBody UserProfileDto profileDto) {
    User updatedUser = userService.updateUserProfile(principal.getName(), profileDto);
    return ResponseEntity.ok(updatedUser);
  }

  // --- Admin İşlemleri ---

  @GetMapping("/admin/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.findAllUsers());
  }
}