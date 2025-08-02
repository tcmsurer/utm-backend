package com.example.utm.repository;

import com.example.utm.model.MailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MailLogRepository extends JpaRepository<MailLog, UUID> {
}