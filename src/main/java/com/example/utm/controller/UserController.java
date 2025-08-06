package com.example.utm.controller;

import com.example.utm.dto.UserProfileDto;
import com.example.utm.model.User;
import com.example.utm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<UserProfileDto> getCurrentUserProfile(Principal principal) {
    UserProfileDto userProfile = userService.findProfileByUsername(principal.getName());
    return ResponseEntity.ok(userProfile);
  }

  @PutMapping("/me")
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<UserProfileDto> updateCurrentUserProfile(Principal principal, @RequestBody UserProfileDto profileDto) {
    UserProfileDto updatedProfile = userService.updateUserProfile(principal.getName(), profileDto);
    return ResponseEntity.ok(updatedProfile);
  }

  @GetMapping("/admin/users")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
    return ResponseEntity.ok(userService.findAllUsers(pageable));
  }
}