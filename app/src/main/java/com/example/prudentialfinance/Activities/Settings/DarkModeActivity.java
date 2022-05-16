package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.anychart.charts.Resource;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LanguageManager;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.MainActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.AppearanceViewModel;
import com.example.prudentialfinance.ViewModel.Users.AddUserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DarkModeActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    AppCompatButton saveBtn;
    ImageButton backBtn;
    Spinner spnLanguage;

    ArrayAdapter<String> adapter;
    List<String> listLanguage = new ArrayList<>();

    GlobalVariable global;
    Alert alert;
    Alert alertConfirm;
    LanguageManager languageManager;
    LoadingDialog loadingDialog;
    Map<String, String> headers;
    AppearanceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);

        setComponent();

        setControl();

        setEvent();

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        languageManager = new LanguageManager(this, global.getAppName());
        alertConfirm = new Alert(this, 2);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(AppearanceViewModel.class);

    }

    private void setEvent() {
        /**
         * DO NOT REMOVE THIS BLOCK OF CODE BELOW
         * IT REPRESENTS FOR TESTING DARK-MODE FOR EACH SCREEN
         * */
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_PrudentialFinance_Dark);
            switchCompat.setChecked(true);
        } else {
            setTheme(R.style.Theme_PrudentialFinance);
            switchCompat.setChecked(false);
        }

        backBtn.setOnClickListener(view -> finish());

        switchCompat.setOnClickListener(view->{
            if( AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES )
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        saveBtn.setOnClickListener(view -> {

        });

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                if(!languageManager.getCurrent().equals(selectedLang)){
                    languageManager.setLang(selectedLang);
                    languageManager.updateResource();

                    alertConfirm.showAlert(getString(R.string.alertTitle), getString(R.string.warning_change_language), R.drawable.ic_close);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                restart();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        alertConfirm.btnCancel.setOnClickListener(view -> alertConfirm.dismiss());
        alertConfirm.btnOK.setOnClickListener(view -> {
            viewModel.updateLanguage(headers, languageManager.getCurrent());
        });

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        switchCompat = findViewById(R.id.darkModeSwitch);
        saveBtn = findViewById(R.id.saveBtn);


        spnLanguage = findViewById(R.id.spnLanguage);

        listLanguage = languageManager.getList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLanguage);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnLanguage.setAdapter(adapter);

        for (int i = 0; i < listLanguage.size(); i++) {
            String item = listLanguage.get(i);
            if (item.equals(languageManager.getCurrent())) {
                spnLanguage.setSelection(i);
                break;
            }
        }

    }

    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}