package com.example.android_snack;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The SharedPre class: Handles score storage
 */
public class SnakeSharedPre {
    private int mostscore;
    private Context context;

    public SnakeSharedPre() {
    }

    public SnakeSharedPre(Context context) {
        this.context = context;
    }

    /**
     * Save: Stores the score
     *
     * @param mscore the score to save
     */
    // Stores the highest game score record
    public void save(int mscore) {
        SharedPreferences sp = context.getSharedPreferences(SnakeMacro.Pname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("score", mscore);
        editor.apply();
    }

    /**
     * Read: Retrieves the stored score
     *
     * @return the highest score as an int
     */
    // Reads and returns the highest game score record
    public int read() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SnakeMacro.Pname, Context.MODE_PRIVATE);
        mostscore = sharedPreferences.getInt("score", 0);
        return mostscore;
    }
}
