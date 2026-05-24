package com.project.quizservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String OptionA;

    @Column(nullable = false)
    private String OptionB;

    @Column(nullable = false)
    private String OptionC;

    @Column(nullable = false)
    private String OptionD;

    @Column(nullable = false)
    private String answer;
}
