package com.project.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreationDTO {

    private String question;
    private String OptionA;
    private String OptionB;
    private String OptionC;
    private String OptionD;
    private String answer;
}
