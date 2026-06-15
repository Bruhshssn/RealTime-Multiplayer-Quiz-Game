package com.project.quizservice.controller;

import com.project.quizservice.dto.QuestionCreationDTO;
import com.project.quizservice.entity.Questions;
import com.project.quizservice.repo.QuestionRepo;
import com.project.quizservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepo questionRepo;

    @PostMapping("/createQuestion")
    public ResponseEntity<Questions> createQuiz(@RequestBody QuestionCreationDTO questionCreationDTO){
        return  new ResponseEntity<>(questionService.createQuestion(questionCreationDTO), HttpStatus.CREATED);
    }

    @GetMapping("getAllQuestions")
    public ResponseEntity<List<Questions>> getAllQuestions(){
        return new ResponseEntity<>(questionRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getQuestions/{questionId}")
    public Optional<Questions> getQuestionsByQuiz(@PathVariable Long questionId){
        return questionRepo.findById(questionId);
    }

}
