package com.example.utm.controller;

import com.example.utm.model.Reply;
import com.example.utm.service.ReplyService;
import com.example.utm.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReplyController {

  private final ReplyService replyService;
  private final ServiceRequestService requestService;

  @PostMapping("/me/requests/{requestId}/replies")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Reply> addUserReply(@PathVariable UUID requestId, @RequestBody Reply reply, Principal principal) {
    requestService.verifyRequestOwner(requestId, principal.getName());
    Reply createdReply = replyService.createReplyForRequest(requestId, reply, principal.getName());
    return ResponseEntity.ok(createdReply);
  }

  @PostMapping("/admin/requests/{requestId}/replies")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Reply> addAdminReply(@PathVariable UUID requestId, @RequestBody Reply reply, Principal principal) {
    Reply createdReply = replyService.createReplyForRequest(requestId, reply, principal.getName());
    return ResponseEntity.ok(createdReply);
  }

  @GetMapping("/requests/{requestId}/replies")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Reply>> getRepliesForRequest(@PathVariable UUID requestId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    boolean isUser = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

    if (isUser) {
      requestService.verifyRequestOwner(requestId, username);
    }

    return ResponseEntity.ok(replyService.findRepliesByRequest(requestId));
  }
}