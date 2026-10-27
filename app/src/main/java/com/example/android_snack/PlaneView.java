package com.example.android_snack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PlaneView extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plane_view);
        
        // 初始化按钮
        Button levelModeButton = findViewById(R.id.button_level_mode);
        Button endlessModeButton = findViewById(R.id.button_endless_mode);
        Button viewHighScoreButton = findViewById(R.id.button_view_high_score);
        Button exitButton = findViewById(R.id.button_exit);

        // Set up button click listeners
        levelModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Level Mode Activity
                //Intent intent = new Intent(PlaneView.this, LevelModeActivity.class); // Replace with actual activity class
                //startActivity(intent);
                Intent intent = new Intent(PlaneView.this, PlaneMainActivity.class);
                startActivity(intent);
            }
        });

        endlessModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Endless Mode Activity
                //Intent intent = new Intent(PlaneView.this, EndlessModeActivity.class); // Replace with actual activity class
                //startActivity(intent);
            }
        });

        viewHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // View High Score Activity
                //Intent intent = new Intent(PlaneView.this, HighScoreActivity.class); // Replace with actual activity class
                //startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the activity or app
                finish(); // You can use `finish()` to close this activity
            }
        });
    }

    @Override
    protected void updatePetBubble() {
        if (petBubble != null) {
            petBubble.setText("Ready to fly? Whoosh! ✈️ (●'◡'●)");
        }
    }
}
