package com.mmr.nassab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Mojtaba Rajabi on 30/12/2018.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new PrefsFragment(), "SettingsFragment").commit();
        }
    }


    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}

