package com.example.utm.dto;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) {}