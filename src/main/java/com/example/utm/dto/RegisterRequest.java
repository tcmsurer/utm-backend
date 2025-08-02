package com.example.utm.dto;

public record RegisterRequest(String username, String password, String email, String phone, String address) {
}