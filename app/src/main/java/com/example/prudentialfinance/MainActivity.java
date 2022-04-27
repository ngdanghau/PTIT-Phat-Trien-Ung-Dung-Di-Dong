package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AppCompatButton buttonNext;
    TextView buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setControl()
    {
        buttonNext = findViewById(R.id.mainButtonNext);
        buttonSkip = findViewById(R.id.mainButtonSkip);

    }

    private void setEvent()
    {
        buttonNext.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, IntroduceActivity.class);
            startActivity(intent);

            /*Swith between light and dark mode*/
//            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//                setTheme(R.style.Theme_PrudentialFinance_Dark);
//                Toast.makeText(this, "set theme light", Toast.LENGTH_SHORT).show();
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            } else {
//                setTheme(R.style.Theme_PrudentialFinance);
//                Toast.makeText(this, "set theme dark", Toast.LENGTH_SHORT).show();
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
        });

        buttonSkip.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}