package com.example.utm.repository;

import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {

  List<ServiceRequest> findByUser(User user);

  // Tüm talepleri tarihe göre tersten sıralayarak ve sayfalayarak getirir
  Page<ServiceRequest> findAllByOrderByCreatedDateDesc(Pageable pageable);
}