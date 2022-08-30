package com.tylorlilley.dokmusrandomizer;

import android.os.Bundle;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.appcompat.widget.Toolbar;


public class SettingsActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        initializeSettingsSwitches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeSettingsSwitches();
    }

    private void initializeSettingsSwitches() {
        ViewGroup settings = (ViewGroup)findViewById(R.id.settings);
        for (int i = 0; i < settings.getChildCount(); i++) {
            CheckBox box = (CheckBox)settings.getChildAt(i);
            setSetting(box);
            box.setOnClickListener(b -> updateSetting((CheckBox) b));
        }
    }

    private void setSetting(CheckBox box) {
        String settingName = getResources().getResourceEntryName(box.getId());
        box.setChecked(pref.getBoolean(settingName, false));
    }

    private void updateSetting(CheckBox box) {
        String settingName = getResources().getResourceEntryName(box.getId());
        editor.putBoolean(settingName, box.isChecked());
        editor.apply();
    }
}