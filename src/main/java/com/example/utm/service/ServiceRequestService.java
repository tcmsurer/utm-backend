package com.example.utm.service;

import com.example.utm.model.ServiceRequest;
import com.example.utm.model.User;
import com.example.utm.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

  private final ServiceRequestRepository requestRepository;

  public List<ServiceRequest> findAllRequests() {
    return requestRepository.findAll();
  }

  public ServiceRequest createRequestForUser(ServiceRequest request, User user) {
    request.setUser(user); // Talebin sahibini belirle

    // DİKKAT: EKSİK OLAN VE HATAYI ÇÖZEN KISIM BURASI
    // Kullanıcının profilindeki iletişim bilgilerini talep anındaki
    // "snapshot" olarak talebe kopyalıyoruz.
    request.setEmail(user.getEmail());
    request.setPhone(user.getPhone());
    request.setAddress(user.getAddress());

    return requestRepository.save(request);
  }

  public List<ServiceRequest> findRequestsByUser(User user) {
    return requestRepository.findByUser(user);
  }

  public void verifyRequestOwner(UUID requestId, String username) {
    ServiceRequest request = requestRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

    if (!request.getUser().getUsername().equals(username)) {
      throw new AccessDeniedException("You do not have permission to access this request.");
    }
  }
}