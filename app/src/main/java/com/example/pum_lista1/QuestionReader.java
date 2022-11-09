package com.example.pum_lista1;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionReader {


    private List<Question> questionList = new ArrayList<Question>();
    public QuestionReader(Context context, String pathName) {
        Context mContext = context;
        BufferedReader reader = null;

       try {
           reader = new BufferedReader(
                   new InputStreamReader(context.getAssets().open("Questions.txt")));

           String line;
           while((line = reader.readLine()) != null) {
               addToQuestionList(line);
           }
       }
       catch(FileNotFoundException e) {
           Log.e("com.example.pum_list1.QuestionReader","Couldn't find file", e);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
    public Question getQuestion(int index){
        return questionList.get(index);
    }
    public int getLength() {return questionList.size();}

    private void addToQuestionList(String strLine){
        if(!strLine.isEmpty()) {
            Boolean boolFileAnswer = strLine.charAt(0) != '0';
            String strFileQuestion = strLine.substring(1);
            questionList.add(new Question(strFileQuestion, boolFileAnswer));
        }
    }

}
