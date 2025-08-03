package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Import et
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
public class ServiceRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Lob
  private String description;

  // DİKKAT: @JsonBackReference eklendi
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @Column(nullable = false)
  private String category;

  private String email;
  private String phone;
  private String address;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "request_details")
  @MapKeyColumn(name = "question")
  @Column(name = "answer")
  private Map<String, String> details;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference
  private List<Offer> offers;
}