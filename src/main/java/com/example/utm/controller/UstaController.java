package com.example.utm.controller;

import com.example.utm.model.Usta;
import com.example.utm.service.UstaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api") // Ana path
@RequiredArgsConstructor
public class UstaController {

  private final UstaService ustaService;

  // BU ENDPOINT HERKESE AÇIK
  @GetMapping("/ustalar")
  public ResponseEntity<List<Usta>> getAllUstalar() {
    return ResponseEntity.ok(ustaService.findAll());
  }

  // --- AŞAĞIDAKİ ENDPOINT'LER SADECE ADMIN İÇİN ---

  @PostMapping("/admin/ustalar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Usta> createUsta(@RequestBody Usta usta) {
    Usta createdUsta = ustaService.createUsta(usta);
    return ResponseEntity.ok(createdUsta);
  }

  @DeleteMapping("/admin/ustalar/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUsta(@PathVariable UUID id) {
    ustaService.deleteUsta(id);
    return ResponseEntity.noContent().build();
  }
}