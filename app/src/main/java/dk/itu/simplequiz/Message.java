package dk.itu.simplequiz;

import android.content.Context;
import android.widget.Toast;

public class Message {

    public static final String NO_QUESTIONS =  "There are no questions!";
    public static final String MISSING_QUESTION =  "Missing question!";
    public static final String MISSING_ANSWER =  "Missing answer!";
    public static final String NOT_ENOUGH_PLAYERS =  "Not enough players!";
    public static final String QUESTION_ADDED =  "Question added!";
    public static final String QUESTION_REMOVED =  "Question removed!";

    /**
     * This method displays a short Toast message on the screen.
     * @param c the context
     * @param s the message as a string
     */
    public static void show(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }
}
