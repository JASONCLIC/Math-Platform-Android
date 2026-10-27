package com.example.android_snack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class PlaneMainActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout gameSpace;
    private Button leftButton, rightButton;
    private PlaneGameView gameView;
    private Timer gameTimer;
    private boolean isPlaying = true;
    private int screenWidth;
    private int screenHeight;
    private final int MOVE_LEFT = 1;
    private final int MOVE_RIGHT = 2;

    @SuppressLint("HandlerLeak")
    private final Handler gameHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (isPlaying) {
                switch (msg.what) {
                    case MOVE_LEFT:
                        gameView.movePlaneLeft();
                        break;
                    case MOVE_RIGHT:
                        gameView.movePlaneRight();
                        break;
                }
                gameView.updateGame();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_main);

        initViews();
        setupGame();
    }

    private void initViews() {
        gameSpace = findViewById(R.id.game_space);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        gameView = new PlaneGameView(this);
        gameSpace.addView(gameView);

        // 确保在布局渲染完成后再获取宽高
        gameSpace.post(() -> {
            screenWidth = gameSpace.getWidth();
            screenHeight = gameSpace.getHeight();
            gameView.initGame(screenWidth, screenHeight); // 初始化游戏
        });
    }

    private void setupGame() {
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameHandler.sendEmptyMessage(0);
            }
        }, 0, 30); // Update every 30ms
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_button) {
            gameHandler.sendEmptyMessage(MOVE_LEFT);
        } else if (v.getId() == R.id.right_button) {
            gameHandler.sendEmptyMessage(MOVE_RIGHT);
        }
    }
    @Override
    protected void updatePetBubble() {
        if (petBubble != null) {
            petBubble.setText("Pew pew! Time to dodge those numbers! ⭐");
        }
    }

}
