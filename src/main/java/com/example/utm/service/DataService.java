package com.example.utm.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// @Service kaldırıldı, abstract sınıf yapıyoruz
public abstract class DataService<T> {
  private final ObjectMapper mapper = new ObjectMapper();
  private final String filePath;

  public DataService(String fileName) {
    this.filePath = "src/main/resources/data/" + fileName;
  }

  public List<T> readAll(TypeReference<List<T>> typeRef) {
    try {
      File file = new File(filePath);
      if (!file.exists()) return new ArrayList<>();
      return mapper.readValue(file, typeRef);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeAll(List<T> data) {
    try {
      mapper.writeValue(new File(filePath), data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public T findById(UUID id, List<T> data, java.util.function.Function<T, UUID> idGetter) {
    return data.stream().filter(d -> idGetter.apply(d).equals(id)).findFirst().orElse(null);
  }
}