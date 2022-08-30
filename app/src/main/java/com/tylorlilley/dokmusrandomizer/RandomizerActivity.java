package com.tylorlilley.dokmusrandomizer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.tylorlilley.dokmusrandomizer.databinding.ActivityRandomizerBinding;

public class RandomizerActivity extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final Random rand = new Random();
    private final int mapTilesInUse = 8;
    private int totalMapTiles = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize app toolbar and main activity content
        com.tylorlilley.dokmusrandomizer.databinding.ActivityRandomizerBinding binding = ActivityRandomizerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initial Randomization or Load From Last Randomized State
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        if (pref.getBoolean("initialized", false)) {
            updateTextFields();
        }
        else {
            initializeSettings();
            randomize();
        }

        binding.randomize.setOnClickListener(view -> randomize());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTextFields();
        updateExpansions();
    }

    private void initializeSettings() {
        editor.putBoolean("mapTileRotation", true);
        editor.putBoolean("returnOfErefel", true);
        editor.putBoolean("initialized", true);
        editor.apply();
    }

    private void randomize() {
        // Initialize SharedPreferences
        updateExpansions();

        // Randomize Map Tiles
        ArrayList<String> mapTiles = new ArrayList<>();
        for (int i = 0; i < totalMapTiles; i++) {
            String tileNumber = Integer.toString(i+1);
            String tileSide = getTileSide();
            String tileDirection = getTileDirection();
            mapTiles.add(tileNumber + tileSide + tileDirection);
        }
        Collections.shuffle(mapTiles);

        // Generate mapTiles Text
        for (int i = 0; i < mapTilesInUse; i++) {
            String mapName = getMapName(i);
            editor.putString(mapName, mapTiles.get(i));
        }

        // Generate Scenario Text
        String scenarioText = (pref.getBoolean("returnOfErefel", false)) ? getScenarioText() : "";
        editor.putString("mapCenter", scenarioText);

        // Update Text Views
        editor.apply();
        updateTextFields();
    }

    private void updateTextFields() {
        // Set Map Tile Text
        for (int i = 0; i < mapTilesInUse; i++) {
            String mapName = getMapName(i);
            String mapText = pref.getString(mapName, "XXX");
            int mapValue = Integer.parseInt(mapText.replaceAll("\\D+", ""));
            int mapColor = getMapColor(mapValue);
            float mapRotation = getMapRotation(mapText);

            TextView mapTile = (TextView) findViewById(getResources().getIdentifier(mapName, "id", getPackageName()));

            mapTile.setBackgroundColor(mapColor);
            mapTile.setRotation(mapRotation);
            if (pref.getBoolean("mapTileRotation", false)) {
                mapText = mapText.substring(0, mapText.length() - 1);
            }
            mapTile.setText(mapText);
        }

        // Set Center Scenario Text
        String scenarioText = pref.getString("mapCenter", "");
        TextView centerMapTile = (TextView) findViewById(R.id.mapCenter);
        centerMapTile.setText(scenarioText);
    }

    private void updateExpansions() {
        totalMapTiles = (pref.getBoolean("returnOfErefel", false) ? 12 : 8);
    }

    private String getScenarioText() {
        switch(rand.nextInt(4)) {
            case 1:
                return getString(R.string.scenario_text_ice);
            case 2:
                return getString(R.string.scenario_text_sun);
            case 3:
                return getString(R.string.scenario_text_winds);
            default:
                return getString(R.string.scenario_text_water);
        }
    }

    private int getMapColor(int mapValue) {
        int baseColor = getResources().getColor(R.color.base_map_tile);
        int expansionColor = getResources().getColor(R.color.expansion_map_tile);
        return ( mapValue > 8 ? expansionColor : baseColor);
    }

    private String getMapName(int i) {
        return ("map"+(i+1));
    }

    private boolean coinFlip() {
        return (rand.nextInt(2) == 0);
    }

    private String getTileSide() {
        return (coinFlip() ? "A" : "B");
    }

    private String getTileDirection() {
        switch(rand.nextInt(4)) {
            case 1:
                return "←";
            case 2:
                return "→";
            case 3:
                return "↑";
            default:
                return "↓";
        }
    }

    private float getMapRotation(String mapText) {
        if (pref.getBoolean("mapTileRotation", false)) {
            switch (mapText.charAt(mapText.length() - 1)) {
                case '←':
                    return 270.0F;
                case '→':
                    return 90.0F;
                case '↑':
                    return 180.0F;
                default:
                    return 0.0F;
            }
        }
        else {
            return 0.0F;
        }
    }
}