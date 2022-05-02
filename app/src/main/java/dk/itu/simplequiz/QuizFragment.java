package dk.itu.simplequiz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuizFragment extends Fragment {

    private PlayerViewModel livePlayers;
    private TextView currentQuestion;
    private RadioGroup[] radioGroups;
    private List<Question> listOfQuestions;
    private List<Player> listOfPlayers;
    private int index; // Index for accessing individual questions in a list
    private Context context;
    private TextView[] playerNames;
    private TextView[] playerScores;
    private int numOfPlayers;
    private TextView remainingNum;
    private TextView remainingString;
    private MaterialButton endEarlyButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        livePlayers = new ViewModelProvider(requireActivity())
                .get(PlayerViewModel.class);

        index = 0;
        context = getContext();

        listOfPlayers = Player.initPlayers();
        Question.initQuestions(context);
        listOfQuestions = Question.getListOfQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View quiz = inflater.inflate(R.layout.fragment_quiz, container, false);

        currentQuestion = quiz.findViewById(R.id.current_question);
        currentQuestion.setText(listOfQuestions.get(index).getQuestionString());

        remainingNum = quiz.findViewById(R.id.remaining_questions_num);
        remainingString = quiz.findViewById(R.id.remaining_questions_text);
        remainingNum.setText(getRemainingNum());
        remainingString.setText(getRemainingString());


        // TextViews for player names
        TextView p1 = quiz.findViewById(R.id.p1);
        TextView p2 = quiz.findViewById(R.id.p2);
        TextView p3 = quiz.findViewById(R.id.p3);
        TextView p4 = quiz.findViewById(R.id.p4);
        TextView[] textViews = {p1, p2, p3, p4};
        for (int i = 0; i < listOfPlayers.size(); i++)
            textViews[i].setText(listOfPlayers.get(i).getName());

        // RadioGroups for player answers
        RadioGroup rg1 = quiz.findViewById(R.id.rg_p1);
        RadioGroup rg2 = quiz.findViewById(R.id.rg_p2);
        RadioGroup rg3 = quiz.findViewById(R.id.rg_p3);
        RadioGroup rg4 = quiz.findViewById(R.id.rg_p4);
        radioGroups = new RadioGroup[] {rg1, rg2, rg3, rg4};
        for (int i = 0; i < listOfPlayers.size(); i++)
            radioGroups[i].setVisibility(View.VISIBLE);

        TextView p1_name = quiz.findViewById(R.id.p1_name_sc);
        TextView p2_name = quiz.findViewById(R.id.p2_name_sc);
        TextView p3_name = quiz.findViewById(R.id.p3_name_sc);
        TextView p4_name = quiz.findViewById(R.id.p4_name_sc);
        playerNames = new TextView[] {p1_name, p2_name, p3_name, p4_name};

        TextView p1_score = quiz.findViewById(R.id.p1_score_sc);
        TextView p2_score = quiz.findViewById(R.id.p2_score_sc);
        TextView p3_score = quiz.findViewById(R.id.p3_score_sc);
        TextView p4_score = quiz.findViewById(R.id.p4_score_sc);
        playerScores = new TextView[] {p1_score, p2_score, p3_score, p4_score};

        numOfPlayers = Player.getListOfPlayers().size();
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE); }

        updateScores();
        livePlayers.getObservable().observe(getViewLifecycleOwner(),
                scores -> updateScores());

        // Buttons
        MaterialButton getResultsButton = quiz.findViewById(R.id.finish);
        MaterialButton nextQuestionButton = quiz.findViewById(R.id.next_question);
        endEarlyButton = quiz.findViewById(R.id.end_early);

        // If there is only one question
        if (listOfQuestions.size() == 1) {
            nextQuestionButton.setVisibility(View.GONE);
            getResultsButton.setVisibility(View.VISIBLE);
            endEarlyButton.setVisibility(View.INVISIBLE);
            remainingNum.setVisibility(View.INVISIBLE);
            remainingString.setVisibility(View.INVISIBLE);

        }

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonChecked()) {
                    Message.show(context, Message.MISSING_ANSWER);
                    return; }
                checkAnswers();
                if (secondToLastQuestion()) {
                    nextQuestionButton.setVisibility(View.GONE);
                    getResultsButton.setVisibility(View.VISIBLE); }
                getNextQuestion();
            }
        });

        // Buttons

        getResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonChecked()) {
                    Message.show(context, Message.MISSING_ANSWER);
                    return;
                }
                checkAnswers();
                Navigation.findNavController(view).navigate(R.id.action_quizFragment_to_resultsFragment);
            }
        });

        endEarlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonChecked()) {
                    Message.show(context, Message.MISSING_ANSWER);
                    return;
                }
                checkAnswers();
                Navigation.findNavController(view).navigate(R.id.action_quizFragment_to_resultsFragment);
            }
        });

        return quiz;
    }

    private void checkAnswers() {
        boolean correctAnswer = listOfQuestions.get(index).getBooleanAnswer();
        for (int i = 0; i < listOfPlayers.size(); i++) {
            int buttonId = radioGroups[i].getCheckedRadioButtonId();
            boolean playerAnswer = findCheckedButton(i, buttonId);
            if (playerAnswer == correctAnswer) livePlayers.addPoint(listOfPlayers.get(i));
        }
        for (RadioGroup rg : radioGroups) rg.clearCheck();
    }

    private void getNextQuestion() {
        index++;
        currentQuestion.setText(listOfQuestions.get(index).getQuestionString());
        remainingNum.setText(getRemainingNum());
        remainingString.setText(getRemainingString());
        if (index == listOfQuestions.size()-1) {
            endEarlyButton.setVisibility(View.INVISIBLE);
            remainingNum.setVisibility(View.INVISIBLE);
            remainingString.setVisibility(View.INVISIBLE);
        }
    }

    private String getRemainingNum() {
        int num = listOfQuestions.size()-1-index;
        return "" + num;
    }

    private String getRemainingString() {
        if (listOfQuestions.size()-1-index > 1) return "  questions remaining";
        return "  question remaining";
    }

    private boolean secondToLastQuestion() {
        return index == listOfQuestions.size()-2;
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

    private void updateScores() {
        List<Player> players = Player.getListOfPlayers();
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setText(players.get(i).getName());
            playerScores[i].setText((players.get(i).getScoreString()));
        }
    }
}
