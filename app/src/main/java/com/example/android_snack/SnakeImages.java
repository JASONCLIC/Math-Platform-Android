package com.example.android_snack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SnakeImages extends View {
    private int BitmapX, BitmapY;
    private int[] FoodX = new int[3]; // Stores the X coordinates of three food items
    private int[] FoodY = new int[3]; // Stores the Y coordinates of three food items

    private int[] snackX = new int[SnakeMacro.MAX_LENGHT];
    private int[] snackY = new int[SnakeMacro.MAX_LENGHT];
    private int lenght;
    private int direction;
    private boolean islive;

    private int snakeNumber = 0; // The number displayed on the snake's head
    private int[] foodNumbers = new int[3]; // Stores numbers on the three food items
    private int target; // Stores the target random number

    private SnakeRandomNumberGenerator numberGenerator; // Instance of the random number generator

    public SnakeImages(Context context) {
        super(context);
        numberGenerator = new SnakeRandomNumberGenerator(); // Initialize random number generator
        target = numberGenerator.generate(); // Generate target number
        initFoodNumbers(); // Initialize numbers on the food items
    }

    public int getSnakeNumber() {
        return snakeNumber;
    }

    public void setSnakeNumber(int snakeNumber) {
        this.snakeNumber = snakeNumber;
    }

    public SnakeRandomNumberGenerator getNumberGenerator() {
        return numberGenerator;
    }

    public void initFoodNumbers() {
        int[] numbers = numberGenerator.generateThreeNumbers(target); // Generate three numbers related to the target
        foodNumbers[0] = numbers[0];
        foodNumbers[1] = numbers[1];
        foodNumbers[2] = numbers[2];
    }

    public int getTarget() {
        return target; // Getter for the target value
    }

    // Getters and setters for foodNumbers
    public int getFoodNumbers(int index) {
        return foodNumbers[index];
    }

    public void setFoodNumber(int index, int foodNumber) {
        foodNumbers[index] = foodNumber;
    }

    // Getters and setters for FoodX
    public int getFoodX(int index) {
        return FoodX[index];
    }

    public void setFoodX(int index, int foodX) {
        FoodX[index] = foodX;
    }

    // Getters and setters for FoodY
    public int getFoodY(int index) {
        return FoodY[index];
    }

    public void setFoodY(int index, int foodY) {
        FoodY[index] = foodY;
    }

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getBitmapX() {
        return BitmapX;
    }

    public void setBitmapX(int bitmapX) {
        BitmapX = bitmapX;
    }

    public int getBitmapY() {
        return BitmapY;
    }

    public void setBitmapY(int bitmapY) {
        BitmapY = bitmapY;
    }

    /**
     * Init: Initialize the snake's starting position and length
     */
    public void Init() {
        BitmapX = SnakeMacro.INIT_SNACKX;
        BitmapY = SnakeMacro.INIT_SNACKY;

        for (int i = 0; i < 3; i++) {
            FoodX[i] = SnakeMacro.INIT_FOODX + i * SnakeMacro.UNIT; // Set initial food positions as needed
            FoodY[i] = SnakeMacro.INIT_FOODY;
        }

        snackX[0] = BitmapX;
        snackY[0] = BitmapY;
        lenght = SnakeMacro.INIT_LENGHT;
        direction = SnakeMacro.Right;
        islive = true;

        for (int i = 1; i < lenght; i++) {
            snackX[i] = snackX[i - 1] - SnakeMacro.UNIT;
            snackY[i] = snackY[i - 1];
        }
    }

    private Bitmap rbitmaphead, lbitmaphead, ubitmaphead, dbitmaphead, bitmapbody;
    private BitmapFactory.Options headoptions, bodyoptions;

    /**
     * Size: Load images of the snake's head in different directions and body, and set their sizes
     */
    public void Size() {
        headoptions = new BitmapFactory.Options();
        headoptions.inSampleSize = SnakeMacro.IMAGE_HEAD_SIZE;
        headoptions.inJustDecodeBounds = false;

        bodyoptions = new BitmapFactory.Options(); // Options for decoding the Bitmap with specified configurations
        bodyoptions.inSampleSize = SnakeMacro.IMAGE_BODY_SIZE;
        bodyoptions.inJustDecodeBounds = false;

        rbitmaphead = BitmapFactory.decodeResource(this.getResources(), R.drawable.rhead, headoptions);
        ubitmaphead = BitmapFactory.decodeResource(this.getResources(), R.drawable.head, headoptions);
        lbitmaphead = BitmapFactory.decodeResource(this.getResources(), R.drawable.lhead, headoptions);
        dbitmaphead = BitmapFactory.decodeResource(this.getResources(), R.drawable.dhead, headoptions);
        bitmapbody = BitmapFactory.decodeResource(this.getResources(), R.drawable.body, bodyoptions);
    }

    /**
     * onDraw: Draw the snake on the screen. The canvas is used to render graphics.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(); // Create a new Paint object for drawing
        paint.setTextSize(40); // Set font size
        paint.setColor(Color.WHITE); // Set font color to white

        // Draw food items
        for (int i = 0; i < 3; i++) {
            if (FoodX[i] == -1 && FoodY[i] == -1 && foodNumbers[i] == 0) {
                continue; // Skip if food item is marked as "deleted"
            }

            canvas.drawBitmap(bitmapbody, FoodX[i] - 4, FoodY[i], paint);
            canvas.drawText(String.valueOf(foodNumbers[i]), FoodX[i] - 10, FoodY[i] - 10, paint); // Display food number
        }
        // Display the initial random target number
        //String initialRandomNumbers = getContext().getString(R.string.initial_random_number, target);
        //canvas.drawText(initialRandomNumbers, 600, 20, paint); // Draw the initial random number at specified location

        // Draw the snake's head
        snackX[0] = BitmapX;
        snackY[0] = BitmapY;
        switch (direction) {
            case SnakeMacro.Right:
                canvas.drawBitmap(rbitmaphead, snackX[0], snackY[0], paint); // Snake head facing right
                break;
            case SnakeMacro.Up:
                canvas.drawBitmap(ubitmaphead, snackX[0], snackY[0], paint);
                break;
            case SnakeMacro.Left:
                canvas.drawBitmap(lbitmaphead, snackX[0], snackY[0], paint);
                break;
            case SnakeMacro.Dwon:
                canvas.drawBitmap(dbitmaphead, snackX[0], snackY[0], paint);
                break;
        }

        // Adjust the position of the number on the snake's head according to the direction
        int textX = BitmapX; // Default X position for the number
        int textY = BitmapY; // Default Y position for the number

        switch (direction) {
            case SnakeMacro.Right:
                textX += SnakeMacro.UNIT + 5; // Number displayed to the right when moving right
                textY += 35; // Centering adjustment
                break;
            case SnakeMacro.Left:
                textX -= SnakeMacro.UNIT; // Number displayed to the left when moving left
                textY += 35; // Centering adjustment
                break;
            case SnakeMacro.Up:
                textY -= SnakeMacro.UNIT - 30; // Number displayed above when moving up
                textX += 10; // Centering adjustment
                break;
            case SnakeMacro.Dwon:
                textY += SnakeMacro.UNIT + 40; // Number displayed below when moving down
                textX += 10; // Centering adjustment
                break;
        }

        // Draw the number on the snake's head
        canvas.drawText(String.valueOf(snakeNumber), textX, textY, paint);

        // Draw the snake's body segments
        for (int i = 1; i < lenght; i++) {
            canvas.drawBitmap(bitmapbody, snackX[i], snackY[i], paint);
        }

        // Check if the snake bites its own body
        for (int i = 1; i < lenght; i++) {
            if (snackX[0] == snackX[i] && snackY[0] == snackY[i]) {
                islive = false;
            }
        }

        // Move each body segment forward to the position of the previous segment
        for (int i = lenght; i > 0; i--) {
            snackX[i] = snackX[i - 1];
            snackY[i] = snackY[i - 1];
        }
    }

    /**
     * Returns true if this bitmap has been recycled. If so, then it is an error
     * to try to access its pixels, and the bitmap will not draw.
     * If the bitmap has already been recycled, returns true
     * @return true if the bitmap has been recycled
     */

    // Recycling bitmaps
    protected void finalize() throws Throwable {
        super.finalize();
        if (rbitmaphead != null && !rbitmaphead.isRecycled()) {
            rbitmaphead.recycle();
        }
        if (bitmapbody != null && !bitmapbody.isRecycled()) {
            bitmapbody.recycle();
        }
        if (ubitmaphead != null && !ubitmaphead.isRecycled()) {
            ubitmaphead.recycle();
        }
        if (lbitmaphead != null && !lbitmaphead.isRecycled()) {
            lbitmaphead.recycle();
        }
        if (dbitmaphead != null && !dbitmaphead.isRecycled()) {
            dbitmaphead.recycle();
        }
    }
}
