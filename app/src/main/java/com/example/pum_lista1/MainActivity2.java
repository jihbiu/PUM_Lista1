package com.example.pum_lista1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;

public class MainActivity2 extends AppCompatActivity {
    private TextView textViewPopUp;
    private TextView textViewAnswer;

    private Button btnGoBack;
    private Button btnCheckInternet;

    private TextView textViewPointsCounter;

    int currentQuestionCounter;
    int currentPointsCounter;
    Question activeQuestion;

    public static final String CURRENT_POINTS_COUNTER_KEY_REPLY= "currentPointsCounterREPLY";

    String questionUrl = "https://www.google.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textViewPopUp = findViewById(R.id.textViewPopUp);
        textViewAnswer = findViewById(R.id.textViewAnswer);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnCheckInternet = findViewById(R.id.btnCheckInternet);
        textViewPointsCounter = findViewById(R.id.textViewPointsCounter);

        Intent intent = getIntent();
        currentQuestionCounter = intent.getIntExtra(MainActivity.CURRENT_QUESTION_COUNTER_KEY, 0);
        currentPointsCounter = intent.getIntExtra(MainActivity.CURRENT_POINTS_COUNTER_KEY, 0);
        activeQuestion = new Question(
                intent.getStringExtra(MainActivity.CURRENT_QUESTION_KEY),
                intent.getBooleanExtra(MainActivity.CURRENT_QUESTION_ANSWER_KEY, false)
        );

        updatePointCounter();
        updateURL();
        render();



        btnGoBack.setOnClickListener(view -> {
            Intent intentReply = new Intent();
            intentReply.putExtra(CURRENT_POINTS_COUNTER_KEY_REPLY, currentPointsCounter);
            setResult(RESULT_OK, intentReply);
            finish();
        });

        btnCheckInternet.setOnClickListener(view->{
            System.out.println(questionUrl);
            Uri uri = Uri.parse(questionUrl);
            Intent intentBrowser = new Intent(Intent.ACTION_VIEW, uri);
            intentBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
            if(intentBrowser.resolveActivity(getPackageManager()) != null){
                startActivity(intentBrowser);
            }
        });
    }
    private void updatePointCounter() {
        if(currentPointsCounter > 15)
            currentPointsCounter -= 15;
        else
            currentPointsCounter = 0;
    }
    private void updateURL(){
        String questionToURL = activeQuestion.getQuestion().replaceAll(" ", "+");
        questionUrl += questionToURL;
    }

    private void render(){
        if(activeQuestion.getAnswer())
            textViewAnswer.setText("The answer is: True");
        else
            textViewAnswer.setText("The answer is: False");

        textViewPopUp.setText("You've cheated, you lost 15 points...");
        textViewPointsCounter.setText("Points:" + currentPointsCounter);
    }
}