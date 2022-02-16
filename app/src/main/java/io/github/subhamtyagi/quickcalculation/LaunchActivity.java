package io.github.subhamtyagi.quickcalculation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.subhamtyagi.quickcalculation.utils.Constants;
import io.github.subhamtyagi.quickcalculation.utils.CrashUtils;


public class LaunchActivity extends AppCompatActivity {

    private Spinner operationsSpinner, timerSpinner;
    private String operationName = "multiply", timer = "30";
    private EditText lower1, lower2, upper1, upper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new CrashUtils(getApplicationContext(), "");

        operationsSpinner = findViewById(R.id.spinner_operator);
        timerSpinner = findViewById(R.id.spinner_timer);
        lower1 = findViewById(R.id.et_range1_lower);
        lower2 = findViewById(R.id.et_range2_lower);
        upper1 = findViewById(R.id.et_range1_upper);
        upper2 = findViewById(R.id.et_range2_upper);

        setAdapters();
        operationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        operationName = "sum";
                        break;
                    case 1:
                        operationName = "subtract";
                        break;
                    case 2:
                        operationName = "multiply";
                        break;
                    default:
                        operationName = "simplification";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        timerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        timer = "30";
                        break;
                    case 1:
                        timer = "45";
                        break;
                    case 2:
                        timer = "60";
                        break;
                    case 3:
                        timer = "90";
                        break;
                    case 4:
                        timer = "120";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.btn_start).setOnClickListener(view -> {
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra(Constants.OPERATIONS, operationName);
            i.putExtra(Constants.TIME, timer);
            String l1 = getValueOf(lower1),
                    l2 = getValueOf(lower2),
                    u2 = getValueOf(upper2),
                    u1 = getValueOf(upper1);

            if (l1 == null) {
                lower1.requestFocus();
            } else if (u1 == null) {
                upper1.requestFocus();
            } else if (l2 == null) {
                lower2.requestFocus();
            } else if (u2 == null) {
                upper2.requestFocus();
            } else {
                int iL1 = Integer.parseInt(l1);
                int iL2 = Integer.parseInt(l2);
                int iU1 = Integer.parseInt(u1);
                int iU2 = Integer.parseInt(u2);
                if (iU2 >= iL2) {
                    if (iU1 >= iL1) {
                        i.putExtra(Constants.LOWER_1, iL1);
                        i.putExtra(Constants.LOWER_2, iL2);
                        i.putExtra(Constants.UPPER_2, iU2);
                        i.putExtra(Constants.UPPER_1, iU1);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        upper1.requestFocus();
                        Toast.makeText(this, getResources().getString(R.string.upper_value_must_be_greater), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    upper2.requestFocus();
                    Toast.makeText(this, getResources().getString(R.string.upper_value_must_be_greater), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void setAdapters() {
        ArrayAdapter<CharSequence> opAdapter;
        opAdapter = ArrayAdapter.createFromResource(this, R.array.Operations, android.R.layout.simple_spinner_item);
        opAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> timerAdapter = ArrayAdapter.createFromResource(this, R.array.Timer, android.R.layout.simple_spinner_item);
        timerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationsSpinner.setAdapter(opAdapter);
        timerSpinner.setAdapter(timerAdapter);
    }

    String getValueOf(EditText editText) {
        String string = editText.getText().toString();
        if (string.isEmpty())
            return null;
        else return string;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}