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

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService; // YENİ EKLENDİ

  public AuthResponse register(RegisterRequest request) {
    var user = new User();
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
    // 1. Adım: Kimlik doğrulama (Bu kısım hem admin hem normal kullanıcı için çalışır)
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        )
    );

    // 2. Adım: Token üretme (DÜZELTİLMİŞ KISIM)
    // Kullanıcıyı bulmak için artık sadece UserRepository'ye değil,
    // hem admin hem de normal kullanıcıyı arayan UserDetailsService'e soruyoruz.
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

    final String jwtToken = jwtService.generateToken(userDetails);
    return new AuthResponse(jwtToken);
  }
}