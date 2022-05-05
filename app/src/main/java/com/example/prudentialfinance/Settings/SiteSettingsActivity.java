package com.example.prudentialfinance.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Settings.SiteSettingsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SiteSettingsActivity extends AppCompatActivity {

    ImageButton backBtn;

    AppCompatButton saveBtn;
    EditText siteName, siteSlogan, siteKeyword, siteDescription, logoMark, logoType, currencyField;
    Spinner spnLanguage;
    GlobalVariable global;

    SiteSettingsViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;

    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();
    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_settings);

        getApplication();

        setComponent();

        setControl();

        setEvent();

        loadData();
    }

    private void loadData() {
        viewModel.getData(headers);
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(SiteSettingsActivity.this);
        alert = new Alert(SiteSettingsActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(SiteSettingsViewModel.class);
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

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        saveBtn.setOnClickListener(view -> {
            String site_name = siteName.getText().toString().trim();
            String site_slogan = siteSlogan.getText().toString().trim();
            String site_description = siteDescription.getText().toString().trim();
            String site_keyword = siteKeyword.getText().toString().trim();

            String logo_mark = logoMark.getText().toString().trim();
            String logo_type = logoType.getText().toString().trim();

            String currency = currencyField.getText().toString().trim();
            String language = spnLanguage.getSelectedItem().toString();
            String action = "save";

            viewModel.updateData(headers, action, site_name, site_slogan, site_description, site_keyword, logo_type, logo_mark, language, currency);
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                setDataToControl(object.getData());
                if(object.getMethod().equals("POST")){
                    Toast.makeText(SiteSettingsActivity.this, object.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                alert.showAlert("Oops!", object.getMsg(), R.drawable.ic_close);
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