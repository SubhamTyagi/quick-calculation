package io.github.subhamtyagi.quickcalculation.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Question {
    private final ArrayList<Integer> arrayList = new ArrayList<>(4);
    private final String question;
    private final int correctAnswer;

    Question(String question, int correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;

        Random r = new Random();
        boolean b = r.nextBoolean();
        boolean c = r.nextBoolean();
        if (b) {
            arrayList.add(correctAnswer + 30);
            arrayList.add(correctAnswer + 10);
            arrayList.add(correctAnswer + 20);

        } else if (c) {
            arrayList.add(correctAnswer - 30);
            arrayList.add(correctAnswer + 10);
            arrayList.add(correctAnswer - 20);
        } else {
            arrayList.add(correctAnswer - 30);
            arrayList.add(correctAnswer - 10);
            arrayList.add(correctAnswer - 20);
        }
        arrayList.add(correctAnswer);
        Collections.shuffle(arrayList);

    }

    @SuppressWarnings("unused")
    Question(String question, int correctAnswer, int wrongAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;

        Random r = new Random();
        boolean b = r.nextBoolean();
        boolean c = r.nextBoolean();
        if (b) {
            arrayList.add(wrongAnswer);
            arrayList.add(correctAnswer + 10);
            arrayList.add(correctAnswer + 20);

        } else if (c) {
            arrayList.add(wrongAnswer);
            arrayList.add(correctAnswer - 10);
            arrayList.add(correctAnswer - 20);
        } else {
            arrayList.add(wrongAnswer);
            arrayList.add(correctAnswer - 10);
            arrayList.add(correctAnswer + 20);
        }
        arrayList.add(correctAnswer);
        Collections.shuffle(arrayList);

    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return arrayList.get(0).toString();
    }

    public String getOption2() {
        return arrayList.get(1).toString();
    }

    public String getOption3() {
        return arrayList.get(2).toString();
    }

    public String getOption4() {
        return arrayList.get(3).toString();
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }


}
