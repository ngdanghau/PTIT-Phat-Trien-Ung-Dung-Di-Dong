package com.example.prudentialfinance.Activities.General;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.prudentialfinance.R;

public class AddCategoryActivity extends AppCompatActivity {

    ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        setControl();
        setEvent();
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
    }
}