package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Import et
import jakarta.persistence.*;
import lombok.Data;
import java.util.List; // Import et
import java.util.UUID;

@Data
@Entity
@Table(name = "app_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;

  private String address;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<ServiceRequest> serviceRequests;
}