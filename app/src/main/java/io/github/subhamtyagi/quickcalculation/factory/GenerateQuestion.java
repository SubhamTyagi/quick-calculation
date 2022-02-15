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

    public static Question addSubs(int range_min, int range_max) {
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
}


