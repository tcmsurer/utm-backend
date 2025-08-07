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
public class Reply {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "request_id")
  @JsonBackReference
  private ServiceRequest serviceRequest;

  // DİKKAT: @Column anotasyonu ile veritabanındaki doğru kolon adını belirtiyoruz.
  @Column(name = "author", nullable = false)
  private String senderUsername;

  @Lob
  @Column(nullable = false)
  private String text;

  @CreationTimestamp
  @Column(updatable = false, name = "reply_date")
  private LocalDateTime date;
}