package com.example.prudentialfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpFirstname, signUpLastname, signUpEmail, signUpPass, signUpPassConfirm;
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
        signUpPassConfirm = findViewById(R.id.signUpPassConfirm);

        signUpBtn = findViewById(R.id.signUpBtn);
    }

    private void setAuthorizedToken( String accessToken) {
        String token = "JWT " +  accessToken.trim();
        GlobalVariable state = ((GlobalVariable) this.getApplication());

        state.setAccessToken(token);

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        signUpLoginBtn.setOnClickListener(view -> {
            finish();
        });

        final LoadingDialog loadingDialog = new LoadingDialog(SignUpActivity.this);
        Alert alert = new Alert(SignUpActivity.this);
        alert.normal();

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });

        signUpBtn.setOnClickListener(view -> {
            String firstName = signUpFirstname.getText().toString();
            String lastName = signUpLastname.getText().toString();
            String email = signUpEmail.getText().toString();
            String pass = signUpPass.getText().toString();
            String passConfirm = signUpPassConfirm.getText().toString();

            /*Step 1*/
            Retrofit service = HTTPService.getInstance();
            HTTPRequest api = service.create(HTTPRequest.class);

            loadingDialog.startLoadingDialog();

            /*Step 2*/
            Call<Login> container = api.signup(firstName, lastName, email, pass, passConfirm);
            container.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    loadingDialog.dismissDialog();
                    if(response.isSuccessful())
                    {
                        Login resource = response.body();
                        assert resource != null;
                        int result = resource.getResult();

                        if( result == 1 )
                        {
                            setAuthorizedToken( resource.getAccessToken() );
                            /*Print access token*/

                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);


                            Toast.makeText(SignUpActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            setAuthorizedToken( "" );
                            alert.showAlert(resource.getMsg(), R.drawable.info_icon);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    loadingDialog.dismissDialog();
                }
            });
        });


    }
}