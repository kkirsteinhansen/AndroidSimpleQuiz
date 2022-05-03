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

    /**
     * This method initializes the database and a list of questions if they do not already exist.
     */
    public static void initQuestions(Context c) {
        if (database == null)
            database = new Database(c.getApplicationContext()).getWritableDatabase();
        if (listOfQuestions == null) {
            listOfQuestions = new ArrayList<>();
            addQuestionsFromDatabase(); // Add persistent questions to the list upon initialization
        }
    }

    public static void addQuestion(String q, boolean a) {
        // No null check for q needed, a check is performed in the QuestionsFragment class
        Question quest = new Question(q, a);
        database.insert(Database.QUESTIONS_TABLE, null,
                asContentValues(quest));
        listOfQuestions.add(quest);
    }

    /**
     * This method converts between Question objects and ContentValue objects.
     * It is used by the addQuestion() method when adding Question objects to
     * the database.
     * @param q the Question object
     * @return a ContentValue object containing the data associated with the Question object
     */
    private static ContentValues asContentValues(Question q) {
        ContentValues cv = new ContentValues();
        cv.put(Database.QUESTION_COL, q.getQuestionString());
        cv.put(Database.ANSWER_COL, q.getBooleanAnswer());
        return cv;
    }

    /**
     * This method removes a question from the database and the list of questions
     * based on a string input matching the question string.
     * This method is used by the RecycleView of questions.
     * @param question a string matching the question to be removed
     */
    public static void removeQuestion(String question) {

        // Remove from database
        String clause = Database.QUESTION_COL + " LIKE ?";
        String[] args = {question};
        database.delete(Database.QUESTIONS_TABLE, clause, args);

        // Remove from list of questions
        Iterator<Question> it = listOfQuestions.iterator();
        while (it.hasNext()) {
            Question quest = it.next();
            if (quest.getQuestionString().equals(question)) {
                it.remove();
                break; }
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

    /**
     * This method adds the persistent questions from the database to the
     * list of questions upon initialization of the list. This is necessary
     * because the list is not persistent.
     */
    private static void addQuestionsFromDatabase() {
        if (listOfQuestions.isEmpty()) {
            DatabaseCursorWrapper cursor = Database.selectAll(database);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listOfQuestions.add(cursor.toQuestion());
                cursor.moveToNext(); }
            cursor.close(); }
    }
}
