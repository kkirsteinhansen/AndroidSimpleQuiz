package dk.itu.simplequiz;

import android.database.Cursor;
import android.database.CursorWrapper;

public class DatabaseCursorWrapper extends CursorWrapper {

    public DatabaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * This method converts a row in the database to a new question object.
     * @return a question from the database
     */
    public Question toQuestion() {
        String q = getString(getColumnIndex(Database.QUESTION_COL));
        boolean a = (getInt(getColumnIndex(Database.ANSWER_COL)) == 1);
        return new Question(q, a);
    }
}
