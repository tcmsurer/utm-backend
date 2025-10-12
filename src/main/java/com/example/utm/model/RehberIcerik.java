package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class RehberIcerik {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private String mediaUrl;

  @Column(nullable = false)
  private String mediaType; // "IMAGE" veya "VIDEO"

  private boolean isActive = true;

  @CreationTimestamp
  private LocalDateTime createdDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usta_id")
  @JsonIgnore // JSON'a çevrilirken sonsuz döngüyü önler
  private Usta usta;
}