package com.example.utm.controller;

import com.example.utm.model.MailLog;
import com.example.utm.service.MailLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin") // Bu controller'daki tüm endpoint'ler /api/admin ile başlar
@RequiredArgsConstructor
public class AdminController {

  private final MailLogService mailLogService;
  // Gelecekte buraya başka admin servisleri de eklenebilir (örn: DashboardService)

  @GetMapping("/maillogs")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<MailLog>> getAllMailLogs() {
    return ResponseEntity.ok(mailLogService.findAllLogs());
  }
}