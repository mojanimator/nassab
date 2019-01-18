package com.mmr.nassab.Fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.mmr.nassab.R;

/**
 * Created by Mojtaba Rajabi on 31/12/2018.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.preferences);
    }

}
