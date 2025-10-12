package com.example.utm.dto;

import java.util.UUID;

public record RehberIcerikDto(
    UUID id,
    String title,
    String description,
    String mediaUrl,
    String mediaType
) {}