package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Example;

@Repository
public interface ExampleRepository extends MongoRepository<Example, String> {

  public List<Example> findAll();

  public Example insert(Example example);

  public Example save(Example example);

  public void deleteById(String id);

}
