package com.example.utm.service;

import com.example.utm.model.Hizmet;
import com.example.utm.repository.HizmetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HizmetService {

  private final HizmetRepository hizmetRepository;

  public List<Hizmet> getAllHizmetler() {
    return hizmetRepository.findAll();
  }

  // Admin için CRUD operasyonları
  public Hizmet createHizmet(Hizmet hizmet) {
    return hizmetRepository.save(hizmet);
  }

  public Hizmet getHizmetById(UUID id) {
    return hizmetRepository.findById(id).orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
  }

  public Hizmet updateHizmet(UUID id, Hizmet hizmetDetails) {
    Hizmet hizmet = getHizmetById(id);
    hizmet.setTitle(hizmetDetails.getTitle());
    hizmet.setDescription(hizmetDetails.getDescription());
    hizmet.setVideoUrl(hizmetDetails.getVideoUrl());
    return hizmetRepository.save(hizmet);
  }

  public void deleteHizmet(UUID id) {
    hizmetRepository.deleteById(id);
  }
}