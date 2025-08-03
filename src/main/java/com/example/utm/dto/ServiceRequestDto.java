package com.example.utm.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Bu DTO, bir talebin sadece frontend'e gönderilecek bilgilerini içerir.
// Dikkat ederseniz, tam bir User objesi yerine sadece username var. Bu döngüyü kırar.
public record ServiceRequestDto(
    UUID id,
    String title,
    String category,
    String username, // Tam User objesi yerine sadece kullanıcı adı
    Map<String, String> details,
    LocalDateTime createdDate,
    List<OfferDto> offers
) {}