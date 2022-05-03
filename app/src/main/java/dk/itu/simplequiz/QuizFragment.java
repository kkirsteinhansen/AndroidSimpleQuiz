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

    private Context context;
    private PlayerViewModel livePlayers;

    private List<Question> listOfQuestions;
    private List<Player> listOfPlayers;

    private TextView currentQuestion, remainingNum, remainingString;
    private TextView[] playerNames, playerScores;
    private RadioGroup[] radioGroups;

    private MaterialButton endEarlyButton, nextQuestionButton, getResultsButton;

    private int index;
    private int numOfPlayers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext(); // Save context in variable for easy use
        index = 0; // For accessing individual questions in a list
        numOfPlayers = Player.getListOfPlayers().size();

        // Get LiveData
        livePlayers = new ViewModelProvider(requireActivity())
                .get(PlayerViewModel.class);

        // Get list of players for counting purposes
        // (LiveData is unnecessary for this purpose)
        listOfPlayers = Player.initPlayers();

        // Needed in case the user navigates straight to the quiz
        Question.initQuestions(context);

        listOfQuestions = Question.getListOfQuestions(); // Get questions
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View quiz = inflater
                .inflate(R.layout.fragment_quiz, container, false);

        // TextViews
        currentQuestion = quiz.findViewById(R.id.current_question);
        remainingNum = quiz.findViewById(R.id.remaining_questions_num);
        remainingString = quiz.findViewById(R.id.remaining_questions_text);

        TextView p1 = quiz.findViewById(R.id.p1);
        TextView p2 = quiz.findViewById(R.id.p2);
        TextView p3 = quiz.findViewById(R.id.p3);
        TextView p4 = quiz.findViewById(R.id.p4);
        TextView[] textViews = {p1, p2, p3, p4};
        for (int i = 0; i < listOfPlayers.size(); i++)
            textViews[i].setText(listOfPlayers.get(i).getName());

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

        // Ensure that the number of fields corresponds to the number of players
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE); }

        getQuestion();     // Set the initial contents of the TextViews
        updateScores();    // Get the initial scores
        livePlayers.getObservable().observe(getViewLifecycleOwner(),
                scores -> updateScores());

        // Buttons
        getResultsButton = quiz.findViewById(R.id.finish);
        nextQuestionButton = quiz.findViewById(R.id.next_question);
        endEarlyButton = quiz.findViewById(R.id.end_early);

        // If there is only one question, do not show 'End early' button etc.
        checkVisibility();

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
                getNextQuestion(); }
        });

        getResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonChecked()) {
                    Message.show(context, Message.MISSING_ANSWER);
                    return; }
                checkAnswers();
                Navigation.findNavController(view)
                        .navigate(R.id.action_quizFragment_to_resultsFragment); }
        });

        endEarlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_quizFragment_to_resultsFragment); }
        });

        return quiz;
    }

    /**
     * This method checks the players' answers and add points for correct ones.
     */
    private void checkAnswers() {
        boolean correctAnswer = listOfQuestions.get(index).getBooleanAnswer();
        for (int i = 0; i < listOfPlayers.size(); i++) {
            int buttonId = radioGroups[i].getCheckedRadioButtonId();
            boolean playerAnswer = findCheckedButton(i, buttonId);
            if (playerAnswer == correctAnswer) livePlayers.addPoint(listOfPlayers.get(i));
        }
        for (RadioGroup rg : radioGroups) rg.clearCheck();
    }

    /**
     * This method gets the next question from the list of questions.
     */
    private void getNextQuestion() {
        index++;
        getQuestion();
        checkVisibility();
    }

    /**
     * This method modifies the visibility of different UI elements when the
     * current question is the last question in the list.
     */
    private void checkVisibility() {
        if (index == listOfQuestions.size()-1) {
            nextQuestionButton.setVisibility(View.GONE);
            getResultsButton.setVisibility(View.VISIBLE);
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

    /**
     * This method checks whether each player has checked a radio button in response
     * to the current question.
     * @return true if each user has checked a button, false otherwise
     */
    private boolean buttonChecked() {
        for (int i = 0; i < listOfPlayers.size(); i++) {
            int id = radioGroups[i].getCheckedRadioButtonId();
            if (id == -1) return false;
        } return true;
    }

    /**
     * This method connects each RadioGroup to a specific player in order to determine
     * the player's answer to the current question.
     * @param player the current player represented by their index in the list of players
     * @param id the RadioButton id
     * @return true if the id corresponds to the 'true' button, false otherwise
     */
    private boolean findCheckedButton(int player, int id) {
        switch (player) {
            case 0: return id == R.id.p1_true;
            case 1: return id == R.id.p2_true;
            case 2: return id == R.id.p3_true;
            case 3: return id == R.id.p4_true;
        } return false; // Will never reach this point
    }

    /**
     * This method retrieves the current scores of each player and puts
     * them in the relevant text fields. This method is used by the observer.
     */
    private void updateScores() {
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setText(listOfPlayers.get(i).getName());
            playerScores[i].setText(listOfPlayers.get(i).getScoreString());
        }
    }

    /**
     * This method retrieves a question based on the current index and sets it as
     * the content of the currentQuestion TextView. It also updates the remaining
     * number of questions accordingly.
     */
    private void getQuestion() {
        currentQuestion.setText(listOfQuestions.get(index).getQuestionString());
        remainingNum.setText(getRemainingNum());
        remainingString.setText(getRemainingString());
    }
}
