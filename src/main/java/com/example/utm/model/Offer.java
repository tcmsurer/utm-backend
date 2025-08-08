package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Offer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "request_id")
  @JsonBackReference
  private ServiceRequest serviceRequest;

  @ManyToOne(optional = false)
  @JoinColumn(name = "provider_id")
  private AdminUser provider;

  @Column(nullable = false)
  private double price;

  @Lob
  private String details;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  // --- LOMBOK SORUNU ICIN MANUEL EKLENEN METODLAR ---
  public UUID getId() {
    return id;
  }

  public double getPrice() {
    return price;
  }

  public String getDetails() {
    return details;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }
  // --- BITIS ---
}