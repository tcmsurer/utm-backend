package com.example.utm.service;

import com.example.utm.dto.OfferDto;
import com.example.utm.dto.ServiceRequestDto;
import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

  private final ServiceRequestRepository requestRepository;

  // YENİ EKLENEN METOD
  public List<ServiceRequestDto> findAllRequests() {
    List<ServiceRequest> requests = requestRepository.findAll();
    return requests.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public ServiceRequest createRequestForUser(ServiceRequest request, User user) {
    request.setUser(user);
    request.setEmail(user.getEmail());
    request.setPhone(user.getPhone());
    request.setAddress(user.getAddress());
    return requestRepository.save(request);
  }

  public List<ServiceRequestDto> findRequestsByUser(User user) {
    List<ServiceRequest> requests = requestRepository.findByUser(user);
    return requests.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  private ServiceRequestDto convertToDto(ServiceRequest request) {
    List<OfferDto> offerDtos = request.getOffers().stream()
        .map(offer -> new OfferDto(
            offer.getId(),
            offer.getPrice(),
            offer.getDetails(),
            offer.getCreatedDate()
        ))
        .collect(Collectors.toList());

    return new ServiceRequestDto(
        request.getId(),
        request.getTitle(),
        request.getCategory(),
        request.getUser().getUsername(),
        request.getDetails(),
        request.getCreatedDate(),
        offerDtos
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