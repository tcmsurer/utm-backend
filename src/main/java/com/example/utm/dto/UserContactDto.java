package com.example.utm.dto;

public record UserContactDto(
    String fullName,
    String email,
    String phone,
    String username
) {}