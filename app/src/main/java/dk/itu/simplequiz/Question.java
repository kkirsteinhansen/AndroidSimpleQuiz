package dk.itu.simplequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Question extends ViewModel {

    private static List<Question> listOfQuestions;
    private static SQLiteDatabase database;
    private String question;
    private boolean answer;


    public Question(String question, boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public static void initQuestions(Context c) {
        if (database == null)
            database = new Database(c.getApplicationContext()).getWritableDatabase();
        if (listOfQuestions == null) {
            listOfQuestions = new ArrayList<>();
            addQuestionsFromDatabase();
        }
    }

    public static void addQuestion(String q, boolean a) {
        // No null check for q needed, a check is performed in QuestionsActivity
        Question quest = new Question(q, a);
        database.insert(Database.QUESTIONS_TABLE, null,
                asContentValues(quest));

        listOfQuestions.add(quest);
    }

    // Database helper methods to convert between Question objects and database rows
    private static ContentValues asContentValues(Question q) {
        ContentValues cv = new ContentValues();
        cv.put(Database.QUESTION_COL, q.getQuestionString());
        cv.put(Database.ANSWER_COL, q.getBooleanAnswer());
        return cv;
    }

    public static void removeQuestion(String question) {
        String clause = Database.QUESTION_COL + " LIKE ?";
        String[] args = {question};
        database.delete(Database.QUESTIONS_TABLE, clause, args);

        Iterator<Question> it = listOfQuestions.iterator();
        while (it.hasNext()) {
            Question quest = it.next();
            if (quest.getQuestionString().equals(question)) {
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

    private static void addQuestionsFromDatabase() {
        if (listOfQuestions.isEmpty()) {
            DatabaseCursorWrapper cursor = Database.selectAll(database);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listOfQuestions.add(cursor.toQuestion());
                cursor.moveToNext();
            }
            cursor.close();
        }
    }
}
