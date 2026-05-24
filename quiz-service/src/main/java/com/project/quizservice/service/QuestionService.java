package com.project.quizservice.service;

import com.project.quizservice.dto.QuestionCreationDTO;
import com.project.quizservice.entity.Questions;
import com.project.quizservice.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    public Questions createQuestion(@RequestBody QuestionCreationDTO questionCreationDTO){
        Questions questions = new Questions();

        questions.setQuestion(questionCreationDTO.getQuestion());
        questions.setOptionA(questionCreationDTO.getOptionA());
        questions.setOptionB(questionCreationDTO.getOptionB());
        questions.setOptionC(questionCreationDTO.getOptionC());
        questions.setOptionD(questionCreationDTO.getOptionD());
        questions.setAnswer(questionCreationDTO.getAnswer());
        return questionRepo.save(questions);
    }
}
