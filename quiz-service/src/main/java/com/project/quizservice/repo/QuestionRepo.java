package com.project.quizservice.repo;

import com.project.quizservice.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Questions, Integer> {
}

