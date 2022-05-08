package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;

import com.example.prudentialfinance.R;

public class DarkModeActivity extends AppCompatActivity {

    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);
        switchCompat = findViewById(R.id.darkModeSwitch);


        /**
         * DO NOT REMOVE THIS BLOCK OF CODE BELOW
         * IT REPRESENTS FOR TESTING DARK-MODE FOR EACH SCREEN
         * */
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_PrudentialFinance_Dark);
            switchCompat.setChecked(true);
        } else {
            setTheme(R.style.Theme_PrudentialFinance);
            switchCompat.setChecked(false);
        }

        switchCompat.setOnClickListener(view->{
            if( AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES )
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

    }
}