package com.example.prudentialfinance.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordActivity extends AppCompatActivity {

    User AuthUser;
    ImageButton backBtn;
    TextView tvEmail;
    EditText password, oldPassword, confirmPassword;
    AppCompatButton saveBtn;

    GlobalVariable global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getApplication();
        global = (GlobalVariable) getApplication();

        AuthUser = global.getAuthUser();
        setControl();
        setEvent();

    }

    private void setAuthorizedToken( String accessToken) {
        String token = "JWT " +  accessToken.trim();
        GlobalVariable state = ((GlobalVariable) this.getApplication());


        state.setAccessToken(token);

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> {
            finish();
        });

        final LoadingDialog loadingDialog = new LoadingDialog(PasswordActivity.this);
        Alert alert = new Alert(PasswordActivity.this);
        alert.normal();

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });


        saveBtn.setOnClickListener(view -> {
            String newPass = password.getText().toString().trim();
            String oldPass = oldPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();


            Retrofit service = HTTPService.getInstance();
            HTTPRequest api = service.create(HTTPRequest.class);

            loadingDialog.startLoadingDialog();

            Map<String, String > headers = global.getHeaders();

            Call<Login> container = api.changePassword(headers, newPass, confirmPass, oldPass);
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
                            global.setAuthUser(resource.getData());
                            Toast.makeText(PasswordActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            alert.showAlert("Oops!",resource.getMsg(), R.drawable.ic_check);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
                }
            });
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        password = findViewById(R.id.password);
        oldPassword = findViewById(R.id.oldPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        saveBtn = findViewById(R.id.saveBtn);

    }
}