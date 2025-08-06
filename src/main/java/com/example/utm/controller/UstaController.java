package com.example.utm.controller;

import com.example.utm.model.Usta;
import com.example.utm.service.UstaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UstaController {

  private final UstaService ustaService;

  // Halka açık, sayfasız
  @GetMapping("/ustalar")
  public ResponseEntity<List<Usta>> getAllUstalar() {
    return ResponseEntity.ok(ustaService.findAll());
  }

  // Admin'e özel, sayfalı
  @GetMapping("/admin/ustalar")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<Usta>> getAllUstalarPaged(Pageable pageable) {
    return ResponseEntity.ok(ustaService.findAllPaged(pageable));
  }

  @PostMapping("/admin/ustalar")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Usta> createUsta(@RequestBody Usta usta) {
    Usta createdUsta = ustaService.createUsta(usta);
    return ResponseEntity.ok(createdUsta);
  }

  @DeleteMapping("/admin/ustalar/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteUsta(@PathVariable UUID id) {
    ustaService.deleteUsta(id);
    return ResponseEntity.noContent().build();
  }
}