package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class MailLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "request_id")
  private ServiceRequest serviceRequest;

  @Column(nullable = false)
  private String email;

  private String subject;

  @Lob
  private String body;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime sentDate;
}