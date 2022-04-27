package com.example.prudentialfinance;

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
import com.example.prudentialfinance.Model.GlobalVariable;
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
    private TextView loginTextViewCreateAccount;

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
        token = "JWT " +  accessToken.trim();
        GlobalVariable state = ((GlobalVariable) this.getApplication());


        state.setAccessToken(token);

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
                            /*Print access token*/

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);


                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_LONG).show();
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


        loginTextViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
}