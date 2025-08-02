package com.example.utm.service;

import com.example.utm.model.Soru;
import com.example.utm.repository.SoruRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SoruService {

  private final SoruRepository soruRepository;

  public List<Soru> getAllSorular() {
    return soruRepository.findAll();
  }

  public List<Soru> getSorularByUstaName(String ustaName) {
    return soruRepository.findByUsta_NameOrderByOrderAsc(ustaName);
  }

  public Soru createSoru(Soru soru) {
    // Gerekli kontroller veya iş mantığı burada eklenebilir
    return soruRepository.save(soru);
  }

  public Soru updateSoru(UUID id, Soru updatedSoru) {
    // Var olan soruyu bul, güncelle ve kaydet
    return soruRepository.findById(id).map(soru -> {
      soru.setQuestion(updatedSoru.getQuestion());
      soru.setType(updatedSoru.getType());
      soru.setOrder(updatedSoru.getOrder());
      soru.setOptions(updatedSoru.getOptions());
      soru.setUsta(updatedSoru.getUsta());
      return soruRepository.save(soru);
    }).orElseThrow(() -> new RuntimeException("Soru not found with id " + id));
  }

  public void deleteSoru(UUID id) {
    soruRepository.deleteById(id);
  }
}