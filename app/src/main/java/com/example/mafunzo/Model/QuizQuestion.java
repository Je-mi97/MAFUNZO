package com.example.mafunzo.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestion implements Serializable {

    private final String question;
    private final List<String> answers;
    private final int correctAnswer;

    public QuizQuestion(
            String question,
            List<String> answers,
            int correctAnswer
    ) {
        this.question = question;
        this.answers = new ArrayList<>(answers);
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}