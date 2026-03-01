package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import java.util.List;

@Service
public class ExampleService {
  private final ExampleRepository exampleRepository;

  public ExampleService(ExampleRepository exampleRepository) {
    this.exampleRepository = exampleRepository;
  }

  public List<Example> fetchExamples() {
    return exampleRepository.findAll();
  }

  public void deleteExample(String id) {
    exampleRepository.deleteById(id);
  }

  public void insertExample(Example example) {
    example.setId(null);
    exampleRepository.insert(example);
  }

  public void updateExample(Example example) {
    exampleRepository.save(example);
  }
}
