package com.example.prudentialfinance.Activities.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Helpers.NumberTextWatcher;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Goal.DepositViewModel;
import com.example.prudentialfinance.ViewModel.Goal.GoalAddViewModel;

import java.util.Map;

public class DepositActivity extends AppCompatActivity {

    private EditText goal_deposit;
    private AppCompatButton btn_deposit;
    private int id,deposit;

    private DepositViewModel viewModel;
    private GlobalVariable global;
    private Map<String,String> headers;
    private LoadingDialog loadingDialog;
    private Alert alert;
    private User authUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        Intent intent = getIntent();
        id = (int)intent.getSerializableExtra("id");
        setControl();
        setComponent();
        setEvent();
    }

    private void setControl()
    {
        goal_deposit = findViewById(R.id.et_deposit);
        btn_deposit = findViewById(R.id.btn_deposit);
    }
    private void setComponent()
    {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        alert = new Alert(this,1);
        loadingDialog = new LoadingDialog(this);
        viewModel = new ViewModelProvider(this).get(DepositViewModel.class);
        authUser = global.getAuthUser();

    }

    private void setEvent()
    {
        goal_deposit.addTextChangedListener(new NumberTextWatcher(goal_deposit));

        btn_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    deposit = Integer.parseInt(goal_deposit.getText().toString().replace(".",""));
                    viewModel.deposit(headers,id,deposit);
                }catch(Exception e)
                {
                    Toast.makeText(DepositActivity.this, "Lỗi chuyển số", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("deposit", deposit);
                setResult(79, intent);
                finish();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });
    }
}