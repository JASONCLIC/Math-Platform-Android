package com.example.android_snack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaneNumberAlgorithm {
    private static final Random random = new Random();

    // 生成一组较小数字和随机运算符
    public static List<Object[]> generateNumberSet(int count) {
        List<Object[]> group = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int number = generateSmallRandomNumber();  // 生成较小随机数字
            char operator = generateRandomOperator(); // 生成随机运算符
            group.add(new Object[] {number, operator}); // 保存到组中
        }
        return group;
    }

    // 生成较小随机数字（如 1 到 3）
    private static int generateSmallRandomNumber() {
        return 1 + random.nextInt(3); // [1, 3]
    }

    // 随机生成一个运算符
    private static char generateRandomOperator() {
        char[] operators = {'+', '-', '*'};
        return operators[random.nextInt(operators.length)];
    }

    // 递归计算所有可能路径
    public static double[] calculateMaxMin(double startValue, List<Object[]> group1, List<Object[]> group2, int index, StringBuilder maxLogic, StringBuilder minLogic) {
        if (index >= group1.size()) {
            return new double[] {startValue, startValue}; // [max, min]
        }

        // 获取当前组的数字和操作符
        Object[] g1Pair = group1.get(index);
        Object[] g2Pair = group2.get(index);

        int g1Number = (int) g1Pair[0];
        char g1Operator = (char) g1Pair[1];

        int g2Number = (int) g2Pair[0];
        char g2Operator = (char) g2Pair[1];

        // 创建两个逻辑路径
        StringBuilder maxLogic1 = new StringBuilder(maxLogic);
        StringBuilder maxLogic2 = new StringBuilder(maxLogic);

        StringBuilder minLogic1 = new StringBuilder(minLogic);
        StringBuilder minLogic2 = new StringBuilder(minLogic);

        // 分别计算两条路径
        double result1 = applyOperation(startValue, g1Operator, g1Number);
        double result2 = applyOperation(startValue, g2Operator, g2Number);

        maxLogic1.append(" ").append(g1Operator).append(" ").append(g1Number);
        maxLogic2.append(" ").append(g2Operator).append(" ").append(g2Number);

        minLogic1.append(" ").append(g1Operator).append(" ").append(g1Number);
        minLogic2.append(" ").append(g2Operator).append(" ").append(g2Number);

        // 递归到下一步
        double[] next1 = calculateMaxMin(result1, group1, group2, index + 1, maxLogic1, minLogic1);
        double[] next2 = calculateMaxMin(result2, group1, group2, index + 1, maxLogic2, minLogic2);

        // 找出最大值和最小值
        double maxResult = Math.max(next1[0], next2[0]);
        double minResult = Math.min(next1[1], next2[1]);

        // 保存最大值逻辑
        if (maxResult == next1[0]) {
            maxLogic.setLength(0);
            maxLogic.append(maxLogic1);
        } else {
            maxLogic.setLength(0);
            maxLogic.append(maxLogic2);
        }

        // 保存最小值逻辑
        if (minResult == next1[1]) {
            minLogic.setLength(0);
            minLogic.append(minLogic1);
        } else {
            minLogic.setLength(0);
            minLogic.append(minLogic2);
        }

        return new double[] {maxResult, minResult};
    }

    // 应用操作
    private static double applyOperation(double currentValue, char operator, int number) {
        switch (operator) {
            case '+':
                return currentValue + number;
            case '-':
                return currentValue - number;
            case '*':
                return currentValue * number;
            default:
                return currentValue;
        }
    }

    public static void main(String[] args) {
        // 生成两组数字和运算符
        List<Object[]> group1 = generateNumberSet(12);
        List<Object[]> group2 = generateNumberSet(12);

        // 输出两组数字和运算符
        System.out.println("第一组数字和运算符：" + formatNumberSet(group1));
        System.out.println("第二组数字和运算符：" + formatNumberSet(group2));

        // 起始值为 1
        double startValue = 1;

        // 计算最大值和最小值
        StringBuilder maxLogic = new StringBuilder("Start");
        StringBuilder minLogic = new StringBuilder("Start");
        double[] results = calculateMaxMin(startValue, group1, group2, 0, maxLogic, minLogic);

        // 输出结果
        System.out.println("最大逻辑运算：" + maxLogic);
        System.out.println("最小逻辑运算：" + minLogic);
        System.out.println("最大值：" + results[0]);
        System.out.println("最小值：" + results[1]);
    }

    // 格式化输出一组数字和运算符
    public static String formatNumberSet(List<Object[]> group) {
        StringBuilder sb = new StringBuilder("Start");
        for (Object[] pair : group) {
            sb.append(" ").append(pair[1]).append(" ").append(pair[0]);
        }
        return sb.toString();
    }
    public static int calculateMaxValue(List<Object[]> leftData, List<Object[]> rightData) {


        double startValue = 1;

        // 计算最大值和最小值
        StringBuilder maxLogic = new StringBuilder("Start");
        StringBuilder minLogic = new StringBuilder("Start");
        double[] results = calculateMaxMin(startValue, leftData, rightData, 0, maxLogic, minLogic);

        // 输出结果
        System.out.println("最大逻辑运算：" + maxLogic);
        System.out.println("最小逻辑运算：" + minLogic);
        System.out.println("最大值：" + results[0]);
        System.out.println("最小值：" + results[1]);
        return (int) results[0]; // 将结果转换为整数并返回
    }

}
