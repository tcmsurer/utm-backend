package com.example.utm.repository;

import com.example.utm.model.Reply;
import com.example.utm.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, UUID> {
  // Belirli bir talebe ait cevapları, tarihe göre artan sırada getirir.
  List<Reply> findByServiceRequestOrderByDateAsc(ServiceRequest serviceRequest);
}