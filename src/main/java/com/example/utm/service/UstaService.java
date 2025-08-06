package com.example.utm.service;

import com.example.utm.model.Usta;
import com.example.utm.repository.UstaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UstaService {

  private final UstaRepository ustaRepository;

  // Ana sayfadaki dropdown için, halka açık ve sayfasız
  public List<Usta> findAll() {
    return ustaRepository.findAll();
  }

  // Admin paneli için, sayfalı
  public Page<Usta> findAllPaged(Pageable pageable) {
    return ustaRepository.findAll(pageable);
  }

  public Usta createUsta(Usta usta) {
    return ustaRepository.save(usta);
  }

  public void deleteUsta(UUID id) {
    ustaRepository.deleteById(id);
  }
}