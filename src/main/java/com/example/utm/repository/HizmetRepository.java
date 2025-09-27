package com.example.utm.repository;

import com.example.utm.model.Hizmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HizmetRepository extends JpaRepository<Hizmet, UUID> {
}