package com.example.utm.service;

import com.example.utm.model.AdminUser;
import com.example.utm.model.Offer;
import com.example.utm.model.ServiceRequest;
import com.example.utm.repository.OfferRepository;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

  private final OfferRepository offerRepository;
  private final ServiceRequestRepository requestRepository;
  private final MailService mailService;

  public Offer createOfferForRequest(UUID requestId, Offer offer, AdminUser admin) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    offer.setServiceRequest(request);
    offer.setProvider(admin);

    Offer savedOffer = offerRepository.save(offer);

    // --- E-POSTA İÇERİĞİNİ GÜNCELLEME ---

    // 1. Soruları ve cevapları güzel bir metin formatına dönüştür
    String detailsText = request.getDetails().entrySet().stream()
        .map(entry -> "- " + entry.getKey() + ": " + entry.getValue())
        .collect(Collectors.joining("\n"));

    // 2. E-posta konusunu ve ana metnini oluştur
    String subject = "Hizmet Talebiniz İçin Yeni Bir Teklif Aldınız!";
    String body = String.format(
        "Merhaba,\n\n'%s' başlıklı talebiniz için yeni bir fiyat teklifi aldınız.\n\n" +
            "--- Teklif Detayları ---\n" +
            "Fiyat: %.2f ₺\n" +
            "Ustanın Mesajı: %s\n\n" +
            "--- Sizin Verdiğiniz Bilgiler ---\n" +
            "%s\n\n" +
            "Talebinizi ve diğer teklifleri görüntülemek için sitemizi ziyaret edebilirsiniz.",
        request.getTitle(),
        savedOffer.getPrice(),
        savedOffer.getDetails(),
        detailsText // Oluşturduğumuz detay metnini buraya ekle
    );

    // 3. E-postayı gönder
    mailService.sendOfferEmail(request, subject, body);

    return savedOffer;
  }
}