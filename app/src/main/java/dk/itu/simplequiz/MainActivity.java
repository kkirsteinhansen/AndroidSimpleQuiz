package dk.itu.simplequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialButton quizButton, questionsButton;
    List<Question> listOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Question.initQuestions(getApplication());
        listOfQuestions = Question.getListOfQuestions();

        // Buttons
        quizButton = findViewById(R.id.quiz);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cannot start quiz if there are no questions
                if (listOfQuestions.isEmpty()) {
                    Message.show(MainActivity.this, Message.NO_QUESTIONS);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });

        questionsButton = findViewById(R.id.questions);
        questionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                startActivity(intent);
            }
        });
    }
}