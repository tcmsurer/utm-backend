package com.example.utm.dto;

// "fullName" alanı eklendi
public record RegisterRequest(
    String fullName,
    String username,
    String password,
    String email,
    String phone,
    String address
) {}