package com.example.android_snack;

import static com.example.android_snack.SnakeMacro.LENGTH_SPEED_INCREMENT;
import static com.example.android_snack.SnakeMacro.LENGTH_THRESHOLD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * MainActivity: Handles the snake movement and direction control logic
 */
public class SnakeMainActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout Living_Space;
    private boolean isstar = false;
    private Timer timer;
    private Button BT_isstar, BT_RIGHT, BT_UP, BT_DOWN, BT_LEFT;
    private TextView prompt, ScoreView, MostScore;
    private int direction;
    private int weight, height;
    private boolean islive;
    private int score, time, mostscore;
    SnakeImages snakeImages;
    MoveSnack movesnack;
    SnakeSharedPre shared;
    private Handler handler;
    private int eatenFoodCount = 0; // Counter for the number of food items eaten

    class MoveSnack extends Thread {
        @SuppressLint("HandlerLeak")
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler() {
                public void handleMessage(@NonNull Message msg) {
                    if (isstar && islive) {
                        // Update snake direction
                        switch (msg.what) {
                            case SnakeMacro.Right:
                                snakeImages.setBitmapX(snakeImages.getBitmapX() + SnakeMacro.UNIT);
                                break;
                            case SnakeMacro.Up:
                                snakeImages.setBitmapY(snakeImages.getBitmapY() - SnakeMacro.UNIT);
                                break;
                            case SnakeMacro.Left:
                                snakeImages.setBitmapX(snakeImages.getBitmapX() - SnakeMacro.UNIT);
                                break;
                            case SnakeMacro.Dwon:
                                snakeImages.setBitmapY(snakeImages.getBitmapY() + SnakeMacro.UNIT);
                                break;
                        }

                        // Check for collision with each food item
                        for (int i = 0; i < 3; i++) {
                            if (snakeImages.getBitmapX() == snakeImages.getFoodX(i) && snakeImages.getBitmapY() == snakeImages.getFoodY(i)) {
                                // Update the number displayed on the snake's head
                                snakeImages.setSnakeNumber(snakeImages.getSnakeNumber() + snakeImages.getFoodNumbers(i));

                                // Adjust length and score
                                if (snakeImages.getFoodNumbers(i) > 0) {
                                    snakeImages.setLenght(snakeImages.getLenght() + 1);
                                    score += SnakeMacro.SCORE_UNIT;
                                } else {
                                    snakeImages.setLenght(Math.max(1, snakeImages.getLenght() - 1));
                                    score = Math.max(0, score - SnakeMacro.SCORE_UNIT / 2);
                                }

                                eatenFoodCount++; // Increment food counter

                                if (eatenFoodCount == 2) {
                                    if ((snakeImages.getSnakeNumber() % snakeImages.getTarget()) != 0) {
                                        islive = false;
                                        runOnUiThread(() -> {
                                            BT_isstar.setText(R.string.restar);
                                            prompt.setText(R.string.promptrestar); // Set failure message text
                                            prompt.setVisibility(View.VISIBLE); // Show failure message
                                            if (score > mostscore)
                                                mostscore = score;
                                            shared.save(mostscore);
                                        });
                                        return; // Exit handleMessage to avoid further code execution
                                    }
                                    // After eating two food items, delete the remaining one and refresh positions and numbers
                                    for (int j = 0; j < 3; j++) {
                                        int newFoodX = (int) (Math.random() * (weight / SnakeMacro.UNIT)) * SnakeMacro.UNIT;
                                        int newFoodY = (int) (Math.random() * (height / SnakeMacro.UNIT)) * SnakeMacro.UNIT;

                                        // Ensure new food position does not overlap with snake head
                                        while (newFoodX == snakeImages.getBitmapX() && newFoodY == snakeImages.getBitmapY()) {
                                            newFoodX = (int) (Math.random() * (weight / SnakeMacro.UNIT)) * SnakeMacro.UNIT;
                                            newFoodY = (int) (Math.random() * (height / SnakeMacro.UNIT)) * SnakeMacro.UNIT;
                                        }

                                        // Update food positions and numbers
                                        snakeImages.initFoodNumbers();
                                        snakeImages.setFoodX(j, newFoodX);
                                        snakeImages.setFoodY(j, newFoodY);
                                        snakeImages.setFoodNumber(j, snakeImages.getFoodNumbers(j));
                                    }
                                    eatenFoodCount = 0; // Reset counter
                                } else {
                                    // Delete the current eaten food
                                    snakeImages.setFoodX(i, -1); // Set X to -1 to indicate deletion
                                    snakeImages.setFoodY(i, -1); // Set Y to -1 to indicate deletion
                                    snakeImages.setFoodNumber(i, 0); // Set food number to 0
                                }

                                // Adjust speed
                                if (snakeImages.getLenght() % LENGTH_SPEED_INCREMENT == 0 && snakeImages.getLenght() >= LENGTH_THRESHOLD) {
                                    timer.cancel();
                                    time = Math.max(100, time - LENGTH_SPEED_INCREMENT);
                                    StarViev(time);
                                }
                            }
                        }

                        // Refresh view
                        runOnUiThread(snakeImages::invalidate);

                        // Update score display
                        runOnUiThread(() -> {
                            String scorePrefix = ScoreView.getContext().getString(R.string.score);
                            ScoreView.setText(scorePrefix + " " + score);
                        });

                        // Check for game over conditions
                        if (snakeImages.getBitmapX() >= weight || snakeImages.getBitmapX() < 0 || snakeImages.getBitmapY() < 0 || snakeImages.getBitmapY() >= height || !snakeImages.isIslive()) {
                            islive = false;
                            runOnUiThread(() -> {
                                BT_isstar.setText(R.string.restar);
                                prompt.setText(R.string.promptrestar);
                                prompt.setVisibility(View.VISIBLE);
                                if (score > mostscore)
                                    mostscore = score;
                                shared.save(mostscore);
                            });
                        }
                    }
                    super.handleMessage(msg);
                }
            };
            Looper.loop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake_main);
        movesnack = new MoveSnack();
        movesnack.start();
        Init();

        // Get textView4 control
        TextView textView4 = findViewById(R.id.textView4);

        // Use string resource and target value to set textView4 content
        String initialRandomNumberText = getString(R.string.initial_random_number, snakeImages.getTarget());
        textView4.setText(initialRandomNumberText);

        StarViev(time);
        // Add images to layout
        Living_Space.addView(snakeImages);
        // Get width and height of active space
        Living_Space.post(new Runnable() {
            @Override
            public void run() {
                weight = Living_Space.getWidth();
                height = Living_Space.getHeight();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        timer.cancel();
    }

    /**
     * Init: Initialize UI controls and parameters
     */
    void Init() {
        Living_Space = findViewById(R.id.framelayout);
        BT_isstar = findViewById(R.id.isstar);
        BT_RIGHT = findViewById(R.id.Right);
        BT_UP = findViewById(R.id.Up);
        BT_DOWN = findViewById(R.id.Dwon);
        BT_LEFT = findViewById(R.id.Left);
        prompt = findViewById(R.id.prompt);
        ScoreView = findViewById(R.id.Score);
        MostScore = findViewById(R.id.MostScore);

        snakeImages = new SnakeImages(SnakeMainActivity.this);
        shared = new SnakeSharedPre(SnakeMainActivity.this);
        direction = SnakeMacro.Right;
        snakeImages.Init();
        snakeImages.Size();
        islive = true;
        score = 0;
        mostscore = shared.read();
        time = 0;

        BT_isstar.setOnClickListener(this);
        BT_RIGHT.setOnClickListener(this);
        BT_UP.setOnClickListener(this);
        BT_LEFT.setOnClickListener(this);
        BT_DOWN.setOnClickListener(this);
        MostScore.setText(getString(R.string.most_score) + shared.read());
    }

    /**
     * onClick: Handles button click events
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Game start/pause button
            case R.id.isstar:
                isstar = !isstar;
                if (isstar && islive) {
                    BT_isstar.setText(R.string.stop);
                    prompt.setVisibility(View.GONE);
                } else if (!isstar && islive) {
                    BT_isstar.setText(R.string.star);
                    prompt.setVisibility(View.VISIBLE);
                } else if (!islive) {
                    finish();
                }
                break;
            // Control snake direction right
            case R.id.Right:
                if (isstar) {
                    direction = SnakeMacro.Right;
                    snakeImages.setDirection(direction);
                }
                break;
            // Control snake direction up
            case R.id.Up:
                if (isstar) {
                    direction = SnakeMacro.Up;
                    snakeImages.setDirection(direction);
                }
                break;
            // Control snake direction left
            case R.id.Left:
                if (isstar) {
                    direction = SnakeMacro.Left;
                    snakeImages.setDirection(direction);
                }
                break;
            // Control snake direction down
            case R.id.Dwon:
                if (isstar) {
                    direction = SnakeMacro.Dwon;
                    snakeImages.setDirection(direction);
                }
        }
    }

    /**
     * StarViev: Timer to periodically send snake direction information and control speed
     *
     * @param time the time interval for the timer
     */
    void StarViev(int time) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(direction);
            }
        }, SnakeMacro.DELAY, SnakeMacro.PERIOD - time);
    }
    @Override
    protected void updatePetBubble() {
        if (petBubble != null) {
            petBubble.setText("Nom nom nom... catch those numbers! 🍎");
        }
    }
}
