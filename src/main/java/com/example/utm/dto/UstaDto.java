package com.example.utm.dto;

import java.util.UUID;

public record UstaDto(
    UUID id,
    String name,
    String profileImageUrl
) {}