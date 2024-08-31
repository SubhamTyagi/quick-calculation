package io.github.subhamtyagi.quickcalculation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.color.DynamicColors;
import io.github.subhamtyagi.quickcalculation.factory.GenerateQuestion;
import io.github.subhamtyagi.quickcalculation.factory.Question;
import io.github.subhamtyagi.quickcalculation.utils.Utils;
import io.github.subhamtyagi.quickcalculation.utils.SpUtil;

public class QuizActivity extends AppCompatActivity {

    private ConstraintLayout mReportCardView;
    private Button mOption1Button;
    private Button mOption2Button;
    private Button mOption3Button;
    private Button mOption4Button;
    private Button mPlayAgainButton;
    private TextView mScoreTextView;
    private TextView mTimerTextView;
    private TextView mQuestionTextView;
    private TextView mResultTextView;
    private TextView mTotalAttemptTextView;
    private TextView mTotalCorrectTextView;

    private Vibrator vibrator;
    private CountDownTimer countDownTimer;

    private int mCorrectCount = 0;
    private int mTotalQuestionCount = 0;
    private int mLowerRange1;
    private int mLowerRange2;
    private int mUpperRange1;
    private int mUpperRange2;

    private String operation = null;
    private String timer = null;

    private Question question;
    boolean isVibrationEnable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.activity_quiz);
        SpUtil.getInstance().init(this);

        mOption1Button = findViewById(R.id.button0);
        mOption2Button = findViewById(R.id.button1);
        mOption3Button = findViewById(R.id.button2);
        mOption4Button = findViewById(R.id.button3);

        mPlayAgainButton = findViewById(R.id.btn_play_again);
        mReportCardView = findViewById(R.id.results);
        mResultTextView = findViewById(R.id.resultText);
        mQuestionTextView = findViewById(R.id.question);
        mTimerTextView = findViewById(R.id.timer);
        mScoreTextView = findViewById(R.id.score);
        mTotalAttemptTextView = findViewById(R.id.tv_total_attempt_result);
        mTotalCorrectTextView = findViewById(R.id.tv_total_correct_result);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        isVibrationEnable = SpUtil.getInstance().getBoolean(getString(R.string.pf_vibration_switch), true);
      
        mOption1Button.setOnClickListener(this::checkForAnswer);
        mOption2Button.setOnClickListener(this::checkForAnswer);
        mOption3Button.setOnClickListener(this::checkForAnswer);
        mOption4Button.setOnClickListener(this::checkForAnswer);
        findViewById(R.id.btn_submit).setOnClickListener(view -> {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        });

        mPlayAgainButton.setOnClickListener(this::play);

        mReportCardView.setVisibility(View.INVISIBLE);

        Intent get = getIntent();
        operation = get.getStringExtra(Utils.OPERATIONS);

        if (operation.equals(Utils.SIMPLIFICATION) || operation.equals(Utils.SIMPLIFICATION_ADVANCE) || operation.equals(Utils.MIX) || operation.equals(Utils.SUM_SERIES)) {
            mQuestionTextView.setTextSize(22);
        }
        timer = get.getStringExtra(Utils.TIME);
        mLowerRange1 = get.getIntExtra(Utils.LOWER_1, 11);
        mLowerRange2 = get.getIntExtra(Utils.LOWER_2, 11);
        mUpperRange2 = get.getIntExtra(Utils.UPPER_2, 99);
        mUpperRange1 = get.getIntExtra(Utils.UPPER_1, 99);

        play(mPlayAgainButton);
    }

    public void play(View v) {
        mReportCardView.setVisibility(View.INVISIBLE);
        // mReportCardView.animate().translationY(-5000);
        mResultTextView.setText("");
        mScoreTextView.setText("0 / 0");
        mCorrectCount = 0;
        mTotalQuestionCount = 0;
        showNewQuestion();

        mPlayAgainButton.setVisibility(View.INVISIBLE);
        startTimer();
    }

    private void showNewQuestion() {
        switch (operation) {
            case Utils.SUM:
                question = GenerateQuestion.addition(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            case Utils.SUBSTRACT:
                question = GenerateQuestion.subtract(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            case Utils.MULTIPLICATION:
                question = GenerateQuestion.multiplication(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            case Utils.DIVISION:
                question = GenerateQuestion.division(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            case Utils.MODULO:
                question = GenerateQuestion.modulo(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            case Utils.SUM_SERIES:
                question = GenerateQuestion.sumSeries(mLowerRange1, mUpperRange1);
                break;
            case Utils.SIMPLIFICATION:
                question = GenerateQuestion.simplification(mLowerRange1, mUpperRange1);
                break;
            case Utils.SIMPLIFICATION_ADVANCE:
                question = GenerateQuestion.simplificationAdvance(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);
                break;
            default:
                question = GenerateQuestion.mix(mLowerRange1, mUpperRange1, mLowerRange2, mUpperRange2);


        }
        //set options
        mQuestionTextView.setText(question.getQuestion());
        mOption1Button.setText(question.getOption1());
        mOption2Button.setText(question.getOption2());
        mOption3Button.setText(question.getOption3());
        mOption4Button.setText(question.getOption4());
    }

    public void startTimer() {
        int t = Integer.parseInt(timer) * 1000 + 100;
        countDownTimer = new CountDownTimer(t, 1000) {
            @Override
            public void onTick(long l) {
                String time = (int) l / 1000 + "s";
                mTimerTextView.setText(time);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                mResultTextView.setText(getString(R.string.done));
                mReportCardView.setVisibility(View.VISIBLE);
                mReportCardView.setY(2000);
                mReportCardView.animate().translationYBy(-1990).setDuration(1350);
                mTotalAttemptTextView.setText(Integer.toString(mTotalQuestionCount));
                mTotalCorrectTextView.setText(Integer.toString(mCorrectCount));
                mPlayAgainButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    public void checkForAnswer(View view) {
        int clickedAnswer = Integer.parseInt(((Button) view).getText().toString());
        if (clickedAnswer == question.getCorrectAnswer()) {
            mResultTextView.setText(R.string.correct);
            mResultTextView.setTextColor(getResources().getColor(R.color.materialGreen));
            mCorrectCount++;
        } else {
            mResultTextView.setText(R.string.wrong);
            mResultTextView.setTextColor(getResources().getColor(R.color.materialRed));
            //vibrate phone
            if(isVibrationEnable){
            	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                	vibrator.vibrate(100);
                	}
                	}
        }
        showNewQuestion();
        mTotalQuestionCount++;
        mScoreTextView.setText(mCorrectCount + "/" + mTotalQuestionCount);
    }

    /*
     *
     * BottomSheetResultsFragment bottomSheetResultsFragment = BottomSheetResultsFragment.newInstance(text);
     *  bottomSheetResultsFragment.show(getSupportFragmentManager(), "bottomSheetResultsFragment");
     *
     */
}
