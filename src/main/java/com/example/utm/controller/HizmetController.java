package com.example.utm.controller;

import com.example.utm.model.Hizmet;
import com.example.utm.service.HizmetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HizmetController {

  private final HizmetService hizmetService;

  // --- Herkesin Erişebileceği Public Endpoint ---
  @GetMapping("/hizmetler")
  public ResponseEntity<List<Hizmet>> getAllHizmetler() {
    return ResponseEntity.ok(hizmetService.getAllHizmetler());
  }

  // --- Sadece Admin'in Erişebileceği Endpoints ---

  // DİKKAT: Admin için de hizmetleri listeleyen bir GET endpoint'i ekliyoruz.
  @GetMapping("/admin/hizmetler")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<Hizmet>> getAdminAllHizmetler() {
    return ResponseEntity.ok(hizmetService.getAllHizmetler());
  }

  @PostMapping("/admin/hizmetler")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Hizmet> createHizmet(@RequestBody Hizmet hizmet) {
    return ResponseEntity.ok(hizmetService.createHizmet(hizmet));
  }

  @GetMapping("/admin/hizmetler/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Hizmet> getHizmetById(@PathVariable UUID id) {
    return ResponseEntity.ok(hizmetService.getHizmetById(id));
  }

  @PutMapping("/admin/hizmetler/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Hizmet> updateHizmet(@PathVariable UUID id, @RequestBody Hizmet hizmetDetails) {
    return ResponseEntity.ok(hizmetService.updateHizmet(id, hizmetDetails));
  }

  @DeleteMapping("/admin/hizmetler/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> deleteHizmet(@PathVariable UUID id) {
    hizmetService.deleteHizmet(id);
    return ResponseEntity.noContent().build();
  }
}