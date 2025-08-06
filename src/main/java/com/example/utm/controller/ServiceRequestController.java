package com.example.utm.controller;

import com.example.utm.dto.ServiceRequestDto;
import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.service.ServiceRequestService;
import com.example.utm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceRequestController {

  private final ServiceRequestService requestService;
  private final UserService userService;

  @PostMapping("/me/requests")
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<ServiceRequest> createRequest(@RequestBody ServiceRequest request, Principal principal) {
    User currentUser = userService.findByUsername(principal.getName());
    ServiceRequest createdRequest = requestService.createRequestForUser(request, currentUser);
    return ResponseEntity.ok(createdRequest);
  }

  @GetMapping("/me/requests")
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<List<ServiceRequestDto>> getMyRequests(Principal principal) {
    User currentUser = userService.findByUsername(principal.getName());
    List<ServiceRequestDto> myRequests = requestService.findRequestsByUser(currentUser);
    return ResponseEntity.ok(myRequests);
  }

  @PutMapping("/me/requests/{id}/close")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Void> closeMyRequest(@PathVariable UUID id, Principal principal) {
    requestService.verifyRequestOwner(id, principal.getName());
    requestService.closeRequestByUser(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin/requests")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<ServiceRequestDto>> getAllRequests(Pageable pageable) {
    Page<ServiceRequestDto> allRequests = requestService.findAllRequests(pageable);
    return ResponseEntity.ok(allRequests);
  }

  @PutMapping("/admin/requests/{id}/close")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> closeRequestByAdmin(@PathVariable UUID id) {
    requestService.closeRequestByAdmin(id);
    return ResponseEntity.ok().build();
  }
}