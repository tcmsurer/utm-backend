package com.example.utm.controller;

import com.example.utm.model.AdminUser;
import com.example.utm.model.Offer;
import com.example.utm.service.AdminUserService;
import com.example.utm.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/requests/{requestId}/offers")
@RequiredArgsConstructor
public class OfferController {

  private final OfferService offerService;
  private final AdminUserService adminUserService;

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Offer> createOffer(@PathVariable UUID requestId, @RequestBody Offer offer, Principal principal) {
    AdminUser admin = adminUserService.findByUsername(principal.getName());
    Offer createdOffer = offerService.createOfferForRequest(requestId, offer, admin);
    return ResponseEntity.ok(createdOffer);
  }
}