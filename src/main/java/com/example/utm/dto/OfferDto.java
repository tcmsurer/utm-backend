package com.example.utm.dto;

import java.time.LocalDateTime;
import java.util.UUID;

// Bu DTO, bir teklifin sadece frontend'e gönderilecek bilgilerini içerir.
public record OfferDto(
    UUID id,
    double price,
    String details,
    LocalDateTime createdDate
) {}