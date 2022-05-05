package com.example.prudentialfinance.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.SiteSettingsResponse;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.LoginActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SiteSettingsActivity extends AppCompatActivity {

    ImageButton backBtn;

    AppCompatButton saveBtn;
    EditText siteName, siteSlogan, siteKeyword, siteDescription, logoMark, logoType, currencyField;
    Spinner spnLanguage;
    GlobalVariable global;

    LoadingDialog loadingDialog;
    Alert alert;

    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_settings);

        getApplication();
        global = (GlobalVariable) getApplication();
        loadingDialog = new LoadingDialog(SiteSettingsActivity.this);

        alert = new Alert(SiteSettingsActivity.this);
        alert.normal();

        setControl();

        loadData();

        setEvent();
    }

    private void setDataToControl(SiteSettings data){

        siteName.setText(data.getSite_name());
        siteSlogan.setText(data.getSite_slogan());
        siteKeyword.setText(data.getSite_keywords());
        siteDescription.setText(data.getSite_description());

        logoMark.setText(data.getLogomark());
        logoType.setText(data.getLogotype());

        currencyField.setText(data.getCurrency());

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if(item.equals(data.getLanguage())){
                spnLanguage.setSelection(i);
                break;
            }
        }
    }

    private void loadData(){


        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });

        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        loadingDialog.startLoadingDialog();

        Map<String, String > headers = global.getHeaders();

        Call<SiteSettingsResponse> container = api.getSiteSettings(headers);
        container.enqueue(new Callback<SiteSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SiteSettingsResponse> call, @NonNull Response<SiteSettingsResponse> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful())
                {
                    SiteSettingsResponse resource = response.body();
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
            public void onFailure(Call<SiteSettingsResponse> call, Throwable t) {
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
    }

    private void updateData(){
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        loadingDialog.startLoadingDialog();

        Map<String, String > headers = global.getHeaders();

        String site_name = siteName.getText().toString().trim();
        String site_slogan = siteSlogan.getText().toString().trim();
        String site_description = siteDescription.getText().toString().trim();
        String site_keyword = siteKeyword.getText().toString().trim();

        String logo_mark = logoMark.getText().toString().trim();
        String logo_type = logoType.getText().toString().trim();

        String currency = currencyField.getText().toString().trim();
        String language = spnLanguage.getSelectedItem().toString();
        String action = "save";

        Call<SiteSettingsResponse> container = api.saveSiteSettings(headers,action,
                site_name, site_slogan, site_description, site_keyword,
                logo_type, logo_mark,
                language, currency);

        container.enqueue(new Callback<SiteSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SiteSettingsResponse> call, @NonNull Response<SiteSettingsResponse> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful())
                {
                    SiteSettingsResponse resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();

                    if( result == 1 )
                    {
                        Toast.makeText(SiteSettingsActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        alert.showAlert("Oops!",resource.getMsg(), R.drawable.ic_check);
                    }
                }
            }

            @Override
            public void onFailure(Call<SiteSettingsResponse> call, Throwable t) {
                loadingDialog.dismissDialog();
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);


        siteName = findViewById(R.id.siteName);
        siteSlogan = findViewById(R.id.siteSlogan);
        siteKeyword = findViewById(R.id.siteKeyword);
        siteDescription = findViewById(R.id.siteDescription);

        logoMark = findViewById(R.id.logoMark);
        logoType = findViewById(R.id.logoType);

        currencyField = findViewById(R.id.currency);
        spnLanguage = findViewById(R.id.spnLanguage);

        list.add("en-US");
        list.add("vi-VN");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnLanguage.setAdapter(adapter);



    }
}