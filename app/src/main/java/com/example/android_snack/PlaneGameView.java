package com.example.android_snack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PlaneGameView extends View {

    private int planeX;
    private int planeY;

    private int planeNumber = 1; // 飞机的初始值

    private final int obstacleWidth = 500;
    private final int obstacleHeight = 150;
    private int screenWidth;
    private int screenHeight;

    private Bitmap planeBitmap; // 用于存储飞机图片
    private Bitmap greenBitmap; // 用于加/乘的图片
    private Bitmap redBitmap;   // 用于减的图片

    // 在顶部添加敌机的变量
    private Bitmap enemyPlaneBitmap; // 用于存储 enemyplane.png 图片

    private int enemyPlaneX; // 敌机的 X 坐标
    private int enemyPlaneY; // 敌机的 Y 坐标

    // 更新障碍物列表的数据结构
    private final List<Object[]> obstacles = new ArrayList<>();

    // 左右侧的障碍物（数字和运算符）
    private List<Object[]> leftData = PlaneNumberAlgorithm.generateNumberSet(12);  // 左侧数据：[数字, 运算符]
    private List<Object[]> rightData = PlaneNumberAlgorithm.generateNumberSet(12); // 右侧数据：[数字, 运算符]

    List<Object[]> left = new ArrayList<>();
    List<Object[]> right = new ArrayList<>();


    private int leftCenterX;
    private int rightCenterX;

    private boolean isCollisionProcessed = false; // 标记是否已经处理了当前对的碰撞

    private Context context;

    public PlaneGameView(Context context) {
        super(context);
        this.context = context;

        // 加载飞机图片
        planeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.myplane);
        int backgroundColor = planeBitmap.getPixel(0, 0); // 获取左上角像素颜色
        planeBitmap = removeBackgroundColor(planeBitmap, backgroundColor);

        greenBitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.green),
                obstacleWidth,
                obstacleHeight,
                true
        );
        redBitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.red),
                obstacleWidth,
                obstacleHeight,
                true
        );
        enemyPlaneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.enemyplane);
        int enemyBackgroundColor = enemyPlaneBitmap.getPixel(0, 0); // 获取左上角像素颜色
        enemyPlaneBitmap = removeBackgroundColor(enemyPlaneBitmap, enemyBackgroundColor);

        // 深拷贝 leftData
        for (Object[] obj : leftData) {
            left.add(obj.clone());
        }

        // 深拷贝 rightData
        for (Object[] obj : rightData) {
            right.add(obj.clone());
        }

    }


    public void initGame(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // 初始化飞机位置
        planeX = screenWidth / 2 - planeBitmap.getWidth() / 2; // 根据图片宽度调整居中
        planeY = screenHeight - planeBitmap.getHeight() - 200; // 根据图片高度调整位置

        // 初始化左右中心点
        leftCenterX = screenWidth / 4 - obstacleWidth / 2;
        rightCenterX = 3 * screenWidth / 4 - obstacleWidth / 2;

        generateObstacles(); // 生成初始掉落物
        // 初始化敌机位置
        if (enemyPlaneBitmap != null) {
            enemyPlaneX = screenWidth / 2 - enemyPlaneBitmap.getWidth() / 2; // 居中显示
            enemyPlaneY = screenHeight / 4 - enemyPlaneBitmap.getHeight() ; // 在屏幕上方
        }
    }

    private boolean isAnyObstacleVisible() {
        for (Object[] obstacle : obstacles) {
            int obstacleY = (int) obstacle[1];
            if (obstacleY + obstacleHeight > 0 && obstacleY < screenHeight) {
                return true; // 如果有障碍物在屏幕范围内
            }
        }
        return false; // 没有障碍物在屏幕范围内
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF); // 白色
        textPaint.setTextSize(100);

        Paint planeTextPaint = new Paint();
        planeTextPaint.setColor(0xFF000000); // 黑色，用于飞机数字
        planeTextPaint.setTextSize(60);

        // 绘制飞机
        if (planeBitmap != null) {
            canvas.drawBitmap(planeBitmap, planeX, planeY, null);

            // 绘制飞机的数字
            String planeText = String.valueOf(planeNumber);
            canvas.drawText(planeText, planeX + planeBitmap.getWidth() / 2, planeY - 20, planeTextPaint);
        }

        // 偏移量，控制障碍物向左靠
        int offsetX = 0;

        // 绘制障碍物
        for (Object[] obstacle : obstacles) {
            int x = (int) obstacle[0] + offsetX; // 调整 X 坐标
            int y = (int) obstacle[1];
            int number = (int) obstacle[2];
            char operator = (char) obstacle[3];

            // 根据符号选择图片
            Bitmap obstacleBitmap = null;
            if (operator == '+' || operator == '*') {
                obstacleBitmap = greenBitmap;
            } else if (operator == '-') {
                obstacleBitmap = redBitmap;
            }

            // 绘制障碍物图片
            if (obstacleBitmap != null) {
                canvas.drawBitmap(obstacleBitmap, x, y, null);
            }

            // 绘制数字和运算符
            String text = operator + String.valueOf(number);
            canvas.drawText(text, x + obstacleWidth / 4, y + obstacleHeight / 2, textPaint);
        }

        // 绘制敌机
        if (!isAnyObstacleVisible() && enemyPlaneBitmap != null) {
            canvas.drawBitmap(enemyPlaneBitmap, enemyPlaneX, enemyPlaneY, null);

            // 绘制敌机的数字

            canvas.drawText(
                    String.valueOf((int) (0.8 * PlaneNumberAlgorithm.calculateMaxValue(left, right))),
                    enemyPlaneX + enemyPlaneBitmap.getWidth() / 2,
                    enemyPlaneY + enemyPlaneBitmap.getHeight() + 20,
                    planeTextPaint

            );

        }
    }


    public void updateGame() {
        List<Object[]> toRemove = new ArrayList<>();
        boolean generateNew = false;

        for (Object[] obstacle : obstacles) {
            obstacle[1] = (int) obstacle[1] + 20; // 障碍物向下移动

            // 检查是否到达屏幕中心
            if ((int) obstacle[1] >= screenHeight / 2 && (int) obstacle[4] == 0) {
                generateNew = true;
                obstacle[4] = 1;
            }

            // 检测碰撞
            if (checkCollision(obstacle)) {
                if (!isCollisionProcessed) {
                    isCollisionProcessed = true;
                    toRemove.add(obstacle);
                }
            }
        }

        obstacles.removeAll(toRemove);

        if (generateNew) {
            generateObstaclesAtTop();
            isCollisionProcessed = false;
        }

        // 检查是否应该开始移动敌机
        if (!isAnyObstacleVisible() && enemyPlaneBitmap != null) {
            updateEnemyPlane(); // 没有障碍物时移动敌机
        }

        invalidate(); // 触发重绘
    }

    private void updateEnemyPlane() {
        if (!isAnyObstacleVisible() && enemyPlaneBitmap != null) {
            // 敌机向下移动
            enemyPlaneY += 5;

            // 检测与玩家飞机的碰撞
            if (checkEnemyCollision()) {
                planeNumber -= 0.8 * PlaneNumberAlgorithm.calculateMaxValue(left, right); // 玩家飞机数字减少最大值的 80%

                // 检查玩家飞机数字是否大于 0
                if (planeNumber > 0) {
                    // 显示胜利弹窗
                    showVictoryDialog();
                } else {
                    // 显示失败弹窗
                    showDefeatDialog();
                }
            }


        }
    }
    private void showVictoryDialog() {
        // 使用 Context 弹出一个 AlertDialog
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Victory")
                .setMessage("Congratulation！You Win！")
                .setPositiveButton("Close", (dialog, which) -> {
                    dialog.dismiss(); // 关闭弹窗
                    // 退出游戏
                    ((android.app.Activity) getContext()).finish();
                })
                .create()
                .show();
    }
    private void showDefeatDialog() {
        // 使用 Context 弹出一个 AlertDialog
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Defeat")
                .setMessage("You Lose! Better luck next time.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    dialog.dismiss(); // 关闭弹窗
                    // 重新开始游戏
                    resetGame();
                })
                .setNegativeButton("Exit", (dialog, which) -> {
                    dialog.dismiss(); // 关闭弹窗
                    ((android.app.Activity) getContext()).finish(); // 退出游戏
                })
                .create()
                .show();
    }
    private void resetGame() {
        // 重置飞机数字
        planeNumber = 1;

        // 重置敌机位置
        enemyPlaneY = screenHeight / 4 - enemyPlaneBitmap.getHeight();

        // 重置障碍物和数据
        leftData = PlaneNumberAlgorithm.generateNumberSet(12);
        rightData = PlaneNumberAlgorithm.generateNumberSet(12);
        obstacles.clear();

        // 初始化游戏
        initGame(screenWidth, screenHeight);

        invalidate(); // 触发重绘
    }



    private boolean checkEnemyCollision() {
        return planeY - enemyPlaneY == 2;
    }


    private void generateObstacles() {
        // 清空现有障碍物
        obstacles.clear();

        // 从左侧和右侧数据中取值
        if (leftData != null && !leftData.isEmpty()) {
            Object[] leftObstacle = leftData.remove(0); // 取出左侧数据
            obstacles.add(new Object[]{leftCenterX, -obstacleHeight, leftObstacle[0], leftObstacle[1], 0}); // 数字和运算符
        }

        if (rightData != null && !rightData.isEmpty()) {
            Object[] rightObstacle = rightData.remove(0); // 取出右侧数据
            obstacles.add(new Object[]{rightCenterX, -obstacleHeight, rightObstacle[0], rightObstacle[1], 0}); // 数字和运算符
        }
    }

    private void generateObstaclesAtTop() {
        // 从左侧和右侧数据中取值
        if (leftData != null && !leftData.isEmpty()) {
            Object[] leftObstacle = leftData.remove(0); // 取出左侧数据
            obstacles.add(new Object[]{leftCenterX, -obstacleHeight, leftObstacle[0], leftObstacle[1], 0}); // 数字和运算符
        }

        if (rightData != null && !rightData.isEmpty()) {
            Object[] rightObstacle = rightData.remove(0); // 取出右侧数据
            obstacles.add(new Object[]{rightCenterX, -obstacleHeight, rightObstacle[0], rightObstacle[1], 0}); // 数字和运算符
        }
    }

    private boolean checkCollision(Object[] obstacle) {
        int obstacleX = (int) obstacle[0]; // x 坐标
        int obstacleY = (int) obstacle[1]; // y 坐标

        boolean isCollided = planeX < obstacleX + obstacleWidth &&
                planeX + planeBitmap.getWidth() > obstacleX &&
                planeY < obstacleY + obstacleHeight &&
                planeY + planeBitmap.getHeight() > obstacleY;

        if (isCollided && !isCollisionProcessed) {
            int number = (int) obstacle[2];  // 障碍物的数字
            char operator = (char) obstacle[3]; // 障碍物的运算符

            // 根据运算符执行加减乘除
            switch (operator) {
                case '+':
                    planeNumber += number;
                    break;
                case '-':
                    planeNumber -= number;
                    break;
                case '*':
                    planeNumber *= number;
                    break;
                case '/':
                    if (number != 0) { // 防止除以零
                        planeNumber /= number;
                    }
                    break;
            }

            return true;
        }

        return isCollided;
    }

    public void movePlaneLeft() {
        int centerX = screenWidth / 4 - planeBitmap.getWidth() / 2; // 左侧中心点
        planeX = centerX;
        invalidate(); // 重新绘制
    }

    public void movePlaneRight() {
        int centerX = 3 * screenWidth / 4 - planeBitmap.getWidth() / 2; // 右侧中心点
        planeX = centerX;
        invalidate(); // 重新绘制
    }

    private Bitmap removeBackgroundColor(Bitmap bitmap, int backgroundColor) {
        int tolerance = 15; // 容差
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 确保支持透明通道
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = bitmap.getPixel(x, y);
                if (isColorSimilar(pixel, backgroundColor, tolerance)) {
                    result.setPixel(x, y, android.graphics.Color.TRANSPARENT); // 设置为透明
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
