package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Offer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "request_id", referencedColumnName = "id")
  @JsonBackReference // JSON'a çevrilirken sonsuz döngüyü önler
  private ServiceRequest serviceRequest;

  @ManyToOne(optional = false)
  @JoinColumn(name = "provider_id", referencedColumnName = "id")
  private AdminUser provider; // Teklifi yapan admin

  private double price;

  @Lob // Uzun metinler için
  private String details;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;
}