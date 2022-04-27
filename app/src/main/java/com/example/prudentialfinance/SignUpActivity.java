package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    TextView signUpLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        setControl();
        setEvent();
    }

    private void setControl() {
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn);
    }

    private void setEvent() {
        signUpLoginBtn.setOnClickListener(view -> {
            finish();
        });
    }
}