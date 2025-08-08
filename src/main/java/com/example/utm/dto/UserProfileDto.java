package com.example.utm.dto;

import java.util.UUID;

public record UserProfileDto(
    UUID id,
    String fullName,
    String username,
    String email,
    String phone,
    String address,
    boolean emailVerified // YENİ ALAN EKLENDİ
) {}