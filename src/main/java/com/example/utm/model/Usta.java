package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Usta {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String name;

  // DİKKAT: Alan adı "active" yerine "isActive" olarak değiştirildi
  @Column(nullable = false)
  private boolean isActive = true;

  private String profileImageUrl;
}