package io.github.subhamtyagi.quickcalculation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.DynamicColors;
import java.util.Calendar;

import io.github.subhamtyagi.quickcalculation.utils.CrashUtils;
import io.github.subhamtyagi.quickcalculation.utils.SpUtil;
import io.github.subhamtyagi.quickcalculation.utils.Utils;

public class LaunchActivity extends AppCompatActivity {

    AlarmManager manager;
    PendingIntent pendingIntent;
    private Spinner operationsSpinner, timerSpinner;
    private String operationName = "mix", timer = "30";
    private EditText lower1, lower2, upper1, upper2;
    private Switch keyboardModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CrashUtils(getApplicationContext(), "");
        SpUtil.getInstance().init(this);
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.activity_launch);

        operationsSpinner = findViewById(R.id.spinner_operator);
        timerSpinner = findViewById(R.id.spinner_timer);
        lower1 = findViewById(R.id.et_range1_lower);
        lower2 = findViewById(R.id.et_range2_lower);
        upper1 = findViewById(R.id.et_range1_upper);
        upper2 = findViewById(R.id.et_range2_upper);
        keyboardModeSwitch = findViewById(R.id.switch_keyboard_mode);

        lower1.setText(SpUtil.getInstance().getString(Utils.LOWER_1, "11"));
        lower2.setText(SpUtil.getInstance().getString(Utils.LOWER_2, "11"));
        upper1.setText(SpUtil.getInstance().getString(Utils.UPPER_1, "99"));
        upper2.setText(SpUtil.getInstance().getString(Utils.UPPER_2, "99"));
        
        boolean savedKeyboardPref = SpUtil.getInstance().getBoolean("PREF_KEYBOARD_MODE", false);
        keyboardModeSwitch.setChecked(savedKeyboardPref);

        setAdapters();

        operationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: operationName = Utils.SUM; break;
                    case 1: operationName = Utils.SUBSTRACT; break;
                    case 2: operationName = Utils.MULTIPLICATION; break;
                    case 3: operationName = Utils.DIVISION; break;
                    case 4: operationName = Utils.SUM_SERIES; break;
                    case 5: operationName = Utils.SIMPLIFICATION; break;
                    case 6: operationName = Utils.SIMPLIFICATION_ADVANCE; break;
                    default: operationName = Utils.MIX;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        timerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: timer = "30"; break;
                    case 1: timer = "45"; break;
                    case 2: timer = "60"; break;
                    case 3: timer = "90"; break;
                    case 4: timer = "120"; break;
                    case 5: timer = "300"; break;
                    case 6: timer = "600"; break;
                    case 7: timer = "-1"; break; 
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        findViewById(R.id.btn_start).setOnClickListener(view -> {
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra(Utils.OPERATIONS, operationName);
            i.putExtra(Utils.TIME, timer);
            
            i.putExtra("KEYBOARD_MODE", keyboardModeSwitch.isChecked());
            SpUtil.getInstance().putBoolean("PREF_KEYBOARD_MODE", keyboardModeSwitch.isChecked());

            String l1 = getValueOf(lower1), l2 = getValueOf(lower2), u2 = getValueOf(upper2), u1 = getValueOf(upper1);

            if (l1 == null) lower1.requestFocus();
            else if (u1 == null) upper1.requestFocus();
            else if (l2 == null) lower2.requestFocus();
            else if (u2 == null) upper2.requestFocus();
            else {
                try {
                    int iL1 = Integer.parseInt(l1), iL2 = Integer.parseInt(l2), iU1 = Integer.parseInt(u1), iU2 = Integer.parseInt(u2);
                    if (iU2 >= iL2 && iU1 >= iL1) {
                        i.putExtra(Utils.LOWER_1, iL1); i.putExtra(Utils.LOWER_2, iL2);
                        i.putExtra(Utils.UPPER_2, iU2); i.putExtra(Utils.UPPER_1, iU1);
                        SpUtil.getInstance().putString(Utils.LOWER_1, String.valueOf(iL1));
                        SpUtil.getInstance().putString(Utils.LOWER_2, String.valueOf(iL2));
                        SpUtil.getInstance().putString(Utils.UPPER_1, String.valueOf(iU1));
                        SpUtil.getInstance().putString(Utils.UPPER_2, String.valueOf(iU2));
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        if (iU2 < iL2) upper2.requestFocus(); else upper1.requestFocus();
                        Toast.makeText(this, getResources().getString(R.string.upper_value_must_be_greater), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Number is too large!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setAdapters() {
        ArrayAdapter<CharSequence> opAdapter = ArrayAdapter.createFromResource(this, R.array.operations, android.R.layout.simple_spinner_item);
        opAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> timerAdapter = ArrayAdapter.createFromResource(this, R.array.timer, android.R.layout.simple_spinner_item);
        timerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationsSpinner.setAdapter(opAdapter);
        timerSpinner.setAdapter(timerAdapter);
    }

    String getValueOf(EditText editText) {
        String string = editText.getText().toString();
        return string.isEmpty() ? null : string;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            SpUtil.getInstance().putBoolean(getString(R.string.is_theme_changed), false);
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {}
    }
}
