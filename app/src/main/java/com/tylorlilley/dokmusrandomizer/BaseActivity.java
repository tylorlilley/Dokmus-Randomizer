package com.tylorlilley.dokmusrandomizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String itemTitle = item.getTitle().toString();
        String settingsTitle = getString(R.string.action_settings);
        String aboutTitle = getString(R.string.action_about);

        if (itemTitle.equals(settingsTitle)) {
            selectedActivity(SettingsActivity.class);
        }
        else if (itemTitle.equals(aboutTitle)) {
            selectedActivity(AboutActivity.class);
        }
        else {
            selectedActivity(RandomizerActivity.class);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    protected void selectedActivity(Class<?> selectedClass) {
        if (this.getClass() == selectedClass) {
            closeOptionsMenu();
        }
        else if (selectedClass == RandomizerActivity.class) {
            finish();
        }
        else {
            if (this.getClass() != RandomizerActivity.class) { finish(); }
            startActivity(new Intent(this, selectedClass));
        }
    }
}