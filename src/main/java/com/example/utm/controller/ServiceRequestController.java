package com.example.utm.controller;

import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.service.ServiceRequestService;
import com.example.utm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceRequestController {

  private final ServiceRequestService requestService;
  private final UserService userService; // Kullanıcıyı bulmak için

  // --- Kullanıcının Kendi Talep İşlemleri ---

  @PostMapping("/me/requests")
  public ResponseEntity<ServiceRequest> createRequest(@RequestBody ServiceRequest request, Principal principal) {
    User currentUser = userService.findByUsername(principal.getName());
    ServiceRequest createdRequest = requestService.createRequestForUser(request, currentUser);
    return ResponseEntity.ok(createdRequest);
  }

  @GetMapping("/me/requests")
  public ResponseEntity<List<ServiceRequest>> getMyRequests(Principal principal) {
    User currentUser = userService.findByUsername(principal.getName());
    List<ServiceRequest> myRequests = requestService.findRequestsByUser(currentUser);
    return ResponseEntity.ok(myRequests);
  }

  // --- Admin İşlemleri ---

  @GetMapping("/admin/requests") // DOĞRU YERİ BURASI
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<ServiceRequest>> getAllRequests() {
    List<ServiceRequest> allRequests = requestService.findAllRequests();
    return ResponseEntity.ok(allRequests);
  }
}