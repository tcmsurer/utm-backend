package com.example.utm.service;

import com.example.utm.dto.CreateRehberIcerikDto;
import com.example.utm.dto.RehberIcerikDto;
import com.example.utm.model.RehberIcerik;
import com.example.utm.model.Usta;
import com.example.utm.repository.RehberIcerikRepository;
import com.example.utm.repository.UstaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RehberIcerikService {

  private final RehberIcerikRepository rehberIcerikRepository;
  private final UstaRepository ustaRepository;
  private final FileStorageService fileStorageService;

  @Transactional
  public RehberIcerik createRehberIcerik(UUID ustaId, CreateRehberIcerikDto dto, MultipartFile mediaFile) {
    Usta usta = ustaRepository.findById(ustaId)
        .orElseThrow(() -> new RuntimeException("Usta not found with id: " + ustaId));

    String fileName = fileStorageService.storeFile(mediaFile);

    RehberIcerik newContent = new RehberIcerik();
    newContent.setTitle(dto.title());
    newContent.setDescription(dto.description());
    newContent.setMediaType(dto.mediaType());
    newContent.setMediaUrl(fileName);
    newContent.setUsta(usta);
    newContent.setActive(true);

    return rehberIcerikRepository.save(newContent);
  }

  @Transactional(readOnly = true)
  public List<RehberIcerikDto> getActivePortfolioByUsta(UUID ustaId) {
    return rehberIcerikRepository.findByUstaIdAndIsActiveTrueOrderByCreatedDateDesc(ustaId).stream()
        .map(item -> new RehberIcerikDto(item.getId(), item.getTitle(), item.getDescription(), item.getMediaUrl(), item.getMediaType()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<RehberIcerik> getPortfolioForAdmin(UUID ustaId) {
    return rehberIcerikRepository.findByUstaIdOrderByCreatedDateDesc(ustaId);
  }

  @Transactional
  public void deleteRehberIcerik(UUID contentId) {
    // İleride dosyayı diskten silme de eklenebilir.
    rehberIcerikRepository.deleteById(contentId);
  }
}