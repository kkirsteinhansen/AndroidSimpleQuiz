package dk.itu.simplequiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuestionDatabase";
    private static final int VERSION = 1;

    public static final String QUESTIONS_TABLE = "Questions";
    public static final String QUESTION_COL = "question";
    public static final String ANSWER_COL = "answer";

    public Database(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + QUESTIONS_TABLE +
                " (" + QUESTION_COL + " TEXT, " + ANSWER_COL + " BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static DatabaseCursorWrapper selectAll(SQLiteDatabase db) {
        Cursor cursor = db.query(Database.QUESTIONS_TABLE, null, null,
                null, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }
}
