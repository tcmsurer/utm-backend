package com.example.utm.controller;

import com.example.utm.dto.AuthRequest;
import com.example.utm.dto.AuthResponse;
import com.example.utm.dto.RegisterRequest;
import com.example.utm.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  // YENİ ENDPOINT: Şifremi unuttum talebi için
  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
    try {
      authService.initiatePasswordReset(payload.get("email"));
      return ResponseEntity.ok("Eger bu e-posta adresi sistemimizde kayitliysa, bir sifre sifirlama linki gonderilecektir.");
    } catch (Exception e) {
      return ResponseEntity.ok("Eger bu e-posta adresi sistemimizde kayitliysa, bir sifre sifirlama linki gonderilecektir.");
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
    try {
      authService.resetPassword(payload.get("token"), payload.get("password"));
      return ResponseEntity.ok("Sifreniz basariyla guncellendi.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}