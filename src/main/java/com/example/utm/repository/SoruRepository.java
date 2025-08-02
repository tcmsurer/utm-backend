package com.example.utm.repository;

import com.example.utm.model.Soru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SoruRepository extends JpaRepository<Soru, UUID> {
  // Usta ismine göre ve 'order' alanına göre sıralanmış soruları getirir.
  List<Soru> findByUsta_NameOrderByOrderAsc(String ustaName);
}