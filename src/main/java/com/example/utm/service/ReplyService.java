package com.example.utm.service;

import com.example.utm.model.Reply;
import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.repository.ReplyRepository;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReplyService {

  private final ReplyRepository replyRepository;
  private final ServiceRequestRepository requestRepository;

  public Reply createReplyForRequest(UUID requestId, Reply reply, String authorUsername) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    reply.setServiceRequest(request);
    reply.setAuthor(authorUsername); // Cevabı yazanın kullanıcı adını sakla

    return replyRepository.save(reply);
  }

  public List<Reply> findRepliesByRequest(UUID requestId) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
    return replyRepository.findByServiceRequestOrderByDateAsc(request);
  }
}