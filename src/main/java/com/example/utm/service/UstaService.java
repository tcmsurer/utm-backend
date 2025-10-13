package com.example.utm.service;

import com.example.utm.model.Usta;
import com.example.utm.repository.UstaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.utm.dto.UstaDto; // Yeni import
import java.util.stream.Collectors; // Yeni import

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UstaService {

  private final UstaRepository ustaRepository;
  private final FileStorageService fileStorageService;

  /**
   * Sadece aktif olan ustaları, kullanıcıların görmesi için listeler.
   * @return Aktif olan Usta listesi.
   */
  @Transactional(readOnly = true)
  public List<UstaDto> getAllActiveUstas() {
    return ustaRepository.findAllByIsActiveTrue().stream()
        .map(usta -> new UstaDto(usta.getId(), usta.getName(), usta.getProfileImageUrl()))
        .collect(Collectors.toList());
  }

  /**
   * Admin paneli için tüm ustaları (aktif ve pasif) sayfalı olarak listeler.
   * @param pageable Sayfalama bilgisi.
   * @return Sayfalanmış Usta listesi.
   */
  @Transactional(readOnly = true)
  public Page<Usta> getAllUstasForAdmin(Pageable pageable) {
    return ustaRepository.findAll(pageable);
  }

  /**
   * Yeni bir usta oluşturur ve profil resmini kaydeder.
   * @param name Ustanın adı.
   * @param profileImage Profil resmi dosyası (opsiyonel).
   * @return Oluşturulan Usta nesnesi.
   */
  @Transactional
  public Usta createUsta(String name, MultipartFile profileImage) {
    if (ustaRepository.findByName(name).isPresent()) {
      throw new RuntimeException("Bu usta adı zaten mevcut: " + name);
    }

    Usta newUsta = new Usta();
    newUsta.setName(name);
    newUsta.setActive(true);

    if (profileImage != null && !profileImage.isEmpty()) {
      String fileName = fileStorageService.storeFile(profileImage);
      newUsta.setProfileImageUrl(fileName);
    }

    return ustaRepository.save(newUsta);
  }

  /**
   * Belirtilen ID'ye sahip ustayı pasif hale getirir.
   * @param id Pasif hale getirilecek ustanın ID'si.
   */
  @Transactional
  public void deactivateUsta(UUID id) {
    Usta usta = ustaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usta bulunamadı: " + id));
    usta.setActive(false);
    ustaRepository.save(usta);
  }

  /**
   * Belirtilen ID'ye sahip ustayı aktif hale getirir.
   * @param id Aktif hale getirilecek ustanın ID'si.
   */
  @Transactional
  public void activateUsta(UUID id) {
    Usta usta = ustaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usta bulunamadı: " + id));
    usta.setActive(true);
    ustaRepository.save(usta);
  }

  /**
   * Mevcut bir ustayı günceller. Adını veya profil resmini değiştirebilir.
   * @param id Güncellenecek ustanın ID'si.
   * @param newName Yeni usta adı.
   * @param profileImage Yeni profil resmi (opsiyonel).
   * @return Güncellenmiş Usta nesnesi.
   */
  @Transactional
  public Usta updateUsta(UUID id, String newName, MultipartFile profileImage) {
    Usta usta = ustaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usta bulunamadı: " + id));

    // Eğer yeni isim mevcut isimden farklıysa ve zaten başka bir ustaya ait değilse güncelle
    Optional<Usta> existingUstaWithName = ustaRepository.findByName(newName);
    if (existingUstaWithName.isPresent() && !existingUstaWithName.get().getId().equals(id)) {
      throw new RuntimeException("Bu usta adı zaten başka bir usta tarafından kullanılıyor: " + newName);
    }
    usta.setName(newName);

    if (profileImage != null && !profileImage.isEmpty()) {
      // İleride eski resmi diskten silme logiği eklenebilir
      String fileName = fileStorageService.storeFile(profileImage);
      usta.setProfileImageUrl(fileName);
    }

    return ustaRepository.save(usta);
  }

}