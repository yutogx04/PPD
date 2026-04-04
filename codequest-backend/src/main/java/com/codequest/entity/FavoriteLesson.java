package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_lessons", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "lesson_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FavoriteLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
