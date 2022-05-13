package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.prudentialfinance.Activities.Auth.LoginActivity;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Notification;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.ViewModel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private Intent intent;
    /**
     * Activities Order Thread
     * 1. Main Activity
     * 2. Login Activity or Introduce Activity or Sign Up Activity
     * 3. Home Activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        GlobalVariable globalVariable = ((GlobalVariable) this.getApplication());


        SharedPreferences preferences = this.getApplication().getSharedPreferences(globalVariable.getAppName(), this.MODE_PRIVATE);
        String accessToken  = preferences.getString("accessToken",null);
        Boolean isFirstOpen  = preferences.getBoolean("isFirstOpen",true);

        viewModel.getObjectLogin().observe(this, object -> {
            if(object != null && object.getResult() == 1){
                globalVariable.setAuthUser( object.getData() );
                globalVariable.setAccessToken(accessToken);
                intent = new Intent(MainActivity.this, HomeActivity.class);

                String fullName =  object.getData().getFirstname() + " " + object.getData().getLastname();
                Notification notification = new Notification();
                notification.showNotification(MainActivity.this,
                        "Welcome back, " + fullName + " !",
                        "Have a nice day");
            }else{
                intent = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                preferences.edit().putString("accessToken", "").apply();
            }
            openActivity();
        });



        viewModel.getObjectAppInfo().observe(this, object -> {
            if(object == null){
                Alert alert = new Alert(this, 1);
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }
            if(object.getResult() == 1){
                // set app info into global variable
                globalVariable.setAppInfo(object.getData());

                // check token is saved in local
                if(accessToken != null && !accessToken.isEmpty()){
                    viewModel.getInfoUser("JWT " + accessToken);
                }
                else{
                    intent = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                    openActivity();
                }
            }
        });

        viewModel.getInfoSettings();
    }

    private void openActivity(){
        startActivity(intent);
        finish();
    }
}