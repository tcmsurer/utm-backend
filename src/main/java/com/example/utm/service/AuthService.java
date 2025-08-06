package com.example.utm.service;

import com.example.utm.dto.AuthRequest;
import com.example.utm.dto.AuthResponse;
import com.example.utm.dto.RegisterRequest;
import com.example.utm.model.User;
import com.example.utm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final MailService mailService;

  public AuthResponse register(RegisterRequest request) {
    var user = new User();
    user.setFullName(request.fullName()); // Gelen Ad Soyad bilgisi set ediliyor
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPhone(request.phone());
    user.setAddress(request.address());
    user.setPassword(passwordEncoder.encode(request.password()));

    userRepository.save(user);

    var userDetails = userDetailsService.loadUserByUsername(request.username());

    var jwtToken = jwtService.generateToken(userDetails);
    return new AuthResponse(jwtToken);
  }

  public AuthResponse login(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        )
    );

    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

    final String jwtToken = jwtService.generateToken(userDetails);
    return new AuthResponse(jwtToken);
  }

  @Transactional
  public void initiatePasswordReset(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Bu e-posta adresi ile kayitli kullanici bulunamadi."));

    String token = UUID.randomUUID().toString();
    user.setPasswordResetToken(token);
    user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token 1 saat geçerli
    userRepository.save(user);

    mailService.sendPasswordResetEmail(user.getEmail(), token);
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    User user = userRepository.findByPasswordResetToken(token)
        .orElseThrow(() -> new RuntimeException("Gecersiz veya suresi dolmus sifre sifirlama linki."));

    if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Sifre sifirlama linkinin suresi dolmus.");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setPasswordResetToken(null);
    user.setPasswordResetTokenExpiry(null);
    userRepository.save(user);
  }
}