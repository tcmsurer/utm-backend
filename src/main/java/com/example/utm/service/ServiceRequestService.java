package com.example.utm.service;

import com.example.utm.dto.OfferDto;
import com.example.utm.dto.ServiceRequestDto;
import com.example.utm.dto.UserContactDto;
import com.example.utm.model.RequestStatus;
import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

  private final ServiceRequestRepository requestRepository;

  @Transactional(readOnly = true)
  public Page<ServiceRequestDto> findAllRequests(Pageable pageable) {
    Page<ServiceRequest> requestsPage = requestRepository.findAllByOrderByCreatedDateDesc(pageable);
    return requestsPage.map(this::convertToDto);
  }

  @Transactional
  public ServiceRequest createRequestForUser(ServiceRequest request, User user) {

    if (!user.isEmailVerified()) {
      throw new IllegalStateException("Talep oluşturmak için e-posta adresinizi doğrulamanız gerekmektedir.");
    }
    request.setUser(user);
    request.setEmail(user.getEmail());
    request.setPhone(user.getPhone());

    if (!StringUtils.hasText(request.getAddress())) {
      request.setAddress(user.getAddress());
    }

    request.setStatus(RequestStatus.OPEN);
    return requestRepository.save(request);
  }

  @Transactional(readOnly = true)
  public List<ServiceRequestDto> findRequestsByUser(User user) {
    List<ServiceRequest> requests = requestRepository.findByUser(user);
    return requests.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public ServiceRequest closeRequestByUser(UUID requestId) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found"));
    request.setStatus(RequestStatus.CLOSED_BY_USER);
    return requestRepository.save(request);
  }

  @Transactional
  public ServiceRequest closeRequestByAdmin(UUID requestId) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found"));
    request.setStatus(RequestStatus.CLOSED_BY_ADMIN);
    return requestRepository.save(request);
  }

  private ServiceRequestDto convertToDto(ServiceRequest request) {
    List<OfferDto> offerDtos = request.getOffers() == null ? List.of() : request.getOffers().stream()
        .map(offer -> new OfferDto(
            offer.getId(),
            offer.getPrice(),
            offer.getDetails(),
            offer.getCreatedDate()
        ))
        .collect(Collectors.toList());

    User userEntity = request.getUser();
    UserContactDto userContactDto = new UserContactDto(
        userEntity.getFullName(),
        userEntity.getEmail(),
        userEntity.getPhone(),
        userEntity.getUsername()
    );

    return new ServiceRequestDto(
        request.getId(),
        request.getTitle(),
        request.getCategory(),
        userContactDto,
        request.getDetails(),
        request.getAddress(), // YENİ EKLENDİ
        request.getCreatedDate(),
        offerDtos,
        request.getStatus()
    );
  }

  public void verifyRequestOwner(UUID requestId, String username) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    if (!request.getUser().getUsername().equals(username)) {
      throw new AccessDeniedException("You do not have permission to access this request.");
    }
  }
}