package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Reply {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "request_id")
  private ServiceRequest serviceRequest;

  // EKSİK OLAN VE HATAYI ÇÖZECEK ALAN:
  @Column(nullable = false)
  private String author; // Cevabı yazan kişinin kullanıcı adı (örn: "user1", "admin_utm")

  @Lob
  @Column(nullable = false)
  private String text;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime date;
}