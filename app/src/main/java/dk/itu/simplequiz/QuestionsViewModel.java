package dk.itu.simplequiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class QuestionsViewModel extends AndroidViewModel {

    private static MutableLiveData<List<Question>> liveQuestions;

    public QuestionsViewModel(@NonNull Application app) {
        super(app);
        if (liveQuestions == null) {
            liveQuestions = new MutableLiveData<>();
            liveQuestions.setValue(Question.initQuestions());
        }
    }

    public void add(String q, boolean a) {
        Question.addQuestion(q, a);
        liveQuestions.setValue(Question.getListOfQuestions());
    }

    public void remove(String q) {
        Question.removeQuestion(q);
        liveQuestions.setValue(Question.getListOfQuestions());
    }

    public int getItemCount() {
        return Question.initQuestions().size();
    }

    public MutableLiveData<List<Question>> getObservable() {
        return liveQuestions;
    }

    public List<Question> getListOfQuestions() {
        return liveQuestions.getValue();
    }

}
