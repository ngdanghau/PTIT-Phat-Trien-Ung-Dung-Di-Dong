package com.example.prudentialfinance.Activities.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Categories.AddCategoryViewModel;
import com.example.prudentialfinance.ViewModel.Goal.GoalAddViewModel;

import java.util.Map;

public class AddGoalActivity extends AppCompatActivity {

    private EditText goal_name,goal_amount,goal_balance,goal_deadline;
    private TextView topTitle;
    private AppCompatButton btn_add;
    private ImageButton btn_back;
    private Goal goal;


    private GoalAddViewModel viewModel;
    private GlobalVariable global;
    private Map<String,String> headers;
    private LoadingDialog loadingDialog;
    private Alert alert;
    private User authUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        goal = (Goal) intent.getSerializableExtra("goal");
        setControl();
        setComponent();
        setData();
        setEvent();
    }

    private void setControl()
    {
        topTitle = findViewById(R.id.goal_topTitle);
        goal_name = findViewById(R.id.goal_name);
        goal_amount = findViewById(R.id.goal_amount);
        goal_balance = findViewById(R.id.goal_balance);
        goal_deadline = findViewById(R.id.goal_date);
        btn_add = findViewById(R.id.Btn_Add_Goal);
        btn_back = findViewById(R.id.backBtnAddGoal);
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(AddGoalActivity.this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(GoalAddViewModel.class);
        authUser = global.getAuthUser();
    }

    private void setData(){
        if(goal.getId()==0)
        {
            topTitle.setText("Thêm mục tiêu");
        }else{
            topTitle.setText("Sửa mục tiêu");
            goal_name.setText(goal.getName());
            goal_amount.setText(String.valueOf(goal.getAmount()));
            goal_balance.setText(String.valueOf(goal.getBalance()));
            goal_deadline.setText(goal.getDeadline());
        }
    }

    private void setEvent()
    {


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal.setName(goal_name.getText().toString());
                goal.setAmount(Long.parseLong(goal_amount.getText().toString()));
                goal.setBalance(Long.parseLong(goal_balance.getText().toString()));
                goal.setDeadline(goal_deadline.getText().toString());

                if(goal.getId()==0)
                {
                    viewModel.saveData(headers,goal);
                }else{
                    viewModel.updateData(headers,goal);

                }
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                goal.setId(object.getGoal());
                Intent intent = new Intent();
                intent.putExtra("goal_entry", goal);
                setResult(78, intent);
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