package com.example.utm.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
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

  @Lob // Uzun metinler için
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonManagedReference // JSON'a çevrilirken sonsuz döngüyü önler
  private List<Offer> offers;

  @Column(nullable = false)
  private String category; // e.g., "Boyacı"

  // Bu bilgiler artık User objesinden de alınabilir ancak talep anındaki
  // bilgiyi saklamak için burada tutmak da bir yöntemdir.
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
}