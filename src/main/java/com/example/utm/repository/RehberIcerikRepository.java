package com.example.utm.repository;

import com.example.utm.model.RehberIcerik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RehberIcerikRepository extends JpaRepository<RehberIcerik, UUID> {
  List<RehberIcerik> findByUstaIdAndIsActiveTrueOrderByCreatedDateDesc(UUID ustaId);
  List<RehberIcerik> findByUstaIdOrderByCreatedDateDesc(UUID ustaId);
}