package com.example.utm.repository;

import com.example.utm.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
  // Gelecekte bir talebe ait tüm teklifleri listelemek gibi
  // özel sorgular gerekirse buraya eklenebilir.
  // Örneğin: List<Offer> findByServiceRequest_Id(UUID requestId);
}