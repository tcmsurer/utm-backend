package com.example.utm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Soru {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "usta_id")
  private Usta usta;

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private String type;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "soru_options", joinColumns = @JoinColumn(name = "soru_id"))
  @Column(name = "options")
  @JdbcTypeCode(SqlTypes.JSON)
  private List<String> options;

  @Column(name = "soru_order", nullable = false) // DİKKAT: Bu satır güncellendi
  private int order;
}