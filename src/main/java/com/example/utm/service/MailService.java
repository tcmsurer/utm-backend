package com.example.utm.service;

import com.example.utm.model.MailLog;
import com.example.utm.model.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async; // Yeni import
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final MailLogService mailLogService;

  @Async // Bu anotasyonu ekleyin
  public void sendOfferEmail(ServiceRequest request, String subject, String body) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(request.getEmail());
      message.setSubject(subject);
      message.setText(body);

      mailSender.send(message);

      MailLog log = new MailLog();
      log.setServiceRequest(request);
      log.setEmail(request.getEmail());
      log.setSubject(subject);
      log.setBody(body);

      mailLogService.createLog(log);
    } catch (Exception e) {
      // Arkaplanda oluşan hataları sunucu loguna yazdır
      System.err.println("E-posta gönderilirken hata oluştu: " + e.getMessage());
    }
  }
}