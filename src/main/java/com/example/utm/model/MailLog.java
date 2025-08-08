package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MailLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "request_id")
  private ServiceRequest serviceRequest;

  private String email;
  private String subject;

  @Lob
  @Column(columnDefinition = "TEXT") // Bu satır hatayı çözer
  private String body;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime sentDate;
}