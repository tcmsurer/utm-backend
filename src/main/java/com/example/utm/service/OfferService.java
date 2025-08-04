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
  private final MailService mailService; // e-posta servisini enjekte et

  public Offer createOfferForRequest(UUID requestId, Offer offer, AdminUser admin) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    offer.setServiceRequest(request);
    offer.setProvider(admin);

    Offer savedOffer = offerRepository.save(offer);

    // --- e-posta icerigini guncelleme ---

    // 1. sorulari ve cevaplari guzel bir metin formatina donustur
    String detailsText = request.getDetails().entrySet().stream()
        .map(entry -> "- " + entry.getKey() + ": " + entry.getValue())
        .collect(Collectors.joining("\n"));

    // 2. e-posta konusunu ve ana metnini olustur
    String subject = "Hizmet Talebiniz Icin Yeni Bir Teklif Aldiniz!";
    String body = String.format(
        "Merhaba,\n\n'%s' baslikli talebiniz icin yeni bir fiyat teklifi aldiniz.\n\n" +
            "--- Teklif Detaylari ---\n" +
            "Fiyat: %.2f ₺\n" +
            "Ustanin Mesaji: %s\n\n" +
            "--- Sizin Verdiginiz Bilgiler ---\n" +
            "%s\n\n" +
            "Talebinizi ve diger teklifleri goruntulemek icin sitemizi ziyaret edebilirsiniz.",
        request.getTitle(),
        savedOffer.getPrice(),
        savedOffer.getDetails(),
        detailsText // olusturdugumuz detay metnini buraya ekle
    );

    // 3. e-postayi gonder
    mailService.sendOfferEmail(request, subject, body);

    return savedOffer;
  }
}