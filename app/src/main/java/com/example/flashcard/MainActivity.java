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
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        CountDownTimer countDownTimer = null;
        Flashcard deleteCard;

private void starttimer(){
    if (countDownTimer!=null){
        countDownTimer.cancel();
        countDownTimer.start();
    }
}
    @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
         countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView timerTextView = findViewById(R.id.timer);
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                View next = findViewById(R.id.next_btn);
                next.callOnClick();
                // This method is called when the countdown timer finishes
            }
        };
starttimer();
        // Initialize the FlashcardDatabase
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

        // Create an intent for AddCardActivity
                Intent addCardActivityIntent = new Intent(this, AddCardActivity.class);


        // Display the first flashcard, if available
                if(flashcardArrayList.size() > 0){
                    questionEdt.setText(flashcardArrayList.get(0).getQuestion());
                    answer1Edt.setText(flashcardArrayList.get(0).getAnswer());
                    answer2Edt.setText(flashcardArrayList.get(0).getWrongAnswer1());
                    answer3Edt.setText(flashcardArrayList.get(0).getWrongAnswer2());

                }

        // Handle "Next" button click
                next_btn.setOnClickListener(view -> {
                    if(current_index == flashcardArrayList.size() - 1)
                    {
                        Toast.makeText(MainActivity.this, "OUPS!!! No more Card", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // Increment index and update views with the next flashcard
                         Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                         Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);
                        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                // this method is called when the animation first starts
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                findViewById(R.id.Question).startAnimation(rightInAnim);
                                // this method is called when the animation is finished playing
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });
                        findViewById(R.id.Question).startAnimation(leftOutAnim);
                        starttimer();
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

        // Handle activity result for editing flashcards
                ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                        Toast.makeText(MainActivity.this, "Card modified with success", Toast.LENGTH_LONG).show();

                                    // Get the updated question and answers from the result
                                        String question = result.getData().getStringExtra("Question");
                                        String answer1 = result.getData().getStringExtra("answer1");
                                        String answer2 = result.getData().getStringExtra("answer2");
                                        String answer3 = result.getData().getStringExtra("answer3");

                                    // Update the card with the new values
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
                              starttimer();  }
                        }
                );

         // Handle activity result for adding new flashcards
                ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                        Toast.makeText(MainActivity.this, "Card added with success", Toast.LENGTH_LONG).show();


                                    // Get the new question and answers from the result
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
                               starttimer(); }
                        }
                );

        // Handle click on the delete button

        deleteBtn.setOnClickListener(view -> {
            // Get the flashcard to delete and remove it from the database
                    deleteCard = flashcardArrayList.get(current_index);
                    flashcardDatabase.deleteCard(deleteCard.getQuestion());
                    Toast.makeText(MainActivity.this, "Card deleted!!!", Toast.LENGTH_SHORT).show();
                    next_btn.performClick();
                });

        // Handle click on the add button
                addBtn.setOnClickListener(view -> {
                        resultLauncher.launch(addCardActivityIntent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                });

        // Handle click on the edit button
                editBtn.setOnClickListener(view -> {
                        addCardActivityIntent.putExtra("main_question", questionEdt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer1", answer1Edt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer2", answer2Edt.getText().toString());
                        addCardActivityIntent.putExtra("main_answer3", answer3Edt.getText().toString());
                        editLauncher.launch(addCardActivityIntent);
                });

        // Handle click on the first answer option
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

