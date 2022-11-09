package com.example.pum_lista1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private QuestionReader questionReader;
    private ArrayList<Integer> questionQueue = new ArrayList<>();

    private TextView textViewQuestion;
    private TextView textViewPointsCounter;
    private TextView textViewPopUp;
    private Button buttonAnswerTrue;
    private Button buttonAnswerFalse;
    private Button buttonCheat;

    private int currentQuestionCounter = 0;
    private int currentPointsCounter = 0;

    private boolean isGameOver = false;

    private Question activeQuestion;

    public static final String CURRENT_QUESTION_COUNTER_KEY = "currentQuestionCounterMSG";
    public static final String CURRENT_POINTS_COUNTER_KEY= "currentPointsCounterMSG";
    public static final String CURRENT_QUESTION_ANSWER_KEY = "currentQuestionAnswerMSG";
    public static final String CURRENT_QUESTION_KEY = "currentQuestionMSG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewPointsCounter = findViewById(R.id.textViewPointsCounter);
        textViewPopUp = findViewById(R.id.textViewPopUp);
        buttonAnswerTrue = findViewById(R.id.btnAnswerTrue);
        buttonAnswerFalse = findViewById(R.id.btnAnswerFalse);
        buttonCheat = findViewById(R.id.btnCheat);

        questionReader = new QuestionReader(this,"Questions.txt");
        this.initQuestionQueue();

        initQuestion();
        render();

        buttonAnswerTrue.setOnClickListener(view -> checkAnswer(true));
        buttonAnswerFalse.setOnClickListener(view -> checkAnswer(false));
        buttonCheat.setOnClickListener(view ->{
            buttonCheatEvent();
        });
    }

    private void checkAnswer(boolean btnPressedValue){
        update(questionReader.getQuestion(questionQueue.get(currentQuestionCounter)).getAnswer() == btnPressedValue);
    }
    private void initQuestionQueue(){
        for(int i = 0; i < questionReader.getLength(); i++){
            questionQueue.add(i);
        }
        Collections.shuffle(questionQueue);
    }

    private void update(boolean userGuessedCorrect){
        if(userGuessedCorrect) {
            textViewPopUp.setText("Good answer!");
            currentPointsCounter += 10;
            currentQuestionCounter++;
        }
        else {
            textViewPopUp.setText("Wrong answer!\n(start from the beginning)");
            isGameOver = true;
        }

        initQuestion();
        render();
    }

    private void render() {
        textViewQuestion.setText(activeQuestion.getQuestion());
        textViewPointsCounter.setText("Points:" + currentPointsCounter);
    }
    private void initQuestion() {
        if(currentPointsCounter >= 10 * questionReader.getLength()){
            textViewPopUp.setText("You've won, begin the quiz from beginning!");
        }
        else if (currentQuestionCounter < questionReader.getLength())
                activeQuestion = questionReader.getQuestion(questionQueue.get(currentQuestionCounter));

        if (currentQuestionCounter >= questionReader.getLength() || isGameOver) {
            currentQuestionCounter = 0;
            currentPointsCounter = 0;
            isGameOver = false;
            initQuestionQueue();
            initQuestion();
        }
        render();
    }

    private void buttonCheatEvent(){
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra(CURRENT_QUESTION_COUNTER_KEY, currentQuestionCounter);
        intent.putExtra(CURRENT_POINTS_COUNTER_KEY, currentPointsCounter);
        intent.putExtra(CURRENT_QUESTION_ANSWER_KEY, activeQuestion.getAnswer());
        intent.putExtra(CURRENT_QUESTION_KEY, activeQuestion.getQuestion());
        secondActivityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> secondActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    currentPointsCounter = result.getData().getIntExtra(MainActivity2.CURRENT_POINTS_COUNTER_KEY_REPLY, 0);
                    textViewPopUp.setText("Let's continue the game" );
                    render();
                }
            }
    );
}