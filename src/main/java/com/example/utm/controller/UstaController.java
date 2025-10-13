package com.example.utm.controller;

import com.example.utm.model.Usta;
import com.example.utm.service.UstaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.utm.dto.UstaDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UstaController {

  private final UstaService ustaService;

  /**
   * Halka açık endpoint. Sadece aktif olan ustaları listeler.
   */
  @GetMapping("/ustalar")
  public ResponseEntity<List<UstaDto>> getActiveUstalar() {
    return ResponseEntity.ok(ustaService.getAllActiveUstas());
  }

  /**
   * Admin paneli için. Tüm ustaları (aktif ve pasif) sayfalı olarak listeler.
   */
  @GetMapping("/admin/ustalar")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<Usta>> getAdminUstalar(Pageable pageable) {
    return ResponseEntity.ok(ustaService.getAllUstasForAdmin(pageable));
  }

  /**
   * Admin için. Yeni bir usta oluşturur. Profil resmi de kabul eder.
   */
  @PostMapping(value = "/admin/ustalar", consumes = {"multipart/form-data"})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Usta> createUsta(
      @RequestPart("name") String name,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

    Usta createdUsta = ustaService.createUsta(name, profileImage);
    return ResponseEntity.ok(createdUsta);
  }

  /**
   * Admin için. Bir ustayı pasif duruma getirir.
   */
  @DeleteMapping("/admin/ustalar/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> deactivateUsta(@PathVariable UUID id) {
    ustaService.deactivateUsta(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Admin için. Pasif durumdaki bir ustayı tekrar aktif hale getirir.
   */
  @PutMapping("/admin/ustalar/{id}/activate")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> activateUsta(@PathVariable UUID id) {
    ustaService.activateUsta(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping(value = "/admin/ustalar/{id}", consumes = {"multipart/form-data"})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Usta> updateUsta(
      @PathVariable UUID id,
      @RequestPart("name") String name,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

    Usta updatedUsta = ustaService.updateUsta(id, name, profileImage);
    return ResponseEntity.ok(updatedUsta);
  }
}