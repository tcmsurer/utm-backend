package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Soru {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "usta_id", referencedColumnName = "id")
  private Usta usta; // Artık ustaName yerine direkt Usta objesi

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private String type; // "text", "select", "number"

  // Bu liste için ayrı bir tablo oluşturulacak (soru_options)
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> options; // Select için

  @Column(name = "soru_order", nullable = false)
  private int order; // "order" rezerve bir kelime olabileceğinden "soru_order" olarak değiştirildi
}