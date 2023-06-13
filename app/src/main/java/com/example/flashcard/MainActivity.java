package com.example.flashcard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

       private FlashcardDatabase flashcardDatabase;
       int current_index = 0;
        List<Flashcard> flashcardArrayList = new ArrayList<>();
        Flashcard cardToEdit = null;
        Flashcard deleteCard;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

               flashcardDatabase = new FlashcardDatabase(this);
               flashcardDatabase.initFirstCard();
               flashcardArrayList = flashcardDatabase.getAllCards();



                ImageView addBtn = findViewById(R.id.add_btn);
                ImageView editBtn = findViewById(R.id.edit_btn);
                ImageView deleteBtn = findViewById(R.id.delete_btn);
                TextView questionEdt = findViewById(R.id.Question);
                TextView answer1Edt = findViewById(R.id.Answer1);
                TextView answer2Edt = findViewById(R.id.Answer2);
                TextView answer3Edt = findViewById(R.id.Answer3);
                ImageView next_btn = findViewById(R.id.next_btn);

                Intent addCardActivityIntent = new Intent(this, AddCardActivity.class);

                if(flashcardArrayList.size() > 0){
                    questionEdt.setText(flashcardArrayList.get(0).getQuestion());
                    answer1Edt.setText(flashcardArrayList.get(0).getAnswer());
                    answer2Edt.setText(flashcardArrayList.get(0).getWrongAnswer1());
                    answer3Edt.setText(flashcardArrayList.get(0).getWrongAnswer2());
                }


                next_btn.setOnClickListener(view -> {

                    if(current_index == flashcardArrayList.size() - 1)
                    {
                        Toast.makeText(MainActivity.this, "OUPS!!! No more Card", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        current_index += 1;
                        questionEdt.setText(flashcardArrayList.get(current_index).getQuestion());
                        answer1Edt.setText(flashcardArrayList.get(current_index).getAnswer());
                        answer2Edt.setText(flashcardArrayList.get(current_index).getWrongAnswer1());
                        answer3Edt.setText(flashcardArrayList.get(current_index).getWrongAnswer2());
                        answer1Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                        answer2Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                        answer3Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                });

                ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                        Toast.makeText(MainActivity.this, "Card modifiee avec succes", Toast.LENGTH_LONG).show();

                                        String question = result.getData().getStringExtra("Question");
                                        String answer1 = result.getData().getStringExtra("answer1");
                                        String answer2 = result.getData().getStringExtra("answer2");
                                        String answer3 = result.getData().getStringExtra("answer3");

                                    if (question != null && answer1 != null && answer2 != null && answer3 != null) {
                                        cardToEdit = flashcardDatabase.getAllCards().get(current_index);
                                        cardToEdit.question = question;
                                        cardToEdit.answer = answer1;
                                        cardToEdit.wrongAnswer1 = answer2;
                                        cardToEdit.wrongAnswer2 = answer3;
                                        flashcardDatabase.updateCard(cardToEdit);
                                        flashcardArrayList = flashcardDatabase.getAllCards();
                                        questionEdt.setText(question);
                                        answer1Edt.setText(answer1);
                                        answer2Edt.setText(answer2);
                                        answer3Edt.setText(answer3);
                                    }
                                }
                        }
                );

                ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                        Toast.makeText(MainActivity.this, "Card ajoutee avec succes", Toast.LENGTH_LONG).show();

                                        String question = result.getData().getStringExtra("Question");
                                        String answer1 = result.getData().getStringExtra("answer1");
                                        String answer2 = result.getData().getStringExtra("answer2");
                                        String answer3 = result.getData().getStringExtra("answer3");

                                        questionEdt.setText(question);
                                        answer1Edt.setText(answer1);
                                        answer2Edt.setText(answer2);
                                        answer3Edt.setText(answer3);
                                    if (question != null && answer1 != null && answer2 != null && answer3 != null) {
                                        flashcardDatabase.insertCard(new Flashcard(question, answer1, answer2, answer3));
                                    }
                                }
                        }
                );

                deleteBtn.setOnClickListener(view -> {
                    deleteCard = flashcardArrayList.get(current_index);
                    flashcardDatabase.deleteCard(deleteCard.getQuestion());
                    Toast.makeText(MainActivity.this, "Card deleted!!!", Toast.LENGTH_SHORT).show();
                    next_btn.performClick();
                });

                addBtn.setOnClickListener(view -> {
                        resultLauncher.launch(addCardActivityIntent);
                });

                editBtn.setOnClickListener(view -> {
                        addCardActivityIntent.putExtra("main_question", questionEdt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer1", answer1Edt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer2", answer2Edt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer3", answer3Edt.getText().toString());
                        editLauncher.launch(addCardActivityIntent);
                });

                answer1Edt.setOnClickListener(view -> {
                        answer1Edt.setBackgroundColor(Color.parseColor("#29b449"));
                        answer2Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                        answer3Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                });

                answer2Edt.setOnClickListener(view -> {
                    answer1Edt.setBackgroundColor(Color.parseColor("#29b449"));
                    answer2Edt.setBackgroundColor(Color.parseColor("#e8364e"));
                    answer3Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                });

                answer3Edt.setOnClickListener(view -> {
                    answer1Edt.setBackgroundColor(Color.parseColor("#29b449"));
                    answer2Edt.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    answer3Edt.setBackgroundColor(Color.parseColor("#e8364e"));
                });
        }
}

