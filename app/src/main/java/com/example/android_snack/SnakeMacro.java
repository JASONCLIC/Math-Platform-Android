package com.example.android_snack;

/**
 * The Macro class: Defines constants used throughout the application.
 */
public class SnakeMacro {
    /**
     * The constant Up: Represents the snake's head direction as up.
     */
    public static final int Up = 0x11;
    /**
     * The constant Dwon: Represents the snake's head direction as down.
     */
    public static final int Dwon = 0x12;
    /**
     * The constant Left: Represents the snake's head direction as left.
     */
    public static final int Left = 0x13;
    /**
     * The constant Right: Represents the snake's head direction as right.
     */
    public static final int Right = 0x14;
    /**
     * The constant UNIT: Basic unit of movement for the snake.
     */
    public static final int UNIT = 50;
    /**
     * The constant LEAST_SIZE: Minimum unit size of the snake's living area.
     */
    public static final int LEAST_SIZE = 1;
    /**
     * The constant DELAY: Timer delay before running the first action.
     */
    public static final int DELAY = 100;
    /**
     * The constant PERIOD: Interval time for the timer to start.
     */
    public static final int PERIOD = 500;
    /**
     * The constant INIT_SNACKX: Initial X-coordinate of the snake's head.
     */
    public static final int INIT_SNACKX = 600;
    /**
     * The constant INIT_SNACKY: Initial Y-coordinate of the snake's head.
     */
    public static final int INIT_SNACKY = 400;
    /**
     * The constant INIT_FOODX: Initial X-coordinate of the food.
     */
    public static final int INIT_FOODX = 900;
    /**
     * The constant INIT_FOODY: Initial Y-coordinate of the food.
     */
    public static final int INIT_FOODY = 400;
    /**
     * The constant INIT_LENGHT: Initial length of the snake.
     */
    public static final int INIT_LENGHT = 3;
    /**
     * The constant IMAGE_HEAD_SIZE: Scale factor for the snake's head image.
     */
    public static final int IMAGE_HEAD_SIZE = 20;
    /**
     * The constant IMAGE_BODY_SIZE: Scale factor for the snake's body image.
     */
    public static final int IMAGE_BODY_SIZE = 46;
    /**
     * The constant SCORE_UNIT: Points awarded for each food eaten.
     */
    public static final int SCORE_UNIT = 10;
    /**
     * The constant MAX_LENGHT: Maximum length of the snake.
     */
    public static final int MAX_LENGHT = 900;
    /**
     * The constant SPEED: Base unit of speed increase when food is eaten.
     */
    public static final int SPEED = 30;
    /**
     * The constant FOOD: Base unit indicating food presence or properties.
     */
    public static final int FOOD = 30;
    /**
     * The constant MAX_DSPEED: Maximum speed increment for the snake.
     */
    public static final int MAX_DSPEED = 400;

    /**
     * The constant LENGTH_SPEED_INCREMENT: Speed increase for every additional 5 units of length.
     */
    public static final int LENGTH_SPEED_INCREMENT = 5;
    /**
     * The constant LENGTH_THRESHOLD: Minimum length to start increasing speed.
     */
    public static final int LENGTH_THRESHOLD = 10;
    /**
     * The constant MAX_SPEED: Maximum achievable speed for the snake.
     */
    public static final int MAX_SPEED = 400;

    /**
     * The constant Pname: Filename for storing the highest score.
     */
    public static final String Pname = "scorefile";
}
