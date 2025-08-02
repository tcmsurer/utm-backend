package com.example.utm.service;

import com.example.utm.model.MailLog;
import com.example.utm.repository.MailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailLogService {

  private final MailLogRepository mailLogRepository;

  public void createLog(MailLog mailLog) {
    mailLogRepository.save(mailLog);
  }

  public List<MailLog> findAllLogs() {
    return mailLogRepository.findAll();
  }
}