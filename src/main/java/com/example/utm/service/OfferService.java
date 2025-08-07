package com.example.utm.service;

import com.example.utm.dto.OfferDto;
import com.example.utm.model.AdminUser;
import com.example.utm.model.Offer;
import com.example.utm.model.ServiceRequest;
import com.example.utm.repository.OfferRepository;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

  private final OfferRepository offerRepository;
  private final ServiceRequestRepository requestRepository;
  private final MailService mailService;

  @Transactional
  public OfferDto createOfferForRequest(UUID requestId, Offer offer, AdminUser admin) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    offer.setServiceRequest(request);
    offer.setProvider(admin);

    Offer savedOffer = offerRepository.save(offer);

    String subject = "Hizmet Talebiniz İçin Yeni Bir Teklif Aldınız!";
    String userFullName = request.getUser().getFullName();
    String requestTitle = request.getTitle();
    double offerPrice = savedOffer.getPrice();
    String offerDetails = savedOffer.getDetails();
    String detailsUrl = "https://ustatedarikmerkezi.com/taleplerim";

    String bodyHtml = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; border: 1px solid #dddddd; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                <div style="background-color: #2196F3; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;">
                    <h1>Usta Tedarik Merkezi</h1>
                </div>
                <div style="padding: 30px 20px; color: #333333; line-height: 1.6;">
                    <h2 style="color: #333333;">Merhaba, %s!</h2>
                    <p><b>"%s"</b> başlıklı hizmet talebiniz için yeni bir fiyat teklifi gönderildi.</p>
                    <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;">
                    <h3 style="color: #333333;">Teklif Detayları</h3>
                    <p style="margin: 5px 0;"><b>Teklif Fiyatı:</b> <span style="font-size: 18px; color: #007bff; font-weight: bold;">%.2f ₺</span></p>
                    <p style="margin: 5px 0;"><b>Adminin Mesajı:</b></p>
                    <p style="background-color: #f9f9f9; border-left: 4px solid #dddddd; padding: 10px; margin-top: 5px; white-space: pre-wrap;"><i>%s</i></p>
                    <div style="text-align: center; margin-top: 30px;">
                        <a href="%s" target="_blank" style="display: inline-block; padding: 12px 25px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;">
                            Talebinizi Görüntüleyin
                        </a>
                    </div>
                </div>
                <div style="text-align: center; padding: 20px; font-size: 12px; color: #888888; background-color: #f4f4f4; border-radius: 0 0 8px 8px;">
                    <p>Usta Tedarik Merkezi</p>
                </div>
            </div>
            """, userFullName, requestTitle, offerPrice, offerDetails, detailsUrl);

    mailService.sendOfferEmail(request, subject, bodyHtml);

    return new OfferDto(
        savedOffer.getId(),
        savedOffer.getPrice(),
        savedOffer.getDetails(),
        savedOffer.getCreatedDate()
    );
  }

  @Transactional
  public OfferDto updateOfferPrice(UUID offerId, double newPrice) {
    Offer offerToUpdate = offerRepository.findById(offerId)
        .orElseThrow(() -> new RuntimeException("Offer not found with id: " + offerId));

    offerToUpdate.setPrice(newPrice);
    Offer savedOffer = offerRepository.save(offerToUpdate);

    return new OfferDto(
        savedOffer.getId(),
        savedOffer.getPrice(),
        savedOffer.getDetails(),
        savedOffer.getCreatedDate()
    );
  }
}