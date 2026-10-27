package com.example.android_snack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ScoreChartView extends View {
    private Paint axisPaint;
    private Paint gridPaint;
    private Paint pointPaint;
    private Paint textPaint;
    private float score = 0;
    private float accuracy = 0;

    public ScoreChartView(Context context) {
        super(context);
        init();
    }

    public ScoreChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(4);
        axisPaint.setStyle(Paint.Style.STROKE);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1);
        gridPaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
    }

    public void updateScore(float score, float accuracy) {
        this.score = score;
        this.accuracy = accuracy;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        // 绘制坐标轴
        canvas.drawLine(padding, height - padding, width - padding, height - padding, axisPaint); // X轴
        canvas.drawLine(padding, padding, padding, height - padding, axisPaint); // Y轴

        // 绘制网格线
        for (int i = 1; i < 10; i++) {
            float x = padding + (width - 2 * padding) * i / 10f;
            float y = padding + (height - 2 * padding) * i / 10f;

            canvas.drawLine(x, height - padding, x, padding, gridPaint);
            canvas.drawLine(padding, y, width - padding, y, gridPaint);
        }

        // 绘制坐标轴标签
        canvas.drawText("Score", width/2, height - 10, textPaint);
        canvas.save();
        canvas.rotate(-90);
        canvas.drawText("Accuracy(%)", -height/2, 30, textPaint);
        canvas.restore();

        // 绘制数据点
        float maxScore = Math.max(20, score); // 动态调整最大分数
        float x = padding + (width - 2 * padding) * (score / maxScore);
        float y = height - padding - (height - 2 * padding) * (accuracy / 100f);
        canvas.drawCircle(x, y, 10, pointPaint);
    }
}