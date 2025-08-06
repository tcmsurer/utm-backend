package com.example.utm.controller;

import com.example.utm.model.Soru;
import com.example.utm.service.SoruService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SoruController {

  private final SoruService soruService;

  // Halka açık endpoint, yetki kontrolü yok
  @GetMapping("/sorular/usta/{ustaName}")
  public ResponseEntity<List<Soru>> getByUsta(@PathVariable String ustaName) {
    return ResponseEntity.ok(soruService.getSorularByUstaName(ustaName));
  }

  // Admin'e özel endpoint'ler
  @GetMapping("/admin/sorular")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<Soru>> getAll(Pageable pageable) {
    return ResponseEntity.ok(soruService.getAllSorular(pageable));
  }

  @PostMapping("/admin/sorular")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Soru> create(@RequestBody Soru soru) {
    return ResponseEntity.ok(soruService.createSoru(soru));
  }

  @PutMapping("/admin/sorular/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Soru> update(@PathVariable UUID id, @RequestBody Soru soru) {
    return ResponseEntity.ok(soruService.updateSoru(id, soru));
  }

  @DeleteMapping("/admin/sorular/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    soruService.deleteSoru(id);
    return ResponseEntity.noContent().build();
  }
}