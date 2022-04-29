package dk.itu.simplequiz;

import android.database.Cursor;
import android.database.CursorWrapper;

public class DatabaseCursorWrapper extends CursorWrapper {

    public DatabaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Question toQuestion() {
        String q = getString(getColumnIndex(Database.QUESTION_COL));
        boolean a = (getInt(getColumnIndex(Database.ANSWER_COL)) == 1);
        return new Question(q, a);
    }
}
