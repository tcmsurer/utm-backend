package com.example.utm.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MailLogDto(
    UUID id,
    String requestTitle, // Tam ServiceRequest objesi yerine sadece başlığı
    String email,
    String subject,
    LocalDateTime sentDate
) {}