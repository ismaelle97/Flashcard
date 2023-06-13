package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

// AddCardActivity.java
public class AddCardActivity extends AppCompatActivity {

    private EditText questionEditText, answerEditText;
    private ImageView saveButton = null;
    private ImageView cancelBtn = null;
    private String question = "";

    private String answer = "";
    private String answer1 = "";
    private String answer2 =  "";
    private String answer3 =  "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);


        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        EditText answer2EditText = findViewById(R.id.wrongAnswer);
        EditText answer3EditText = findViewById(R.id.wrongAnswer2);
        saveButton = findViewById(R.id.saveButton);
        cancelBtn = findViewById(R.id.cancel_button);
        Intent mainActivity = getIntent();
        Intent mainActivityIntent = new Intent();

        if(mainActivity != null) {
            question = mainActivity.getStringExtra("main_question");
            answer1 = mainActivity.getStringExtra("main_answer1");
            answer2 = mainActivity.getStringExtra("main_answer2");
            answer3 = mainActivity.getStringExtra("main_answer3");

            questionEditText.setText(question);
            answerEditText.setText(answer1);
            answer2EditText.setText(answer2);
            answer3EditText.setText(answer3);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = questionEditText.getText().toString();
                answer1 = answerEditText.getText().toString();
                answer2 = answer2EditText.getText().toString();
                answer3 = answer3EditText.getText().toString();

               if (!question.isEmpty() && !answer.isEmpty()) {
                   // Do something with the user input
                   Toast.makeText(AddCardActivity.this, "Card saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                }

                if(!question.isEmpty() || !answer1.isEmpty() || !answer2.isEmpty() || !answer3.isEmpty()) {
                    mainActivityIntent.putExtra("Question", question);
                    mainActivityIntent.putExtra("answer1", answer1);
                    mainActivityIntent.putExtra("answer2", answer2);
                    mainActivityIntent.putExtra("answer3", answer3);
                    setResult(Activity.RESULT_OK, mainActivityIntent);
                    finish();
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(questionEditText, "Error : Empty Blank",
                            Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel action, such as finishing the activity or returning to the previous screen
                finish();
            }
        });

    }
}