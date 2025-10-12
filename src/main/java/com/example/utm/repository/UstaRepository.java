package com.example.utm.repository;

import com.example.utm.model.Usta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UstaRepository extends JpaRepository<Usta, UUID> {

  // DİKKAT: Metot adı "findAllByActiveTrue" yerine "findAllByIsActiveTrue" olarak değiştirildi
  List<Usta> findAllByIsActiveTrue();

  Optional<Usta> findByName(String name);
}