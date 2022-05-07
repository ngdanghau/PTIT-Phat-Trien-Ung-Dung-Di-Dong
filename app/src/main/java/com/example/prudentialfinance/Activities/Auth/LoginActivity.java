package com.example.prudentialfinance.Activities.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.LoginViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private  AppCompatButton buttonSignIn;
    private EditText username, password;
    private LoginViewModel viewModel;
    private String token;
    private TextView loginTextViewCreateAccount, txt_forgotpassword;
    GlobalVariable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setControl() {
        buttonSignIn = findViewById(R.id.loginButtonSignIn);
        loginTextViewCreateAccount = findViewById(R.id.loginTextViewCreateAccount);
        username = findViewById(R.id.loginTextViewUsername);
        password = findViewById(R.id.loginTextViewPassword);
        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }


    /***
     * @author Phong-Kaster
     *
     * this function set Access token after loginning successfully. Then, this token can be utilized in
     * header. To get Header, we call (Model/Global Variable) getHeader
     *
     */
    private void setAuthorizedToken( String accessToken) {
        state = ((GlobalVariable) this.getApplication());
        state.setAccessToken(accessToken.trim());

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        Alert alert = new Alert(LoginActivity.this);
        alert.normal();

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });

        txt_forgotpassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RecoveryActivity.class);
            startActivity(intent);
        });

        buttonSignIn.setOnClickListener(view->{

            String user = username.getText().toString();
            String pass = password.getText().toString();

            /*Step 1*/
            Retrofit service = HTTPService.getInstance();
            HTTPRequest api = service.create(HTTPRequest.class);

            loadingDialog.startLoadingDialog();

            /*Step 2*/
            Call<Login> container = api.login(user, pass);
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
                            state.setAuthUser(resource.getData());
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            setAuthorizedToken( "" );
                            alert.showAlert(getResources().getString(R.string.alertTitle), resource.getMsg(), R.drawable.ic_close);;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);;
                }
            });
        });


        loginTextViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
}