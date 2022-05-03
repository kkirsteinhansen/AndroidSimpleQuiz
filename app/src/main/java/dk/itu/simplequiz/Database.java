package dk.itu.simplequiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuestionDatabase";
    private static final int VERSION = 1; // Versioning will not be used

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
        addDummyQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This method selects all the content in the given database and returns a cursor.
     * @param db the database
     * @return a cursor wrapper
     */
    public static DatabaseCursorWrapper selectAll(SQLiteDatabase db) {
        Cursor cursor = db.query(Database.QUESTIONS_TABLE, null, null,
                null, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }

    /**
     * This method fills the database with a few questions upon first installation,
     * which allows the user to start quizzing immediately.
     * @param db the database the questions should be added to
     */
    private void addDummyQuestions(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + QUESTIONS_TABLE +
                " (" + QUESTION_COL + "," + ANSWER_COL + ") " +
                "VALUES ('Molecules are smaller than electrons.', FALSE)");

        db.execSQL("INSERT INTO " + QUESTIONS_TABLE +
                " (" + QUESTION_COL + "," + ANSWER_COL + ") " +
                "VALUES ('There are seven continents in the world.', TRUE)");

        db.execSQL("INSERT INTO " + QUESTIONS_TABLE +
                " (" + QUESTION_COL + "," + ANSWER_COL + ") " +
                "VALUES ('Vatican City is the smallest city in the world.', TRUE)");

        db.execSQL("INSERT INTO " + QUESTIONS_TABLE +
                " (" + QUESTION_COL + "," + ANSWER_COL + ") " +
                "VALUES ('The Nile is the longest river in Africa.', TRUE)");
    }
}
