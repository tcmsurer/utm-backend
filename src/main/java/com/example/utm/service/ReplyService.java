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
  public Reply createReplyForRequest(UUID requestId, String text, String authorUsername) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    Reply newReply = new Reply();
    newReply.setServiceRequest(request);
    newReply.setSenderUsername(authorUsername);
    newReply.setText(text);

    return replyRepository.save(newReply);
  }

  @Transactional(readOnly = true)
  public List<Reply> findRepliesByRequest(UUID requestId) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
    return replyRepository.findByServiceRequestOrderByDateAsc(request);
  }
}