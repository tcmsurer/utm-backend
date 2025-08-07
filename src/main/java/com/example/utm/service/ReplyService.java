package com.example.utm.service;

import com.example.utm.model.Reply;
import com.example.utm.model.ServiceRequest;
import com.example.utm.repository.ReplyRepository;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReplyService {

  private final ReplyRepository replyRepository;
  private final ServiceRequestRepository requestRepository;

  @Transactional
  public Reply createReplyForRequest(UUID requestId, Reply reply, String authorUsername) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    reply.setServiceRequest(request);
    reply.setSenderUsername(authorUsername); // BU SATIR HATAYI ÇÖZER

    return replyRepository.save(reply);
  }

  @Transactional(readOnly = true)
  public List<Reply> findRepliesByRequest(UUID requestId) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
    return replyRepository.findByServiceRequestOrderByDateAsc(request);
  }
}