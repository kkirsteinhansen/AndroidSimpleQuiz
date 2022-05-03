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
            Question.initQuestions(getApplication());
            liveQuestions.setValue(Question.getListOfQuestions());
        }
    }

    /**
     * This method is a wrapper method for the corresponding add() method in
     * the Question class. This method calls the original method and then resets the
     * value of the MutableLiveData object. This ensures that the observer is
     * notified of a change in the data.
     * @param q the question
     * @param a the answer
     */
    public void add(String q, boolean a) {
        Question.addQuestion(q, a);
        liveQuestions.setValue(Question.getListOfQuestions());
    }

    /**
     * This method is a wrapper method for the corresponding remove() method in
     * the Question class. This method calls the original method and then resets the
     * value of the MutableLiveData object. This ensures that the observer is
     * notified of a change in the data.
     * @param q a string matching the question to be removed
     */
    public void remove(String q) {
        Question.removeQuestion(q);
        liveQuestions.setValue(Question.getListOfQuestions());
    }

    public int getItemCount() {
        return Question.getListOfQuestions().size();
    }

    public MutableLiveData<List<Question>> getObservable() {
        return liveQuestions;
    }

    public List<Question> getListOfQuestions() {
        return liveQuestions.getValue();
    }

}
