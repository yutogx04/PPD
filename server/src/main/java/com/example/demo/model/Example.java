package com.example.demo.model;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

@Data
@Document(collection = "Example")
public class Example {
  @Id
  private String id;
}
