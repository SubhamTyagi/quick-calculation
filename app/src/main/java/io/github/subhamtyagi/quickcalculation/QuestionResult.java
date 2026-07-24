package io.github.subhamtyagi.quickcalculation;

import java.io.Serializable;

public class QuestionResult implements Serializable {
    private String question;
        private String userAnswer;
            private String correctAnswer;

                public QuestionResult(String question, String userAnswer, String correctAnswer) {
                        this.question = question;
                                this.userAnswer = userAnswer;
                                        this.correctAnswer = correctAnswer;
                                            }

                                                public String getQuestion() { return question; }
                                                    public String getUserAnswer() { return userAnswer; }
                                                        public String getCorrectAnswer() { return correctAnswer; }
                                                        }
                                                        