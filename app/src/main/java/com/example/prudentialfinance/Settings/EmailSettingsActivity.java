package com.example.prudentialfinance.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.EmailSettingsResponse;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.EmailSettings;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EmailSettingsActivity extends AppCompatActivity {
    ImageButton backBtn;
    AppCompatButton saveBtn;

    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;

    LinearLayout authSMTP;
    Switch swAuth;

    EditText username_email, password_email, txtHost, txtFrom, txtPort;
    Spinner spnEncryption;
    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_settings);

        getApplication();
        global = (GlobalVariable) getApplication();
        loadingDialog = new LoadingDialog(EmailSettingsActivity.this);

        alert = new Alert(EmailSettingsActivity.this);
        alert.normal();

        setControl();

        loadData();

        setEvent();
    }

    private void setDataToControl(EmailSettings data){
        swAuth.setChecked(data.getAuth());

        username_email.setText(data.getUsername());
        password_email.setText(data.getPassword());


        txtHost.setText(data.getHost());
        txtFrom.setText(data.getFrom());
        txtPort.setText(data.getPort());

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if(item.equals(data.getEncryption().toUpperCase())){
                spnEncryption.setSelection(i);
                break;
            }

        }

    }

    private void loadData(){



        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        loadingDialog.startLoadingDialog();

        Map<String, String > headers = global.getHeaders();

        Call<EmailSettingsResponse> container = api.getEmailSettings(headers);
        container.enqueue(new Callback<EmailSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmailSettingsResponse> call, @NonNull Response<EmailSettingsResponse> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful())
                {
                    EmailSettingsResponse resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();

                    if( result == 1 )
                    {
                        setDataToControl(resource.getData());
                    }
                    else
                    {
                        alert.showAlert("Oops!",resource.getMsg(), R.drawable.ic_check);
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailSettingsResponse> call, Throwable t) {
                loadingDialog.dismissDialog();
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
            }
        });
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> {
            finish();
        });

        saveBtn.setOnClickListener(view -> {
            updateData();
        });

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });

        swAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                authSMTP.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void updateData(){
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        loadingDialog.startLoadingDialog();

        Map<String, String > headers = global.getHeaders();

        String host = txtHost.getText().toString().trim();
        String port = txtPort.getText().toString().trim();
        String encryption = spnEncryption.getSelectedItem().toString().toLowerCase();
        Boolean auth = swAuth.isChecked();
        String username = username_email.getText().toString().trim();
        String password = password_email.getText().toString().trim();
        String from = txtFrom.getText().toString().trim();
        String action = "save";

        Call<EmailSettingsResponse> container = api.saveEmailSettings(headers, action,
                host, port, encryption, auth, username, password, from);

        container.enqueue(new Callback<EmailSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmailSettingsResponse> call, @NonNull Response<EmailSettingsResponse> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful())
                {
                    EmailSettingsResponse resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();

                    if( result == 1 )
                    {
                        Toast.makeText(EmailSettingsActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                    }else{
                        alert.showAlert("Oops!", resource.getMsg(), R.drawable.ic_close);
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailSettingsResponse> call, Throwable t) {
                loadingDialog.dismissDialog();
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);

        swAuth = findViewById(R.id.swAuth);
        authSMTP = findViewById(R.id.authSMTP);

        username_email = findViewById(R.id.username_email);
        password_email = findViewById(R.id.password);

        txtHost = findViewById(R.id.txtHost);
        txtPort = findViewById(R.id.txtPort);
        txtFrom = findViewById(R.id.txtFrom);

        spnEncryption = findViewById(R.id.spnEncryption);

        list.add("None");
        list.add("TLS");
        list.add("SSL");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnEncryption.setAdapter(adapter);

    }
}