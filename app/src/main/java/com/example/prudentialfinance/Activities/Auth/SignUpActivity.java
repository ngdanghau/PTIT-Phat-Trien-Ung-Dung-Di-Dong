package com.example.prudentialfinance.Activities.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.SignUpViewModel;


public class SignUpActivity extends AppCompatActivity {

    EditText signUpFirstname, signUpLastname, signUpEmail, signUpPass, signUpPassConfirm;
    TextView signUpLoginBtn;
    AppCompatButton signUpBtn;
    SignUpViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setComponent();
        setControl();
        setEvent();
    }

    private void setComponent() {
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        loadingDialog = new LoadingDialog(SignUpActivity.this);
        alert = new Alert(SignUpActivity.this, 1);
    }

    private void setControl() {
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn);
        signUpFirstname = findViewById(R.id.signUpFirstname);
        signUpLastname = findViewById(R.id.signUpLastname);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPass);
        signUpPassConfirm = findViewById(R.id.signUpPassConfirm);
        signUpBtn = findViewById(R.id.signUpBtn);
    }

    private void setAuthorizedToken( String accessToken) {
        String token = "JWT " +  accessToken.trim();
        GlobalVariable state = ((GlobalVariable) this.getApplication());

        state.setAccessToken(token);

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        signUpLoginBtn.setOnClickListener(view -> finish());
        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        signUpBtn.setOnClickListener(view -> {
            String firstName = signUpFirstname.getText().toString();
            String lastName = signUpLastname.getText().toString();
            String email = signUpEmail.getText().toString();
            String pass = signUpPass.getText().toString();
            String passConfirm = signUpPassConfirm.getText().toString();

            viewModel.signUp(firstName, lastName, email, pass, passConfirm);
        });

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                setAuthorizedToken("");
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                setAuthorizedToken(object.getAccessToken());
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);

                Toast.makeText(SignUpActivity.this, object.getMsg(), Toast.LENGTH_LONG).show();
                finish();
            } else {
                setAuthorizedToken("");
                alert.showAlert("Oops!", object.getMsg(), R.drawable.ic_close);
            }
        });
    }
}