package com.example.android_snack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MathPlatformMain extends BaseActivity {
    private int score = 0; // 用户得分
    private int totalQuestions = 0; // 总答题数
    private Switch petSwitch; // 添加开关控件变量
    private ScoreChartView scoreChart; // 添加这行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_platform_main);

        // 初始化 Practice 页面元素
        LinearLayout practiceContent = findViewById(R.id.practice_content);
        TextView practiceQuestion = findViewById(R.id.practice_question);
        EditText practiceAnswer = findViewById(R.id.practice_answer);
        Button submitAnswerButton = findViewById(R.id.submit_answer_button);
        TextView practiceFeedback = findViewById(R.id.practice_feedback);

        TextView scoreTextView = findViewById(R.id.practice_score); // 显示分数的 TextView
        scoreTextView.setText("Score: " + score); // 初始分数


        // 初始化 Guide 页面元素
        ScrollView guideContent = findViewById(R.id.guide_content);
        TextView multiplicationTable = findViewById(R.id.multiplication_table);
        Button guideButton = findViewById(R.id.guide_button);
        Button practiceButton = findViewById(R.id.practice_button);

        ScrollView myAccountContent = findViewById(R.id.my_account_content);
        TextView accountScore = findViewById(R.id.account_score);
        TextView accountAccuracy = findViewById(R.id.account_accuracy);
        Button myAccountButton = findViewById(R.id.my_account_button); // 初始化按钮


        scoreChart = findViewById(R.id.scoreChart);


        // 初始化 Game 按钮
        Button gameButton = findViewById(R.id.game_button);

        // 随机生成题目变量
        Random random = new Random();
        int[] currentQuestion = new int[2]; // 当前问题的两个随机数

        // 默认显示 Practice 页面
        generateNewQuestion(random, practiceQuestion, currentQuestion);

        // 九九乘法表生成逻辑
        StringBuilder table = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                // 使用 String.format 格式化每组乘法表
                table.append(String.format("%d×%d=%-3d ", j, i, i * j));
            }
            table.append("\n"); // 每组结束后换行
        }
        multiplicationTable.setText(table.toString());


        // 切换到 Guide 页面
        guideButton.setOnClickListener(view -> {
            practiceContent.setVisibility(View.GONE);
            myAccountContent.setVisibility(View.GONE);
            guideContent.setVisibility(View.VISIBLE);
        });

        // 返回到 Practice 页面
        practiceButton.setOnClickListener(view -> {
            guideContent.setVisibility(View.GONE);
            myAccountContent.setVisibility(View.GONE);
            practiceContent.setVisibility(View.VISIBLE);
        });

        submitAnswerButton.setOnClickListener(v -> {
            String userAnswer = practiceAnswer.getText().toString().trim();
            if (!userAnswer.isEmpty()) {
                int correctAnswer = currentQuestion[0] * currentQuestion[1];
                totalQuestions++; // 增加总答题数
                if (Integer.parseInt(userAnswer) == correctAnswer) {
                    score++; // 答对增加得分
                    scoreTextView.setText("Score: " + score);
                    practiceFeedback.setText("Correct! Well done.");
                    practiceFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                    // 更新图表
                    if (scoreChart != null) {
                        double accuracy = (score * 100.0) / totalQuestions;
                        scoreChart.updateScore(score, (float)accuracy);
                    }

                    generateNewQuestion(random, practiceQuestion, currentQuestion);
                } else {
                    practiceFeedback.setText("Wrong! Try again.");
                    practiceFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                    // 即使答错也要更新图表
                    if (scoreChart != null) {
                        double accuracy = (score * 100.0) / totalQuestions;
                        scoreChart.updateScore(score, (float)accuracy);
                    }
                }
                practiceAnswer.setText("");
            } else {
                practiceFeedback.setText("Please enter an answer.");
                practiceFeedback.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        });


        // 切换到 Game 页面
        gameButton.setOnClickListener(v -> {
            Intent intent = new Intent(MathPlatformMain.this, MathPlatformGame.class);
            startActivity(intent);
        });


        myAccountButton.setOnClickListener(view -> {
            // 隐藏其他页面
            practiceContent.setVisibility(View.GONE);
            guideContent.setVisibility(View.GONE);
            myAccountContent.setVisibility(View.VISIBLE);

            // 更新 My Account 页面内容
            double accuracy = totalQuestions > 0 ? (score * 100.0) / totalQuestions : 0; // 计算正确率
            accountScore.setText("Total Score: " + score);
            accountAccuracy.setText("Accuracy: " + String.format("%.2f", accuracy) + "%");

            // 更新图表
            scoreChart.updateScore(score, (float)accuracy);
        });

        // 初始化桌宠开关
        petSwitch = findViewById(R.id.petSwitch);

    // 设置初始状态
        petSwitch.setChecked(PetManager.getInstance(this).isPetVisible());

    // 添加开关监听器
        petSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PetManager.getInstance(this).setPetVisible(isChecked);
            updatePetVisibility();
        });



    }

    // 随机生成新的数学题目
    private void generateNewQuestion(Random random, TextView questionView, int[] currentQuestion) {
        currentQuestion[0] = random.nextInt(9) + 1; // 生成 1 到 9 的随机数
        currentQuestion[1] = random.nextInt(9) + 1;
        questionView.setText("What is " + currentQuestion[0] + " × " + currentQuestion[1] + "?");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePetVisibility();
    }
}
