package com.project.quizservice.repo;

import com.project.quizservice.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepo extends JpaRepository<Quiz, Integer> {
}
