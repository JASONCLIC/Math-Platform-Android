package com.example.android_snack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The StarView class: Represents the game start screen
 */
public class SnakeStarView extends BaseActivity implements View.OnClickListener {

    private Button Starbutton, Sbutton, Backbutton;
    /**
     * The Shared instance: Handles score storage
     */
    SnakeSharedPre shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake_star_view);
        Init();
    }

    /**
     * Init: Initializes the UI components for this activity
     */
    void Init() {
        Starbutton = findViewById(R.id.star_game);
        Sbutton = findViewById(R.id.MostBUtton);
        Backbutton = findViewById(R.id.BackButton);

        shared = new SnakeSharedPre(this);

        Starbutton.setOnClickListener(this);
        Sbutton.setOnClickListener(this);
        Backbutton.setOnClickListener(this);
    }

    /**
     * onClick: Handles button click events on this screen
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star_game:
                Intent intent = new Intent(SnakeStarView.this, SnakeMainActivity.class);
                startActivity(intent);
                break;
            case R.id.BackButton:
                finish();
                break;
            case R.id.MostBUtton:
                Sbutton.setText(getString(R.string.h_most_score) + shared.read());
                break;
        }
    }
    @Override
    protected void updatePetBubble() {
        if (petBubble != null) {
            petBubble.setText("Sssssup! Ready to play snake? 🐍");
        }
    }

}
