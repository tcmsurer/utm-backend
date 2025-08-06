package com.example.utm.service;

import com.example.utm.dto.MailLogDto;
import com.example.utm.model.MailLog;
import com.example.utm.repository.MailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailLogService {

  private final MailLogRepository mailLogRepository;

  public void createLog(MailLog mailLog) {
    mailLogRepository.save(mailLog);
  }

  @Transactional(readOnly = true)
  public Page<MailLogDto> findAllLogs(Pageable pageable) {
    Page<MailLog> logsPage = mailLogRepository.findAll(pageable);
    return logsPage.map(this::convertToDto); // Page<Entity> -> Page<Dto>
  }

  // Entity'yi DTO'ya dönüştüren yardımcı metod
  private MailLogDto convertToDto(MailLog log) {
    return new MailLogDto(
        log.getId(),
        log.getServiceRequest() != null ? log.getServiceRequest().getTitle() : "N/A",
        log.getEmail(),
        log.getSubject(),
        log.getSentDate()
    );
  }
}