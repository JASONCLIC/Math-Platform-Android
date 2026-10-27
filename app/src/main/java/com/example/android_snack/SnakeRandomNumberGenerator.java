package com.example.android_snack;

import java.util.Random;

public class SnakeRandomNumberGenerator {
    private Random random = new Random();

    // Generates a random natural number between 3 and 15
    public int generate() {
        return 3 + random.nextInt(15);
    }

    // Generates three numbers based on specified conditions
    public int[] generateThreeNumbers(int target) {

        int num1 = -10 + random.nextInt(target + 11); // Random number between -10 and target

        int num2 = target - num1; // Ensures num1 and num2 sum up to target

        // Generates a third number, which could be positive or negative
        int num3 = random.nextInt(5) - 2; // Generates a random number between -2 and 2

        return new int[]{num1, num2, num3}; // Returns the three numbers
    }
}
