package io.github.subhamtyagi.quickcalculation.factory;

import java.util.Random;

public class GenerateQuestion {

    public static Question multiplication(int range_min, int range_max, int range_min2, int range_max2) {
        Random rand = new Random();
        int a = rand.nextInt(range_max - range_min + 1) + range_min;
        int b = rand.nextInt(range_max2 - range_min2 + 1) + range_min2;
        return new Question(a + " x " + b, a * b);
        // "The End, Bas Yahi tak tha jo tha ";

    }

    public static Question subtract(int range_min, int range_max, int range_min2, int range_max2) {
        Random rand = new Random();
        int a = rand.nextInt(range_max - range_min + 1) + range_min;
        int b = rand.nextInt(range_max2 - range_min2 + 1) + range_min2;
        return new Question(a + " - " + b, a - b);
        // "The End, Bas Yahi tak tha jo tha ";

    }

    public static Question addition(int range_min, int range_max, int range_min2, int range_max2) {
        Random rand = new Random();
        int a = rand.nextInt(range_max - range_min + 1) + range_min;
        int b = rand.nextInt(range_max2 - range_min2 + 1) + range_min2;
        return new Question(a + " + " + b, a + b);
    }

    public static Question division(int range_min, int range_max, int range_min2, int range_max2) {
        Random rand = new Random();
        int dividend, divisor;
        do {
            int a = rand.nextInt(range_max - range_min + 1) + range_min;
            int b = rand.nextInt(range_max2 - range_min2 + 1) + range_min2;
            dividend = a * b;
            divisor = Math.min(a, b);
        } while (divisor == 0);
        int answer = dividend / divisor;
        return new Question(dividend + "÷" + divisor, answer);
    }

    public static Question mix(int mLowerRange1, int mUpperRange1, int mLowerRange2, int mUpperRange2) {
        Random rand = new Random();
        int a = rand.nextInt(7);
        switch (a) {
            case 0:
                return multiplication(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
            case 1:
                return addition(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
            case 2:
                return subtract(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
            case 3:
                return simplification(mLowerRange1, mUpperRange1);
            case 4:
                return simplificationAdvance(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
            case 5:
                return sumSeries(mLowerRange1, mUpperRange1);
            default:
                return division(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);

        }
    }

    public static Question sumSeries(int range_min, int range_max) {
        Random rand = new Random();
        int a = rand.nextInt(range_max - range_min + 1) + range_min;
        int depth = 0;
        while (depth == 0) depth = rand.nextInt(7);
        int answer = a;
        StringBuilder questionBuilder = new StringBuilder();
        questionBuilder.append(a);
        for (int i = 0; i < depth; i++) {
            questionBuilder.append("+");
            a = rand.nextInt(range_max - range_min + 1) + range_min;
            questionBuilder.append(a);
            answer += a;
        }
        return new Question(questionBuilder.toString(), answer);
    }

    public static Question simplification(int range_min, int range_max) {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();
        int length = random.nextInt(10 - 2 + 1) + 2;
        int j = 0;
        int answer = 0;
        int number1;
        boolean sign = false;
        do {
            number1 = random.nextInt(range_max - range_min + 1) + range_min;
            if (sign) {
                answer -= number1;
            } else {
                answer += number1;
            }
            sign = random.nextBoolean();
            stringBuffer.append(number1);
            j++;
            if (j != length) {
                if (sign) {
                    stringBuffer.append("-");
                } else stringBuffer.append("+");
            }
        } while (j < length);
        return new Question(stringBuffer.toString(), answer);

    }

    public static Question simplificationAdvance(int range_min, int range_max, int range_min2, int range_max2) {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();

        int length = random.nextInt(10 - 2 + 1) + 2;
        int j = 0;
        int answer = 0;
        int number1;
        int number2;
        int number3;
        boolean sign = false;

        Random random1 = new Random();
        int ops;
        Node node;

        do {
            number1 = random.nextInt(range_max - range_min + 1) + range_min;
            number2 = random.nextInt(range_max2 - range_min2 + 1) + range_min2;
            do {
                number3 = random.nextInt(range_max - range_min + 1) + range_min;
            } while (number3 == 0);

            ops = random1.nextInt(4);
            switch (ops) {
                case 2:
                    node = generateTreeNode(number1, number2, number3, "x");
                    break;
                case 3: {
                    while (number1 == 0)
                        number1 = random.nextInt(range_max - range_min + 1) + range_min;
                    while (number2 == 0)
                        number2 = random.nextInt(range_max2 - range_min2 + 1) + range_min2;
                    node = generateTreeNode(number1 * number2, number2, number3, "/");
                    break;
                }
                default:
                    node = new Node(number1 + "", number1);
                    break;

            }
            number1 = node.second;
            if (sign) {
                answer -= number1;
            } else {
                answer += number1;
            }

            sign = random.nextBoolean();
            stringBuffer.append(node.first);
            j++;
            if (j != length) {
                if (sign) {
                    stringBuffer.append("-");
                } else stringBuffer.append("+");
            }

        } while (j < length);
        return new Question(stringBuffer.toString(), answer);
    }

    private static Node generateTreeNode(int n1, int n2, int n3, String operator) {
        Random ra = new Random();
        int depth = ra.nextInt(8);
        switch (operator) {
            case "x": {
                if (depth < 5) {
                    return new Node(n1 + "x" + n2 + "x" + n3, n1 * n2 * n3);
                } else
                    return new Node(n1 + "x" + n2, n1 * n2);
            }
            case "/": {
                if (depth < 3) {
                    int temp = n1 * n3;
                    return new Node(temp + "÷" + n2 + "÷" + n3, n1 / n2);
                } else if (depth < 5) {
                    return new Node(n1 + "÷" + n2 + "x" + n3, n1 * n3 / n2);
                }
                return new Node(n1 + "÷" + n2, n1 / n2);
            }
            default:
                return new Node(n1 + "", n1);// never happen
        }
    }

    private static class Node {
        String first;
        int second;

        public Node(String s, int i) {
            first = s;
            second = i;
        }
    }
}


