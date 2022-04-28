package dk.itu.simplequiz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class QuizActivity extends AppCompatActivity {

    private FragmentManager fM;
    private Fragment scoreFragment;
    private Fragment questionFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        fM = getSupportFragmentManager();
        setUpFragments();
    }

    private void setUpFragments() {
        scoreFragment = fM.findFragmentById(R.id.score_container);
        questionFragment = fM.findFragmentById(R.id.question_container);
        if (scoreFragment == null && questionFragment == null) {
            scoreFragment = new ScoreFragment();
            questionFragment = new QuestionFragment();
            fM.beginTransaction()
                    .add(R.id.score_container, new ScoreFragment())
                    .add(R.id.question_container, new QuestionFragment())
                    .commit();
        }
    }
}
