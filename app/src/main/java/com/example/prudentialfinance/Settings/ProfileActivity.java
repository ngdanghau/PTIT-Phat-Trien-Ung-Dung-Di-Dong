package com.example.prudentialfinance.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Application;
import android.content.Intent;
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

public class ProfileActivity extends AppCompatActivity {

    User AuthUser;
    ImageButton backBtn;
    TextView tvEmail;
    EditText firstname, lastname;
    AppCompatButton saveBtn;

    GlobalVariable global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getApplication();
        global = (GlobalVariable) getApplication();

        AuthUser = global.getAuthUser();
        setControl();
        setEvent();

    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> {
            finish();
        });

        final LoadingDialog loadingDialog = new LoadingDialog(ProfileActivity.this);
        Alert alert = new Alert(ProfileActivity.this);
        alert.normal();

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });


        saveBtn.setOnClickListener(view -> {
            String firstName = firstname.getText().toString().trim();
            String lastName = lastname.getText().toString().trim();


            Retrofit service = HTTPService.getInstance();
            HTTPRequest api = service.create(HTTPRequest.class);

            loadingDialog.startLoadingDialog();

            Map<String, String > headers = global.getHeaders();

            Call<Login> container = api.updateProfile(headers, firstName, lastName);
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
                            global.setAuthUser(resource.getData());
                            Toast.makeText(ProfileActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
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
                    alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_check);
                }
            });
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        tvEmail = findViewById(R.id.signUpEmail);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        saveBtn = findViewById(R.id.saveBtn);

        firstname.setText(AuthUser.getFirstname());
        lastname.setText(AuthUser.getLastname());
        tvEmail.setText(AuthUser.getEmail());


    }
}