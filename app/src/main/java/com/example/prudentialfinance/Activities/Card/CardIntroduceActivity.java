package com.example.prudentialfinance.Activities.Card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.example.prudentialfinance.R;

public class CardIntroduceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_introduce);

        AppCompatButton button = findViewById(R.id.cardIntroduceButtonCreate);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(CardIntroduceActivity.this, CardCreationActivity.class);
            startActivity(intent);
        });
    }
}