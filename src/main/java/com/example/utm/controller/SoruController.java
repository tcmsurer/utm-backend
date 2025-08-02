package com.example.utm.controller;

import com.example.utm.model.Soru;
import com.example.utm.service.SoruService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api") // Ana path /api olarak değiştirildi
@RequiredArgsConstructor
public class SoruController {

  private final SoruService soruService;

  // BU ENDPOINT HERKESE AÇIK
  @GetMapping("/sorular/usta/{ustaName}")
  public ResponseEntity<List<Soru>> getByUsta(@PathVariable String ustaName) {
    return ResponseEntity.ok(soruService.getSorularByUstaName(ustaName));
  }

  // --- AŞAĞIDAKİ ENDPOINT'LER SADECE ADMIN İÇİN ---

  @GetMapping("/admin/sorular")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Soru>> getAll() {
    return ResponseEntity.ok(soruService.getAllSorular());
  }

  @PostMapping("/admin/sorular")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Soru> create(@RequestBody Soru soru) {
    return ResponseEntity.ok(soruService.createSoru(soru));
  }

  @PutMapping("/admin/sorular/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Soru> update(@PathVariable UUID id, @RequestBody Soru soru) {
    return ResponseEntity.ok(soruService.updateSoru(id, soru));
  }

  @DeleteMapping("/admin/sorular/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    soruService.deleteSoru(id);
    return ResponseEntity.noContent().build();
  }
}