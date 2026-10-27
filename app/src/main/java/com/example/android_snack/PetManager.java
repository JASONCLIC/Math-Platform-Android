package com.example.android_snack;

import android.content.Context;
import android.content.SharedPreferences;

public class PetManager {
    private static PetManager instance;
    private boolean isPetVisible = false;
    private Context context;

    private PetManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized PetManager getInstance(Context context) {
        if (instance == null) {
            instance = new PetManager(context);
        }
        return instance;
    }

    public boolean isPetVisible() {
        return isPetVisible;
    }

    public void setPetVisible(boolean visible) {
        isPetVisible = visible;
    }
}