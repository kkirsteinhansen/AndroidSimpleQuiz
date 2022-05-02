package dk.itu.simplequiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainMenuFragment extends Fragment {

    MaterialButton quizButton, questionsButton;
    List<Question> listOfQuestions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Question.initQuestions(getActivity());
        listOfQuestions = Question.getListOfQuestions();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View  mainMenu = inflater.inflate(R.layout.fragment_main_menu, container, false);

        // Buttons
        quizButton = mainMenu.findViewById(R.id.quiz);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cannot start quiz if there are no questions
                if (listOfQuestions.isEmpty()) {
                    Message.show(getActivity(), Message.NO_QUESTIONS);
                    return;
                }
                Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_playerFragment);
            }
        });

        questionsButton = mainMenu.findViewById(R.id.questions);
        questionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_questionsActivity);
            }
        });

        return mainMenu;
    }
}