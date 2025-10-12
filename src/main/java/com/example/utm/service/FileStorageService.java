package com.example.utm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

  private final Path fileStorageLocation;

  // **NOTE:** The constructor has been updated
  public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
    // We now save files to the user's home directory instead of next to the project.
    // This prevents permission errors.
    Path userHome = Paths.get(System.getProperty("user.home"));
    this.fileStorageLocation = userHome.resolve(uploadDir).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public String storeFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new RuntimeException("You must select a file to upload.");
    }

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileExtension = "";
    try {
      fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    } catch(Exception e) {
      // Can be left empty if we allow files without extensions
    }
    String newFileName = UUID.randomUUID().toString() + fileExtension;

    try {
      Path targetLocation = this.fileStorageLocation.resolve(newFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return newFileName;
    } catch (IOException ex) {
      throw new RuntimeException("Could not save file: " + newFileName, ex);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        return resource;
      } else {
        throw new RuntimeException("File not found: " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new RuntimeException("File not found: " + fileName, ex);
    }
  }
}