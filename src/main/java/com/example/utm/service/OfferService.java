package com.example.utm.service;

import com.example.utm.model.AdminUser;
import com.example.utm.model.Offer;
import com.example.utm.model.ServiceRequest;
import com.example.utm.repository.OfferRepository;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

  private final OfferRepository offerRepository;
  private final ServiceRequestRepository requestRepository;
  private final MailService mailService; // E-posta servisini enjekte et

  public Offer createOfferForRequest(UUID requestId, Offer offer, AdminUser admin) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    offer.setServiceRequest(request);
    offer.setProvider(admin);

    Offer savedOffer = offerRepository.save(offer);

    // TEKLİF KAYDEDİLDİKTEN SONRA E-POSTA GÖNDER
    String subject = "Hizmet Talebiniz İçin Yeni Bir Teklif Aldınız!";
    String body = String.format(
        "Merhaba,\n\n'%s' başlıklı talebiniz için yeni bir fiyat teklifi aldınız.\n\nTeklif Detayları:\nFiyat: %.2f ₺\nMesaj: %s\n\nTalebinizi ve diğer teklifleri görüntülemek için sitemizi ziyaret edebilirsiniz.",
        request.getTitle(),
        savedOffer.getPrice(),
        savedOffer.getDetails()
    );
    mailService.sendOfferEmail(request, subject, body);

    return savedOffer;
  }
}