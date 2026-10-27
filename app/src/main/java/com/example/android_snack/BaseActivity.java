package com.example.android_snack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class BaseActivity extends AppCompatActivity {
    private static Bitmap processedPetBitmap;
    protected TextView petBubble;
    private ImageView petView;

    private Handler handler = new Handler();
    private Random random = new Random();

    private boolean isMoving = false;
    private float currentSpeed = 10f; // 初始速度
    private static final int BASE_DURATION = 5000; // 默认持续时间 5 秒
    private int remainingDuration = BASE_DURATION;
    private float originalX, originalY; // 回归位置 (右上角)
    private float originalBubbleX, originalBubbleY; // 气泡框原始位置

    private float moveX, moveY; // 移动方向向量
    private Runnable moveRunnable;
    private static final int DIRECTION_COUNT = 12; // 方向数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化一次桌宠图片
        if (processedPetBitmap == null) {
            Bitmap petBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pet);
            int backgroundColor = petBitmap.getPixel(0, 0);
            processedPetBitmap = removeBackgroundColor(petBitmap, backgroundColor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPetAnimation();
        updatePetVisibility();
        updatePetBubble(); // 确保文本更新
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPetAnimation();
    }

    private void setupPetAnimation() {
        petView = findViewById(R.id.pet_image);
        petBubble = findViewById(R.id.pet_bubble);

        if (petView != null) {
            petView.setImageBitmap(processedPetBitmap);

            // 保存原始位置
            petView.post(() -> {
                originalX = petView.getX();
                originalY = petView.getY();
                if (petBubble != null) {
                    originalBubbleX = petBubble.getX();
                    originalBubbleY = petBubble.getY();
                }
            });

            // 设置点击监听
            petView.setOnClickListener(v -> {
                if (isMoving) {
                    currentSpeed *= 1.5f;
                    remainingDuration += 3000;
                    updatePetBubble();
                } else {
                    startRandomMovement();
                }
            });
        }
    }

    private void updateDirection() {
        // 随机选择一个0-11的数字，对应12个方向
        int randomDirection = random.nextInt(DIRECTION_COUNT);
        // 计算对应的角度 (0°, 30°, 60°, ..., 330°)
        float angle = randomDirection * 30;
        // 转换为弧度
        double radian = Math.toRadians(angle);
        // 计算新的移动向量
        moveX = (float) Math.cos(radian);
        moveY = (float) Math.sin(radian);
    }

    private void startRandomMovement() {
        if (petView == null) return;

        isMoving = true;
        currentSpeed = 10f;
        remainingDuration = BASE_DURATION;

        moveRunnable = new Runnable() {
            private long lastDirectionUpdate = System.currentTimeMillis();

            @Override
            public void run() {
                if (remainingDuration <= 0) {
                    returnToOriginalPosition();
                    return;
                }

                // 每秒随机更新一次方向
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastDirectionUpdate >= 1000) {
                    updateDirection();
                    lastDirectionUpdate = currentTime;
                    updatePetBubble(); // 每次改变方向时更新对话
                }

                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;

                // 计算下一位置
                float newX = petView.getX() + moveX * currentSpeed;
                float newY = petView.getY() + moveY * currentSpeed;

                // 确保不超出屏幕边界
                newX = Math.max(0, Math.min(screenWidth - petView.getWidth(), newX));
                newY = Math.max(0, Math.min(screenHeight - petView.getHeight(), newY));

                // 更新位置
                petView.setX(newX);
                petView.setY(newY);

                // 更新气泡位置
                if (petBubble != null) {
                    petBubble.setX(newX);
                    petBubble.setY(newY - petBubble.getHeight());
                }

                remainingDuration -= 16;
                handler.postDelayed(this, 16);
            }
        };

        handler.post(moveRunnable);
        updatePetBubble();
    }

    private void returnToOriginalPosition() {
        isMoving = false;

        // 桌宠返回原位
        petView.animate()
                .x(originalX)
                .y(originalY)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    currentSpeed = 10f;
                    updatePetBubble();
                });

        // 气泡框返回原位
        if (petBubble != null) {
            petBubble.animate()
                    .x(originalBubbleX)
                    .y(originalBubbleY)
                    .setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator());
        }
    }

    private void stopPetAnimation() {
        if (handler != null) {
            handler.removeCallbacks(moveRunnable);
        }
        if (isMoving) {
            returnToOriginalPosition();
        }
    }

    protected void updatePetVisibility() {
        petView = findViewById(R.id.pet_image);
        petBubble = findViewById(R.id.pet_bubble);

        if (petView != null) {
            boolean isVisible = PetManager.getInstance(this).isPetVisible();
            petView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            if (petBubble != null) {
                petBubble.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        }
    }

    protected void updatePetBubble() {
        if (petBubble != null) {
            if (isMoving) {
                if (currentSpeed > 15f) {
                    petBubble.setText("Zooming around! ⚡");
                } else {
                    String[] messages = {
                            "This way looks fun! 🎯",
                            "Let's try this direction! 🎪",
                            "Wheee! New path! 🎨"
                    };
                    petBubble.setText(messages[random.nextInt(messages.length)]);
                }
            } else {
                petBubble.setText("Math is fun with me around! (｡･ω･｡)");
            }
        }
    }

    private Bitmap removeBackgroundColor(Bitmap bitmap, int backgroundColor) {
        int tolerance = 15;
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = bitmap.getPixel(x, y);
                if (isColorSimilar(pixel, backgroundColor, tolerance)) {
                    result.setPixel(x, y, android.graphics.Color.TRANSPARENT);
                } else {
                    result.setPixel(x, y, pixel);
                }
            }
        }
        return result;
    }

    private boolean isColorSimilar(int color1, int color2, int tolerance) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;

        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        return Math.abs(red1 - red2) <= tolerance &&
                Math.abs(green1 - green2) <= tolerance &&
                Math.abs(blue1 - blue2) <= tolerance;
    }
}
