package com.example.utm.dto;

import com.example.utm.model.RequestStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ServiceRequestDto(
    UUID id,
    String title,
    String category,
    UserContactDto user,
    Map<String, String> details,
    String address, // YENİ EKLENDİ
    LocalDateTime createdDate,
    List<OfferDto> offers,
    RequestStatus status
) {}