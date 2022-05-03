package dk.itu.simplequiz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ManageQuestionsFragment extends Fragment {

    private Context context;
    private QuestionsViewModel liveQuestions;

    private EditText questionField;
    private RadioGroup radioGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get LiveData
        liveQuestions = new QuestionsViewModel(getActivity().getApplication());
        context = getContext(); // Save context in variable for easy use
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View questionsMenu = inflater
                .inflate(R.layout.fragment_manage_questions, container, false);

        questionField = questionsMenu.findViewById(R.id.addquestion_question);
        radioGroup = questionsMenu.findViewById(R.id.addquestion_radiogroup);

        // Button
        Button addButton = questionsMenu.findViewById(R.id.addquestion_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check for missing question
                if (fieldEmpty(questionField)) {
                    Message.show(context, Message.MISSING_QUESTION);
                    return; }

                int radioButtonId = radioGroup.getCheckedRadioButtonId();

                // Check for missing answer
                if (!radioButtonChecked(radioButtonId)) {
                    Message.show(context, Message.MISSING_ANSWER);
                    return; }

                String q = questionField.getText().toString();
                boolean a = findRadioButton(radioButtonId);

                liveQuestions.add(q, a);
                Message.show(context, Message.QUESTION_ADDED);

                // Clear fields
                questionField.setText("");
                radioGroup.clearCheck(); }
        });

        // RecyclerView
        RecyclerView recycler = questionsMenu.findViewById(R.id.recycler_container);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        QuestionAdapter adapter = new QuestionAdapter();
        recycler.setAdapter(adapter);

        liveQuestions.getObservable().observe(this,
                q -> adapter.notifyDataSetChanged());

        return questionsMenu;
    }

    /**
     * This method checks whether a radio button has been checked or not.
     * @param id the id of the checked radio button (-1 if no button is checked)
     * @return true if a button has been checked, false otherwise
     */
    private boolean radioButtonChecked(int id) {
        return id != -1;
    }

    /**
     * This method returns the answer to a question as indicated by the checked
     * radio button.
     * @param answerId the id of the checked radio button
     * @return true if the 'true' button has been checked, false otherwise
     */
    private boolean findRadioButton(int answerId) {
        return answerId == R.id.addquestion_radiotrue;
    }

    /**
     * This method checks whether the question field is empty.
     * @param field the EditText field
     * @return true if the contents of the field is equal to the empty string, false otherwise
     */
    private boolean fieldEmpty(EditText field) {
        return field.getText().toString().equals("");
    }



    public class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater lI = LayoutInflater.from(context);
            View v = lI.inflate(R.layout.recyclerview_questions, parent, false);
            return new QuestionHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionHolder questionHolder, int i) {
            String q = liveQuestions.getListOfQuestions().get(i).getQuestionString();
            String a = liveQuestions.getListOfQuestions().get(i).getBooleanAnswer().toString();
            questionHolder.bind(q, i, a);
        }

        @Override
        public int getItemCount() {
            return liveQuestions.getItemCount();
        }
    }

    public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView recyclerQuestion, recyclerAnswer;

        public QuestionHolder(@NonNull View v) {
            super(v);
            recyclerQuestion = v.findViewById(R.id.recycler_question);
            recyclerAnswer = v.findViewById(R.id.recycler_answer);
            v.setOnClickListener(this);
        }

        public void bind(String q, int i, String a) {
            // i is unused because the questions should not be numbered in the UI
            recyclerQuestion.setText(q);
            recyclerAnswer.setText(a);
        }

        @Override
        public void onClick(View v) {
            // Get input from RecyclerView on click
            String q = (String)((TextView)v.findViewById(R.id.recycler_question)).getText();
            liveQuestions.remove(q); // Remove question on click
            Message.show(context, Message.QUESTION_REMOVED);
        }
    }
}
