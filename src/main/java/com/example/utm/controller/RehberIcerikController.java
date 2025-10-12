package com.example.utm.controller;

import com.example.utm.dto.CreateRehberIcerikDto;
import com.example.utm.dto.RehberIcerikDto;
import com.example.utm.model.RehberIcerik;
import com.example.utm.service.RehberIcerikService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RehberIcerikController {

  private final RehberIcerikService rehberIcerikService;
  private final ObjectMapper objectMapper;

  // Public: Bir usta'nın aktif portfolyo içeriklerini listele
  @GetMapping("/ustalar/{ustaId}/portfolio")
  public ResponseEntity<List<RehberIcerikDto>> getActivePortfolio(@PathVariable UUID ustaId) {
    return ResponseEntity.ok(rehberIcerikService.getActivePortfolioByUsta(ustaId));
  }

  // Admin: Bir usta'nın tüm portfolyo içeriklerini listele (aktif/pasif)
  @GetMapping("/admin/ustalar/{ustaId}/portfolio")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<RehberIcerik>> getAdminPortfolio(@PathVariable UUID ustaId) {
    return ResponseEntity.ok(rehberIcerikService.getPortfolioForAdmin(ustaId));
  }

  // Admin: Bir usta için yeni portfolyo içeriği oluştur
  @PostMapping(value = "/admin/ustalar/{ustaId}/portfolio", consumes = {"multipart/form-data"})
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RehberIcerik> createPortfolioItem(@PathVariable UUID ustaId,
      @RequestPart("dto") String dtoString,
      @RequestPart("mediaFile") MultipartFile mediaFile) throws IOException {
    CreateRehberIcerikDto dto = objectMapper.readValue(dtoString, CreateRehberIcerikDto.class);
    RehberIcerik createdItem = rehberIcerikService.createRehberIcerik(ustaId, dto, mediaFile);
    return ResponseEntity.ok(createdItem);
  }

  // Admin: Bir portfolyo içeriğini sil
  @DeleteMapping("/admin/portfolio/{contentId}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> deletePortfolioItem(@PathVariable UUID contentId) {
    rehberIcerikService.deleteRehberIcerik(contentId);
    return ResponseEntity.noContent().build();
  }
}