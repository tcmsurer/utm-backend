package com.example.utm.controller;

import com.example.utm.dto.OfferDto;
import com.example.utm.model.AdminUser;
import com.example.utm.model.Offer;
import com.example.utm.service.AdminUserService;
import com.example.utm.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class OfferController {

  private final OfferService offerService;
  private final AdminUserService adminUserService;

  @PostMapping("/requests/{requestId}/offers")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<OfferDto> createOffer(@PathVariable UUID requestId, @RequestBody Offer offer, Principal principal) {
    AdminUser admin = adminUserService.findByUsername(principal.getName());
    OfferDto createdOfferDto = offerService.createOfferForRequest(requestId, offer, admin);
    return ResponseEntity.ok(createdOfferDto);
  }

  @PutMapping("/offers/{offerId}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<OfferDto> updateOffer(@PathVariable UUID offerId, @RequestBody Map<String, Double> payload) {
    Double newPrice = payload.get("price");
    if (newPrice == null) {
      return ResponseEntity.badRequest().build();
    }
    OfferDto updatedOfferDto = offerService.updateOfferPrice(offerId, newPrice);
    return ResponseEntity.ok(updatedOfferDto);
  }
}