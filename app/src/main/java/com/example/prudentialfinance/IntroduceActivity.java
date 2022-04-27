package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class IntroduceActivity extends AppCompatActivity {

    AppCompatButton buttonNext;
    TextView buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        setControl();
        setEvent();
    }



    private void setControl() {
        buttonNext = findViewById(R.id.introduceButtonNext);
        buttonSkip = findViewById(R.id.introduceButtonSkip);
    }

    private void setEvent() {
        buttonNext.setOnClickListener(view ->{
            Intent intent = new Intent(IntroduceActivity.this, LoginActivity.class);
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
            Intent intent = new Intent(IntroduceActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}