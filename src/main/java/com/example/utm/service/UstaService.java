package com.example.utm.service;

import com.example.utm.model.Usta;
import com.example.utm.repository.UstaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UstaService {

  private final UstaRepository ustaRepository;

  /**
   * Tüm usta tiplerini getirir.
   * Bu, ana sayfadaki "Usta Seçiniz" dropdown'ını doldurmak için kullanılır.
   * @return Usta tiplerinin listesi.
   */
  public List<Usta> findAll() {
    return ustaRepository.findAll();
  }

  /**
   * Admin tarafından yeni bir usta tipi oluşturmak için kullanılır.
   * @param usta Adı içeren usta objesi.
   * @return Kaydedilen usta objesi.
   */
  public Usta createUsta(Usta usta) {
    // Burada aynı isimde usta var mı gibi kontroller eklenebilir.
    return ustaRepository.save(usta);
  }

  /**
   * Admin tarafından bir usta tipini silmek için kullanılır.
   * @param id Silinecek usta tipinin ID'si.
   */
  public void deleteUsta(UUID id) {
    ustaRepository.deleteById(id);
  }
}