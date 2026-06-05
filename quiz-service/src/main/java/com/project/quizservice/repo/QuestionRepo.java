package com.project.quizservice.repo;

import com.project.quizservice.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<Questions, Integer> {
    List<Questions> findByQuizId(long quizId);
}

