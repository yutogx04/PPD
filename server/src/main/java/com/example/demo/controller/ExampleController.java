package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Example;
import com.example.demo.service.*;

@RestController
@RequestMapping("/Examples")
public class ExampleController {

  private final ExampleService exampleService;

  public ExampleController(ExampleService exampleService) {
    this.exampleService = exampleService;
  }

  @GetMapping
  public List<Example> fetchExamples() {
    List<Example> examples = exampleService.fetchExamples();
    for (Example example : examples) {
      System.out.println(example.getId());
    }
    System.out.println();
    return examples;
  }

  @PostMapping("/insert")
  public void insertItem(@RequestBody Example example) {
    System.out.println("\n\nInserting item: " + example + "\n\n");
    exampleService.insertExample(example);
  }

  @PostMapping("/update")
  public void updateItem(@RequestBody Example example) {
    System.out.println("\n\nUpdating item: " + example + "\n\n");
    exampleService.updateExample(example);
  }

  @DeleteMapping("/{id}")
  public void deleteItem(@PathVariable String id) {
    System.out.println("\n\nDeleting item with ID: " + id + "\n\n");
    exampleService.deleteExample(id);
  }
}
