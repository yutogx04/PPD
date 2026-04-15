package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "badges")
public class Badge {

    @Id
    private String id;

    // External user service — not a DB-level FK
    private String userId;

    private String type;
    private String name;
    private String description;

    private LocalDateTime earnedAt;
}
