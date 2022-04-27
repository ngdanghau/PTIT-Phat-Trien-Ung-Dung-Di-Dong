package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prudentialfinance.Lib.LoadingDialog;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpFirstname, signUpLastname, signUpEmail, signUpPass;
    TextView signUpLoginBtn;
    AppCompatButton signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        setControl();
        setEvent();
    }

    private void setControl() {
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn);
        signUpFirstname = findViewById(R.id.signUpFirstname);
        signUpLastname = findViewById(R.id.signUpLastname);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPass);

        signUpBtn = findViewById(R.id.signUpBtn);
    }

    private void setEvent() {
        signUpLoginBtn.setOnClickListener(view -> {
            finish();
        });

        final LoadingDialog loadingDialog = new LoadingDialog(SignUpActivity.this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 5000);
            }
        });


    }
}