package com.example.android_snack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MathPlatformGame extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_platform_game);

        // 按钮初始化
        Button mainButton = findViewById(R.id.button);
        Button snakeGameButton = findViewById(R.id.button2);
        Button planeGameButton = findViewById(R.id.button3);
        Button exitButton = findViewById(R.id.button4);



        // 点击 Main 按钮，跳转回 MathPlatformMain
        mainButton.setOnClickListener(v -> {
            Intent intent = new Intent(MathPlatformGame.this, MathPlatformMain.class);
            startActivity(intent);
            finish(); // 结束当前活动以防止返回按钮导致的重复导航
        });

        // 跳转到 Snake 游戏
        snakeGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(MathPlatformGame.this, SnakeStarView.class);
            startActivity(intent);
        });

        // 跳转到 Plane 游戏
        planeGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(MathPlatformGame.this, PlaneView.class);
            startActivity(intent);
        });

        // 退出应用
        exitButton.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePetVisibility();
    }

    @Override
    protected void updatePetBubble() {
        if (petBubble != null) {
            petBubble.setText("Games time! Let's have some fun! (≧▽≦)/");
        }
    }
}
