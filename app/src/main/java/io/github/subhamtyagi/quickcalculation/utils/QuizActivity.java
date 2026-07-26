package io.github.subhamtyagi.quickcalculation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.DynamicColors;

import io.github.subhamtyagi.quickcalculation.factory.GenerateQuestion;
import io.github.subhamtyagi.quickcalculation.factory.Question;
import io.github.subhamtyagi.quickcalculation.utils.Utils;
import io.github.subhamtyagi.quickcalculation.utils.SpUtil;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private ConstraintLayout mReportCardView;
    private GridLayout mOptionsGridLayout;
    private EditText mAnswerInput;
    private Button mOption1Button, mOption2Button, mOption3Button, mOption4Button;
    private Button mPlayAgainButton, mSubmitEndButton, mSkipButton;
    private TextView mScoreTextView, mTimerTextView, mQuestionTextView, mResultTextView;
    private TextView mTotalAttemptTextView, mTotalCorrectTextView;
    
    private RecyclerView mRecyclerView;
    private QuestionReviewAdapter mAdapter;

    private Vibrator vibrator;
    private CountDownTimer countDownTimer;

    private int mCorrectCount = 0;
    private int mTotalQuestionCount = 0;
    private int mLowerRange1, mLowerRange2, mUpperRange1, mUpperRange2;

    private String operation = null;
    private String timer = null;
    private boolean isKeyboardMode = false; 
    private boolean isQuizEnded = false; 

    private Question question;
    boolean isVibrationEnable;

    private ArrayList<QuestionResult> questionResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.activity_quiz);
        SpUtil.getInstance().init(this);

        mOptionsGridLayout = findViewById(R.id.options);
        mAnswerInput = findViewById(R.id.et_answer_input);
        mOption1Button = findViewById(R.id.button0);
        mOption2Button = findViewById(R.id.button1);
        mOption3Button = findViewById(R.id.button2);
        mOption4Button = findViewById(R.id.button3);
        mSubmitEndButton = findViewById(R.id.btn_submit);
        mSkipButton = findViewById(R.id.btn_skip); 
        mPlayAgainButton = findViewById(R.id.btn_play_again);
        mReportCardView = findViewById(R.id.results);
        mResultTextView = findViewById(R.id.resultText);
        mQuestionTextView = findViewById(R.id.question);
        mTimerTextView = findViewById(R.id.timer);
        mScoreTextView = findViewById(R.id.score);
        mTotalAttemptTextView = findViewById(R.id.tv_total_attempt_result);
        mTotalCorrectTextView = findViewById(R.id.tv_total_correct_result);

        mRecyclerView = findViewById(R.id.rv_results);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuestionReviewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        isVibrationEnable = SpUtil.getInstance().getBoolean(getString(R.string.pf_vibration_switch), true);

        operation = getIntent().getStringExtra(Utils.OPERATIONS);
        if (operation == null) {
            operation = Utils.MIX; 
        }
        
        timer = getIntent().getStringExtra(Utils.TIME);
        isKeyboardMode = getIntent().getBooleanExtra("KEYBOARD_MODE", false); 
        
        mLowerRange1 = getIntent().getIntExtra(Utils.LOWER_1, 11);
        mLowerRange2 = getIntent().getIntExtra(Utils.LOWER_2, 11);
        mUpperRange2 = getIntent().getIntExtra(Utils.UPPER_2, 99);
        mUpperRange1 = getIntent().getIntExtra(Utils.UPPER_1, 99);

        if (isKeyboardMode) {
            mOptionsGridLayout.setVisibility(View.GONE);
            mAnswerInput.setVisibility(View.VISIBLE);
            mAnswerInput.requestFocus();
            showSoftKeyboard(mAnswerInput);
        } else {
            mOptionsGridLayout.setVisibility(View.VISIBLE);
            mAnswerInput.setVisibility(View.GONE);
        }

        mOption1Button.setOnClickListener(this::checkForAnswer);
        mOption2Button.setOnClickListener(this::checkForAnswer);
        mOption3Button.setOnClickListener(this::checkForAnswer);
        mOption4Button.setOnClickListener(this::checkForAnswer);
        
        mSkipButton.setOnClickListener(view -> skipQuestion());

        mAnswerInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || 
               (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                if (isQuizEnded) return true;

                String input = mAnswerInput.getText().toString().trim();
                if (!input.isEmpty() && !input.equals("-")) {
                    try {
                        int parsedAnswer = Integer.parseInt(input);
                        processAnswer(parsedAnswer);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid number!", Toast.LENGTH_SHORT).show();
                    }
                    mAnswerInput.setText(""); 
                }
                return true;
            }
            return false;
        });

        mSubmitEndButton.setOnClickListener(view -> endQuiz());
        mPlayAgainButton.setOnClickListener(this::play);
        mReportCardView.setVisibility(View.INVISIBLE);

        if (operation.equals(Utils.SIMPLIFICATION) || operation.equals(Utils.SIMPLIFICATION_ADVANCE) || operation.equals(Utils.MIX) || operation.equals(Utils.SUM_SERIES)) {
            mQuestionTextView.setTextSize(22);
        }

        play(mPlayAgainButton);
    }

    public void play(View v) {
        mReportCardView.animate().cancel();
        
        mReportCardView.setVisibility(View.INVISIBLE);
        mResultTextView.setText("");
        mScoreTextView.setText("0 / 0");
        mCorrectCount = 0;
        mTotalQuestionCount = 0;
        isQuizEnded = false; 
        questionResultsList.clear();
        mAdapter.submitList(new ArrayList<>()); 
        
        if (isKeyboardMode) {
            mAnswerInput.setText("");
            showSoftKeyboard(mAnswerInput);
        }

        showNewQuestion();
        mPlayAgainButton.setVisibility(View.INVISIBLE);
        startTimer();
    }

    private void showNewQuestion() {
        switch (operation) {
            case Utils.SUM: question = GenerateQuestion.addition(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2); break;
            case Utils.SUBSTRACT: question = GenerateQuestion.subtract(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2); break;
            case Utils.MULTIPLICATION: question = GenerateQuestion.multiplication(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2); break;
            case Utils.DIVISION: question = GenerateQuestion.division(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2); break;
            case Utils.SUM_SERIES: question = GenerateQuestion.sumSeries(mLowerRange1, mUpperRange1); break;
            case Utils.SIMPLIFICATION: question = GenerateQuestion.simplification(mLowerRange1, mUpperRange1); break;
            case Utils.SIMPLIFICATION_ADVANCE: question = GenerateQuestion.simplificationAdvance(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2); break;
            default: question = GenerateQuestion.mix(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
        }
        
        mQuestionTextView.setText(question.getQuestion());
        
        if (!isKeyboardMode) {
            mOption1Button.setText(question.getOption1());
            mOption2Button.setText(question.getOption2());
            mOption3Button.setText(question.getOption3());
            mOption4Button.setText(question.getOption4());
        }
    }

    public void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        int t = 60; 
        try {
            if (timer != null) {
                t = Integer.parseInt(timer);
            }
        } catch (NumberFormatException ignored) {}

        if (t <= 0) {
            mTimerTextView.setText("∞");
        } else {
            int timeInMillis = t * 1000 + 100;
            countDownTimer = new CountDownTimer(timeInMillis, 1000) {
                @Override
                public void onTick(long l) {
                    mTimerTextView.setText((int) l / 1000 + "s");
                }
                @Override
                public void onFinish() {
                    endQuiz();
                }
            }.start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void endQuiz() {
        if (isFinishing() || isDestroyed()) return; 

        if (isQuizEnded) return;
        isQuizEnded = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        hideSoftKeyboard();

        mResultTextView.setText(getString(R.string.done));
        mTotalAttemptTextView.setText(Integer.toString(mTotalQuestionCount));
        mTotalCorrectTextView.setText(Integer.toString(mCorrectCount));
        mPlayAgainButton.setVisibility(View.VISIBLE);
        
        mAdapter.submitList(new ArrayList<>(questionResultsList));

        mReportCardView.setAlpha(0f);
        mReportCardView.setVisibility(View.VISIBLE);
        mReportCardView.animate().alpha(1f).setDuration(500);
    }

    public void checkForAnswer(View view) {
        if (isQuizEnded) return;
        int clickedAnswer = Integer.parseInt(((Button) view).getText().toString());
        processAnswer(clickedAnswer);
    }

    @SuppressLint("SetTextI18n")
    private void processAnswer(int clickedAnswer) {
        if (clickedAnswer == question.getCorrectAnswer()) {
            mResultTextView.setText(R.string.correct);
            mResultTextView.setTextColor(ContextCompat.getColor(this, R.color.materialGreen));
            mCorrectCount++;
        } else {
            mResultTextView.setText(R.string.wrong);
            mResultTextView.setTextColor(ContextCompat.getColor(this, R.color.materialRed));
            
            if (isVibrationEnable && vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
            }
        }

        QuestionResult result = new QuestionResult(mQuestionTextView.getText().toString(), String.valueOf(clickedAnswer), String.valueOf(question.getCorrectAnswer()));
        questionResultsList.add(result);
        
        showNewQuestion();
        mTotalQuestionCount++;
        mScoreTextView.setText(mCorrectCount + "/" + mTotalQuestionCount);
        
        if (isKeyboardMode) {
            mAnswerInput.requestFocus();
        }
    }

    @SuppressLint("SetTextI18n")
    private void skipQuestion() {
        if (isQuizEnded) return;
        
        QuestionResult result = new QuestionResult(mQuestionTextView.getText().toString(), getString(R.string.skip), String.valueOf(question.getCorrectAnswer()));
        questionResultsList.add(result);

        mResultTextView.setText(getString(R.string.skip).toUpperCase());
        mResultTextView.setTextColor(Color.GRAY);

        mTotalQuestionCount++;
        mScoreTextView.setText(mCorrectCount + "/" + mTotalQuestionCount);
        showNewQuestion();

        if (isKeyboardMode) {
            mAnswerInput.setText(""); 
            mAnswerInput.requestFocus();
        }
    }

    private void showSoftKeyboard(View view) {
        view.post(() -> {
            if (view.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = findViewById(android.R.id.content);
        }
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
