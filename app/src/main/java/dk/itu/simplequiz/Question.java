package dk.itu.simplequiz;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Question extends ViewModel {

    private static List<Question> listOfQuestions;
    private String question;
    private boolean answer;

    public Question(String question, boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public static List<Question> initQuestions() {
        if (listOfQuestions == null) {
            listOfQuestions = new ArrayList<>();

            // Temp questions for testing
            listOfQuestions.add(new Question("Q1", true));
            listOfQuestions.add(new Question("Q2", false));
            listOfQuestions.add(new Question("Q3", true));

            return listOfQuestions;
        }
        return listOfQuestions;
    }

    public static void addQuestion(String q, boolean a) {
        if (!q.equals("")) listOfQuestions.add(new Question(q, a));
    }

    public static void removeQuestion(String q) {
        Iterator<Question> it = listOfQuestions.iterator();
        while (it.hasNext()) {
            Question question = it.next();
            if (question.getQuestionString().equals(q)) {
                it.remove();
                break;
            }
        }
    }

    public String getQuestionString() {
        return question;
    }

    public Boolean getBooleanAnswer() {
        return answer;
    }

    public static List<Question> getListOfQuestions() {
        return listOfQuestions;
    }
}
