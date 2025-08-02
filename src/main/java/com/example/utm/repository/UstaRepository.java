package com.example.utm.repository;

import com.example.utm.model.Usta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UstaRepository extends JpaRepository<Usta, UUID> {
}