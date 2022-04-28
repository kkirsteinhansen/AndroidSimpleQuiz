package dk.itu.simplequiz;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class QuestionFragment extends Fragment {

    private PlayerViewModel livePlayers;
    private QuestionsViewModel liveQuestions; // For end early option
    private TextView currentQuestion;
    private RadioGroup[] radioGroups;
    private List<Question> listOfQuestions;
    private List<Player> listOfPlayers;
    private int index; // Index for accessing individual questions in a list
    private Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        livePlayers = new ViewModelProvider(requireActivity())
                .get(PlayerViewModel.class);
        liveQuestions = new ViewModelProvider(requireActivity())
                .get(QuestionsViewModel.class);

        index = 0;
        listOfQuestions = Question.initQuestions();
        listOfPlayers = Player.initPlayers();
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        currentQuestion = v.findViewById(R.id.current_question);
        currentQuestion.setText(listOfQuestions.get(index).getQuestionString());

        // TextViews for player names
        TextView p1 = v.findViewById(R.id.p1);
        TextView p2 = v.findViewById(R.id.p2);
        TextView p3 = v.findViewById(R.id.p3);
        TextView p4 = v.findViewById(R.id.p4);
        TextView[] textViews = {p1, p2, p3, p4};
        for (int i = 0; i < listOfPlayers.size(); i++)
            textViews[i].setText(listOfPlayers.get(i).getName());

        // RadioGroups for player answers
        RadioGroup rg1 = v.findViewById(R.id.rg_p1);
        RadioGroup rg2 = v.findViewById(R.id.rg_p2);
        RadioGroup rg3 = v.findViewById(R.id.rg_p3);
        RadioGroup rg4 = v.findViewById(R.id.rg_p4);
        radioGroups = new RadioGroup[] {rg1, rg2, rg3, rg4};
        for (RadioGroup rg : radioGroups) rg.setVisibility(View.INVISIBLE);
        for (int i = 0; i < listOfPlayers.size(); i++)
            radioGroups[i].setVisibility(View.VISIBLE);

        // Buttons
        Button submitButton = v.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonChecked()) {
                    Display.message(context, Display.MISSING_ANSWER);
                    return;
                }
                checkAnswers();
                nextQuestion();
            }
        });
        return v;
    }

    private void checkAnswers() {
        boolean correctAnswer = listOfQuestions.get(index).getBooleanAnswer();
        for (int i = 0; i < listOfPlayers.size(); i++) {
            int buttonId = radioGroups[i].getCheckedRadioButtonId();
            boolean playerAnswer = findCheckedButton(i, buttonId);
            if (playerAnswer == correctAnswer) livePlayers.addPoint(listOfPlayers.get(i));
        }
    }

    private void nextQuestion() {
        index++;
        if (index < listOfQuestions.size()) {
            currentQuestion.setText(listOfQuestions.get(index).getQuestionString());
            for (RadioGroup rg : radioGroups) rg.clearCheck();
            return;
        } showResults();
    }

    private void showResults() {
        Display.message(context, Display.GETTING_RESULTS);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, ResultsActivity.class);
                startActivity(intent);
            }}, 2000);
    }

    private boolean buttonChecked() {
        for (int i = 0; i < listOfPlayers.size(); i++) {
            int id = radioGroups[i].getCheckedRadioButtonId();
            if (id == -1) return false;
        } return true;
    }

    private boolean findCheckedButton(int player, int id) {
        switch (player) {
            case 0: return id == R.id.p1_true;
            case 1: return id == R.id.p2_true;
            case 2: return id == R.id.p3_true;
            case 3: return id == R.id.p4_true;
        } return false; // Will never reach this point
    }
}
