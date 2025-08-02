package com.example.utm.service;

import com.example.utm.model.MailLog;
import com.example.utm.model.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final MailLogService mailLogService;

  public void sendOfferEmail(ServiceRequest request, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(request.getEmail());
    message.setSubject(subject);
    message.setText(body);

    // E-postayı gönder
    mailSender.send(message);

    // Gönderilen e-postayı logla
    MailLog log = new MailLog();
    log.setServiceRequest(request);
    log.setEmail(request.getEmail());
    log.setSubject(subject);
    log.setBody(body);

    mailLogService.createLog(log);
  }
}