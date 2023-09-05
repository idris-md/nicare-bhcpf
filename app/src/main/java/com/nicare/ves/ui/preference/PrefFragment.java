package com.nicare.ves.ui.preference;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.fragment.app.Fragment;

import com.nicare.ves.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragment {
    public static final String PREF_BIOMETRIC = "pref_biometric";
    public static final String PREF_SYNC_TYPE = "pref_sync_connection_type";
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;


    public PrefFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals(PREF_BIOMETRIC)) {
                    Preference preference = findPreference(s);
                    preference.setSummary(sharedPreferences.getString(s, " Default"));
                }


                if (s.equals(PREF_SYNC_TYPE)) {
                    Preference preference = findPreference(s);
                    preference.setSummary(sharedPreferences.getString(s, " Sync"));
                }

            }
        };


    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        Preference preferenceSync = findPreference(PREF_SYNC_TYPE);
        preferenceSync.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_SYNC_TYPE, null));

        Preference preferenceBiometric = findPreference(PREF_BIOMETRIC);
        preferenceBiometric.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_BIOMETRIC, null));

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
