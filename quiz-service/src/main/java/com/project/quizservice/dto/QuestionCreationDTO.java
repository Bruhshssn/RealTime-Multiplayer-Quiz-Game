package com.project.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreationDTO {

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
}
