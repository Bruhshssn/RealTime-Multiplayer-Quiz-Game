package com.project.quizservice.controller;

import com.project.quizservice.dto.QuestionCreationDTO;
import com.project.quizservice.entity.Questions;
import com.project.quizservice.entity.Quiz;
import com.project.quizservice.repo.QuestionRepo;
import com.project.quizservice.repo.QuizRepo;
import com.project.quizservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class CommonController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @PostMapping("/createQuiz")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz){
        return new ResponseEntity<>(quizRepo.save(quiz), HttpStatus.CREATED);
    }

    @PostMapping("/createQuestion")
    public ResponseEntity<Questions> createQuiz(@RequestBody QuestionCreationDTO questionCreationDTO){
        return  new ResponseEntity<>(questionService.createQuestion(questionCreationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getQuestionsByQuiz/{quizId}")
    public List<Questions> getQuestionsByQuiz(@PathVariable Long quizId){
        return questionRepo.findByQuizId(quizId);
    }

}
