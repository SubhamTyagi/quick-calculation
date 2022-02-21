package io.github.subhamtyagi.quickcalculation.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

import io.github.subhamtyagi.quickcalculation.R;
import io.github.subhamtyagi.quickcalculation.utils.SpUtil;


/**
 * Time setter dialog.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class TimeDialog extends Dialog
        implements View.OnClickListener, TimePicker.OnTimeChangedListener {

    private int mHour;
    private int mMinute;

    public TimeDialog(@NonNull Context context) {
        super(context);
    }


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_dialog);
        initData();
        initWidget();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

    }


    private void initWidget() {
        Button done = findViewById(R.id.dialog_time_done);
        done.setOnClickListener(this);
        Button cancel = findViewById(R.id.dialog_time_cancel);
        cancel.setOnClickListener(this);
        TimePicker timePicker = findViewById(R.id.dialog_time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(SpUtil.getInstance().getInt(getContext().getString(R.string.key_notification_hours), 8));
        timePicker.setMinute(SpUtil.getInstance().getInt(getContext().getString(R.string.key_notification_minutes), 0));
        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        mHour = i;
        mMinute = i1;
    }

    // on click.

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_time_cancel:
                dismiss();
                break;

            case R.id.dialog_time_done:
                //SAVE DATA TO PREFERENCES
                SpUtil.getInstance().putInt(getContext().getString(R.string.key_notification_hours), mHour);
                SpUtil.getInstance().putInt(getContext().getString(R.string.key_notification_minutes), mMinute);

                Log.d("dialog", "onClick: " + mHour + " : " + mMinute);
                dismiss();
                break;
        }
    }
}