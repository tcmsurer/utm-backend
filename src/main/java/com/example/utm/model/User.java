package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "app_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private UUID id;

  @Column(nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;

  private String address;

  // Sifre sifirlama icin yeni alanlar
  private String passwordResetToken;
  private LocalDateTime passwordResetTokenExpiry;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<ServiceRequest> serviceRequests;

  public String getUsername() { return this.username; }
  public String getPassword() { return this.password; }
}