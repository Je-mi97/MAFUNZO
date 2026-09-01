package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.QuizQuestion;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.CountDownTimer;

public class QuizActivity
        extends AppCompatActivity {

    private static final long
            SECONDS_PER_QUESTION = 30;

    private TextView tvQuestionNumber;
    private TextView tvQuestion;
    private TextView tvResult;
    private TextView tvTimer;

    private ProgressBar progressQuiz;

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

    private CountDownTimer countDownTimer;

    private long endTimeMillis = 0L;

    private boolean quizFinished = false;

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_quiz
        );

        bindViews();

        courseId =
                getIntent()
                        .getStringExtra(
                                "course_id"
                        );

        moduleId =
                getIntent()
                        .getStringExtra(
                                "module_id"
                        );

        questions =
                createQuestionsForModule(
                        moduleId
                );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_quiz
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        btnValidate.setOnClickListener(
                v -> validateAnswer()
        );

        btnRestart.setOnClickListener(
                v -> restartQuiz()
        );

        if (savedInstanceState != null) {

            currentQuestion =
                    savedInstanceState.getInt(
                            "currentQuestion",
                            0
                    );

            score =
                    savedInstanceState.getInt(
                            "score",
                            0
                    );

            endTimeMillis =
                    savedInstanceState.getLong(
                            "endTimeMillis",
                            0L
                    );

            quizFinished =
                    savedInstanceState.getBoolean(
                            "quizFinished",
                            false
                    );
        }

        if (quizFinished) {

            showResult();

        } else {

            if (endTimeMillis <= 0) {

                startNewTimer();
            }

            displayQuestion();
        }
    }

    private void bindViews() {

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

        tvTimer =
                findViewById(
                        R.id.tv_quiz_timer
                );

        progressQuiz =
                findViewById(
                        R.id.progress_quiz
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
    }

    // TIMER

    private void startNewTimer() {

        long totalSeconds =
                questions.size()
                        * SECONDS_PER_QUESTION;

        endTimeMillis =
                SystemClock.elapsedRealtime()
                        + totalSeconds * 1000L;

        startTimer();
    }

    private void startTimer() {

        cancelTimer();

        long remaining =
                endTimeMillis
                        - SystemClock.elapsedRealtime();

        if (remaining <= 0) {

            finishQuizByTimeout();

            return;
        }

        countDownTimer =
                new CountDownTimer(
                        remaining,
                        1000
                ) {

                    @Override
                    public void onTick(
                            long millisUntilFinished
                    ) {

                        updateTimer(
                                millisUntilFinished
                        );
                    }

                    @Override
                    public void onFinish() {

                        finishQuizByTimeout();
                    }
                };

        countDownTimer.start();
    }

    private void updateTimer(
            long millis
    ) {

        long totalSeconds =
                Math.max(
                        0,
                        millis / 1000
                );

        long minutes =
                totalSeconds / 60;

        long seconds =
                totalSeconds % 60;

        tvTimer.setText(
                String.format(
                        "Temps restant : %02d:%02d",
                        minutes,
                        seconds
                )
        );

        if (totalSeconds <= 30) {

            tvTimer.setTextColor(
                    getColor(
                            R.color.error
                    )
            );

        } else {

            tvTimer.setTextColor(
                    getColor(
                            R.color.green_dark
                    )
            );
        }
    }

    private void cancelTimer() {

        if (countDownTimer != null) {

            countDownTimer.cancel();

            countDownTimer = null;
        }
    }

    // QUESTIONS

    private void displayQuestion() {

        if (quizFinished
                || currentQuestion >= questions.size()) {

            showResult();

            return;
        }

        QuizQuestion question =
                questions.get(
                        currentQuestion
                );

        tvQuestionNumber.setText(
                "Question "
                        + (currentQuestion + 1)
                        + " / "
                        + questions.size()
        );

        int progress =
                (
                        (currentQuestion + 1) * 100
                ) / questions.size();

        progressQuiz.setProgress(
                progress
        );

        tvQuestion.setText(
                question.getQuestion()
        );

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

        radioAnswers.clearCheck();

        btnValidate.setText(
                currentQuestion ==
                        questions.size() - 1
                        ? "Terminer le quiz"
                        : "Valider"
        );

        tvResult.setVisibility(
                View.GONE
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

        startTimer();
    }

    private void validateAnswer() {

        if (quizFinished) {
            return;
        }

        int selectedId =
                radioAnswers
                        .getCheckedRadioButtonId();

        if (selectedId == -1) {

            tvQuestion.setText(
                    "Sélectionnez une réponse avant de continuer."
            );

            return;
        }

        int selectedAnswer = -1;

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

        QuizQuestion question =
                questions.get(
                        currentQuestion
                );

        if (selectedAnswer ==
                question.getCorrectAnswer()) {

            score++;
        }

        currentQuestion++;

        if (currentQuestion >=
                questions.size()) {

            showResult();

        } else {

            displayQuestion();
        }
    }

    // FIN PAR EXPIRATION

    private void finishQuizByTimeout() {

        if (quizFinished) {
            return;
        }

        quizFinished = true;

        cancelTimer();

        showResult();
    }

    // RÉSULTAT

    private void showResult() {

        quizFinished = true;

        cancelTimer();

        int percentage =
                questions.isEmpty()
                        ? 0
                        : (
                        score * 100
                ) / questions.size();

        radioAnswers.setVisibility(
                View.GONE
        );

        btnValidate.setVisibility(
                View.GONE
        );

        btnRestart.setVisibility(
                View.VISIBLE
        );

        tvQuestionNumber.setText(
                "Quiz terminé"
        );

        tvQuestion.setText(
                "Résultat final"
        );

        tvResult.setVisibility(
                View.VISIBLE
        );

        tvTimer.setText(
                "Temps écoulé"
        );

        if (percentage >= 60) {

            tvResult.setText(
                    "✓ Quiz réussi !\n\n"
                            + "Score : "
                            + score
                            + " / "
                            + questions.size()
                            + "\n"
                            + percentage
                            + " %\n\n"
                            + "Le module est terminé."
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

        } else {

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
    }

    // RECOMMENCER

    private void restartQuiz() {

        currentQuestion = 0;

        score = 0;

        quizFinished = false;

        endTimeMillis = 0L;

        radioAnswers.clearCheck();

        startNewTimer();

        displayQuestion();
    }

    // QUESTIONS PAR FORMATION

    private List<QuizQuestion>
    createQuestionsForModule(
            String moduleId
    ) {

        if ("M010".equals(moduleId)) {

            return createHtmlCssQuestions();
        }

        if ("M011".equals(moduleId)) {

            return createMarketingQuestions();
        }

        return createJavaQuestions();
    }

    private List<QuizQuestion>
    createJavaQuestions() {

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

    private List<QuizQuestion>
    createHtmlCssQuestions() {

        List<QuizQuestion> list =
                new ArrayList<>();

        list.add(
                new QuizQuestion(
                        "Quel langage décrit principalement la structure d'une page Web ?",
                        Arrays.asList(
                                "HTML",
                                "CSS",
                                "SQL",
                                "XML"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Quel langage permet principalement de modifier l'apparence d'une page ?",
                        Arrays.asList(
                                "Java",
                                "CSS",
                                "SQL",
                                "PHP"
                        ),
                        1
                )
        );

        list.add(
                new QuizQuestion(
                        "Quelle balise permet de créer un paragraphe ?",
                        Arrays.asList(
                                "<div>",
                                "<p>",
                                "<h1>",
                                "<span>"
                        ),
                        1
                )
        );

        list.add(
                new QuizQuestion(
                        "Quelle propriété CSS permet de changer la couleur du texte ?",
                        Arrays.asList(
                                "background-color",
                                "font-size",
                                "color",
                                "text-style"
                        ),
                        2
                )
        );

        list.add(
                new QuizQuestion(
                        "Quelle technologie permet d'adapter une interface aux différentes tailles d'écran ?",
                        Arrays.asList(
                                "Responsive design",
                                "FTP",
                                "DNS",
                                "SQL"
                        ),
                        0
                )
        );

        return list;
    }

    private List<QuizQuestion>
    createMarketingQuestions() {

        List<QuizQuestion> list =
                new ArrayList<>();

        list.add(
                new QuizQuestion(
                        "Que faut-il définir avant de construire une stratégie marketing ?",
                        Arrays.asList(
                                "La cible",
                                "Le mot de passe",
                                "Le système d'exploitation",
                                "Le câble réseau"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Quel est le rôle d'une proposition de valeur ?",
                        Arrays.asList(
                                "Expliquer le bénéfice apporté par l'offre",
                                "Créer un mot de passe",
                                "Installer une application",
                                "Modifier un fichier"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Que signifie convertir un utilisateur ?",
                        Arrays.asList(
                                "Le supprimer",
                                "Le pousser à réaliser l'action recherchée",
                                "Changer son téléphone",
                                "Changer son navigateur"
                        ),
                        1
                )
        );

        list.add(
                new QuizQuestion(
                        "Pourquoi mesurer les résultats d'une campagne ?",
                        Arrays.asList(
                                "Pour améliorer la stratégie",
                                "Pour supprimer les clients",
                                "Pour changer de système",
                                "Pour empêcher les clics"
                        ),
                        0
                )
        );

        list.add(
                new QuizQuestion(
                        "Quel est l'un des objectifs de la fidélisation ?",
                        Arrays.asList(
                                "Faire revenir les utilisateurs",
                                "Bloquer l'utilisateur",
                                "Supprimer les contenus",
                                "Réduire la visibilité"
                        ),
                        0
                )
        );

        return list;
    }

    // SAUVEGARDE D'ÉTAT

    @Override
    protected void onSaveInstanceState(
            @Nullable Bundle outState
    ) {

        outState.putInt(
                "currentQuestion",
                currentQuestion
        );

        outState.putInt(
                "score",
                score
        );

        outState.putLong(
                "endTimeMillis",
                endTimeMillis
        );

        outState.putBoolean(
                "quizFinished",
                quizFinished
        );

        super.onSaveInstanceState(
                outState
        );
    }

    @Override
    protected void onDestroy() {

        cancelTimer();

        super.onDestroy();
    }
}