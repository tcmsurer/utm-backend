package com.example.utm.controller;

import com.example.utm.model.MailLog;
import com.example.utm.service.MailLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.utm.dto.MailLogDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final MailLogService mailLogService;

  @GetMapping("/maillogs")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<MailLogDto>> getAllMailLogs(Pageable pageable) {
    return ResponseEntity.ok(mailLogService.findAllLogs(pageable));
  }
}