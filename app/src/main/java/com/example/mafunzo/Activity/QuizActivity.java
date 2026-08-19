package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.QuizQuestion;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private TextView tvResult;

    private RadioGroup radioAnswers;

    private RadioButton rbAnswer1;
    private RadioButton rbAnswer2;
    private RadioButton rbAnswer3;
    private RadioButton rbAnswer4;

    private MaterialButton btnValidate;
    private MaterialButton btnRestart;

    private List<QuizQuestion> questions;

    private int currentQuestion = 0;
    private int score = 0;

    private String courseId;
    private String moduleId;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_quiz
        );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_quiz
                );

        tvQuestionNumber =
                findViewById(
                        R.id.tv_question_number
                );

        tvQuestion =
                findViewById(
                        R.id.tv_question
                );

        tvResult =
                findViewById(
                        R.id.tv_quiz_result
                );

        radioAnswers =
                findViewById(
                        R.id.radio_answers
                );

        rbAnswer1 =
                findViewById(
                        R.id.rb_answer_1
                );

        rbAnswer2 =
                findViewById(
                        R.id.rb_answer_2
                );

        rbAnswer3 =
                findViewById(
                        R.id.rb_answer_3
                );

        rbAnswer4 =
                findViewById(
                        R.id.rb_answer_4
                );

        btnValidate =
                findViewById(
                        R.id.btn_validate_answer
                );

        btnRestart =
                findViewById(
                        R.id.btn_restart_quiz
                );

        courseId =
                getIntent().getStringExtra(
                        "course_id"
                );

        moduleId =
                getIntent().getStringExtra(
                        "module_id"
                );

        questions =
                createQuestions();

        btnBack.setOnClickListener(
                v -> finish()
        );

        btnValidate.setOnClickListener(
                v -> validateAnswer()
        );

        btnRestart.setOnClickListener(
                v -> restartQuiz()
        );

        displayQuestion();
    }

    private void displayQuestion() {

        if (currentQuestion >= questions.size()) {

            showResult();

            return;
        }

        // Afficher les éléments du quiz

        tvQuestionNumber.setVisibility(
                View.VISIBLE
        );

        tvQuestion.setVisibility(
                View.VISIBLE
        );

        radioAnswers.setVisibility(
                View.VISIBLE
        );

        btnValidate.setVisibility(
                View.VISIBLE
        );

        btnRestart.setVisibility(
                View.GONE
        );

        tvResult.setVisibility(
                View.GONE
        );

        // Récupérer la question actuelle

        QuizQuestion question =
                questions.get(
                        currentQuestion
                );

        // Numéro de question

        tvQuestionNumber.setText(
                "Question "
                        + (currentQuestion + 1)
                        + " / "
                        + questions.size()
        );

        // Texte de la question

        tvQuestion.setText(
                question.getQuestion()
        );

        // Réponses

        List<String> answers =
                question.getAnswers();

        rbAnswer1.setText(
                answers.get(0)
        );

        rbAnswer2.setText(
                answers.get(1)
        );

        rbAnswer3.setText(
                answers.get(2)
        );

        rbAnswer4.setText(
                answers.get(3)
        );

        // Réinitialiser la sélection

        radioAnswers.clearCheck();

        // Texte du bouton

        if (currentQuestion ==
                questions.size() - 1) {

            btnValidate.setText(
                    "Terminer le quiz"
            );

        } else {

            btnValidate.setText(
                    "Valider"
            );
        }
    }

    private void validateAnswer() {

        int selectedId =
                radioAnswers.getCheckedRadioButtonId();

        // Aucune réponse sélectionnée

        if (selectedId == -1) {

            return;
        }

        int selectedAnswer = -1;

        // Identifier la réponse choisie

        if (selectedId ==
                rbAnswer1.getId()) {

            selectedAnswer = 0;

        } else if (selectedId ==
                rbAnswer2.getId()) {

            selectedAnswer = 1;

        } else if (selectedId ==
                rbAnswer3.getId()) {

            selectedAnswer = 2;

        } else if (selectedId ==
                rbAnswer4.getId()) {

            selectedAnswer = 3;
        }

        // Question actuelle

        QuizQuestion question =
                questions.get(
                        currentQuestion
                );

        // Vérification

        if (selectedAnswer ==
                question.getCorrectAnswer()) {

            score++;
        }

        // Question suivante

        currentQuestion++;

        displayQuestion();
    }

    private void showResult() {

        int percentage =
                (score * 100)
                        / questions.size();

        tvQuestionNumber.setVisibility(
                View.VISIBLE
        );

        tvQuestionNumber.setText(
                "Quiz terminé"
        );

        tvQuestion.setVisibility(
                View.VISIBLE
        );

        tvQuestion.setText(
                "Résultat"
        );

        radioAnswers.setVisibility(
                View.GONE
        );

        btnValidate.setVisibility(
                View.GONE
        );

        tvResult.setVisibility(
                View.VISIBLE
        );

        boolean passed =
                percentage >= 60;

        if (passed) {

            tvResult.setText(
                    "✓ Félicitations !\n\n"
                            + "Score : "
                            + score
                            + " / "
                            + questions.size()
                            + "\n"
                            + percentage
                            + " %\n\n"
                            + "Vous avez réussi le quiz."
            );

            tvResult.setTextColor(
                    getColor(
                            R.color.success
                    )
            );

            if (courseId != null
                    && moduleId != null) {

                ProgressManager progressManager =
                        new ProgressManager(this);

                progressManager.notifyQuizPassed(
                        courseId,
                        moduleId,
                        percentage
                );
            }

        }

        else {

            tvResult.setText(
                    "✗ Quiz non réussi\n\n"
                            + "Score : "
                            + score
                            + " / "
                            + questions.size()
                            + "\n"
                            + percentage
                            + " %\n\n"
                            + "Il faut au moins 60 % pour valider le module."
            );

            tvResult.setTextColor(
                    getColor(
                            R.color.error
                    )
            );
        }

        btnRestart.setVisibility(
                View.VISIBLE
        );

        btnRestart.setText(
                "Recommencer"
        );
    }

    private void restartQuiz() {

        currentQuestion = 0;

        score = 0;

        displayQuestion();
    }

    private List<QuizQuestion> createQuestions() {

        List<QuizQuestion> list =
                new ArrayList<>();

        list.add(
                new QuizQuestion(
                        "Quel mot-clé permet de déclarer une classe en Java ?",
                        Arrays.asList(
                                "class",
                                "define",
                                "struct",
                                "object"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Quel type permet de stocker un nombre entier ?",
                        Arrays.asList(
                                "String",
                                "boolean",
                                "int",
                                "double"
                        ),
                        2
                )
        );

        list.add(
                new QuizQuestion(
                        "Quel symbole termine généralement une instruction Java ?",
                        Arrays.asList(
                                ":",
                                ";",
                                ".",
                                ","
                        ),
                        1
                )
        );

        list.add(
                new QuizQuestion(
                        "Quelle structure permet de tester une condition ?",
                        Arrays.asList(
                                "if",
                                "for",
                                "import",
                                "package"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Quelle boucle est adaptée lorsqu'on connaît le nombre d'itérations ?",
                        Arrays.asList(
                                "if",
                                "while",
                                "for",
                                "switch"
                        ),
                        2
                )
        );

        return list;
    }
}