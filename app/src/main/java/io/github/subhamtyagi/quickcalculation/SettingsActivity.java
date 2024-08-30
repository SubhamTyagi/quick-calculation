package io.github.subhamtyagi.quickcalculation;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.material.color.DynamicColors;

import io.github.subhamtyagi.quickcalculation.dialog.TimeDialog;
import io.github.subhamtyagi.quickcalculation.utils.SpUtil;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpUtil.getInstance().init(this);
        /*  setTheme(Utils.getTheme(this));*/
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
            /*ListPreference theme = findPreference(getString(R.string.pf_theme));*/
            SwitchPreferenceCompat notification = findPreference(getString(R.string.pf_notification_switch));
            Preference time = findPreference(getString(R.string.pf_notification_time));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                assert notification != null;
                notification.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean b = (boolean) newValue;
                    assert time != null;
                    time.setVisible(b);
                    if (b) {
                        String msg = SpUtil.getInstance().getInt(getContext().getString(R.string.key_notification_hours)) + ":"
                                + SpUtil.getInstance().getInt(getContext().getString(R.string.key_notification_minutes));
                        time.setSummary(msg);
                    }
                    return true;
                });

                assert time != null;
                time.setOnPreferenceClickListener(preference -> {
                    new TimeDialog(getContext()).show();
                    return true;
                });
            } else {
                notification.setVisible(false);
                time.setVisible(false);
            }

           /* assert theme != null;
            theme.setOnPreferenceChangeListener((preference, newValue) -> {
                theme.setSummary((String) newValue);
                SpUtil.getInstance().putBoolean(getContext().getString(R.string.is_theme_changed),true);
                return true;
            });*/
        }
    }

}