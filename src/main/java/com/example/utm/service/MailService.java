package com.example.utm.service;

import com.example.utm.model.MailLog;
import com.example.utm.model.ServiceRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final MailLogService mailLogService;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Async
  public void sendOfferEmail(ServiceRequest request, String subject, String body) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(body, true); // HTML olarak göndermek için
      helper.setTo(request.getEmail());
      helper.setSubject(subject);
      mailSender.send(mimeMessage);

      MailLog log = new MailLog();
      log.setServiceRequest(request);
      log.setEmail(request.getEmail());
      log.setSubject(subject);
      log.setBody(body);

      mailLogService.createLog(log);
    } catch (Exception e) {
      System.err.println("Teklif e-postasi gonderilirken hata olustu: " + e.getMessage());
    }
  }

  @Async
  public void sendPasswordResetEmail(String to, String token) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

      String resetUrl = frontendUrl + "/sifre-sifirla?token=" + token;
      String htmlMsg = String.format("""
                <h3>Şifre Sıfırlama Talebi</h3>
                <p>Şifrenizi sıfırlamak için aşağıdaki linke tıklayın:</p>
                <a href="%s" target="_blank" style="background-color: #007bff; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px;">Şifrenizi Sıfırlamak İçin Tıklayınız</a>
                <p>Eğer bu isteği siz yapmadıysanız, bu e-postayı dikkate almayın.</p>
                """, resetUrl);

      helper.setText(htmlMsg, true);
      helper.setTo(to);
      helper.setSubject("Şifre Sıfırlama Talebi");

      mailSender.send(mimeMessage);
    } catch (Exception e) {
      System.err.println("Sifre sifirlama e-postasi gonderilirken hata olustu: " + e.getMessage());
    }
  }
}