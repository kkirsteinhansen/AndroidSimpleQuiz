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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionsActivity extends AppCompatActivity {

    private EditText questionField;
    private RadioGroup radioGroup;
    private QuestionsViewModel liveQuestions;
    private Context context;
    private RecyclerView recycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        liveQuestions = new QuestionsViewModel(getApplication());
        context = QuestionsActivity.this;

        // EditText
        questionField = findViewById(R.id.addquestion_question);

        // RadioGroup
        radioGroup = findViewById(R.id.addquestion_radiogroup);

        // Button
        Button addButton = findViewById(R.id.addquestion_add);

        // Button functionality
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fieldEmpty(questionField)) {
                    Display.message(context, Display.MISSING_QUESTION);
                    return;
                }
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if (!radioButtonChecked(radioButtonId)) {
                    Display.message(context, Display.MISSING_ANSWER);
                    return; }

                String q = questionField.getText().toString();
                boolean a = findRadioButton(radioButtonId);
                liveQuestions.add(q, a);
                Display.message(context, Display.QUESTION_ADDED);
            }
        });

        // RecyclerView
        recycler = findViewById(R.id.lq_rv);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        QuestionAdapter adapter = new QuestionAdapter();
        recycler.setAdapter(adapter);
        // Observer (part of RecyclerView)
        liveQuestions.getObservable().observe(this,
                q -> adapter.notifyDataSetChanged());
    }

    private boolean radioButtonChecked(int id) {
        return id != -1;
    }

    private boolean findRadioButton(int answerId) {
        return answerId == R.id.addquestion_radiotrue;
    }

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

        private TextView recycler_tv, recycler_answer;

        public QuestionHolder(@NonNull View v) {
            super(v);
            recycler_tv = v.findViewById(R.id.recycler_question);
            recycler_answer = v.findViewById(R.id.recycler_answer);
            v.setOnClickListener(this);
        }

        public void bind(String q, int i, String a) {
            // i is unused because the questions should not be numbered in the UI
            recycler_tv.setText(q);
            recycler_answer.setText(a);
        }

        @Override
        public void onClick(View v) {
            // Get input from RecyclerView OnClick
            // https://stackoverflow.com/questions/5754887/accessing-view-inside-the-linearlayout-with-code
            String q = (String)((TextView)v.findViewById(R.id.recycler_question)).getText();
            liveQuestions.remove(q); // Remove question on click
            Display.message(context, Display.QUESTION_REMOVED);
        }
    }
}
