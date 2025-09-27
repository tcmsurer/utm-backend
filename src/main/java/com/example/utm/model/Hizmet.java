package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Hizmet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private String videoUrl;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;
}