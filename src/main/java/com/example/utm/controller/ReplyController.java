package com.example.utm.controller;

import com.example.utm.model.Reply;
import com.example.utm.service.ReplyService;
import com.example.utm.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Reply> addUserReply(@PathVariable UUID requestId, @RequestBody Reply reply, Principal principal) {
    requestService.verifyRequestOwner(requestId, principal.getName());
    Reply createdReply = replyService.createReplyForRequest(requestId, reply, principal.getName());
    return ResponseEntity.ok(createdReply);
  }

  @PostMapping("/admin/requests/{requestId}/replies")
  public ResponseEntity<Reply> addAdminReply(@PathVariable UUID requestId, @RequestBody Reply reply, Principal principal) {
    Reply createdReply = replyService.createReplyForRequest(requestId, reply, principal.getName() + " (Admin)");
    return ResponseEntity.ok(createdReply);
  }

  @GetMapping("/me/requests/{requestId}/replies")
  public ResponseEntity<List<Reply>> getRepliesForMyRequest(@PathVariable UUID requestId, Principal principal) {
    requestService.verifyRequestOwner(requestId, principal.getName());
    return ResponseEntity.ok(replyService.findRepliesByRequest(requestId));
  }

  @GetMapping("/admin/requests/{requestId}/replies")
  public ResponseEntity<List<Reply>> getRepliesForAdmin(@PathVariable UUID requestId) {
    return ResponseEntity.ok(replyService.findRepliesByRequest(requestId));
  }
}