package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Model.GlobalVariable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVariable state = ((GlobalVariable) this.getApplication());


        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        String accessToken  = preferences.getString("accessToken",null);
        Boolean isFirstOpen  = preferences.getBoolean("isFirstOpen",true);

        state.setAccessToken(accessToken);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i;
                    if (accessToken == null){
                        i = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                    }else{
                        i = new Intent(MainActivity.this, DashboardActivity.class);
                    }

                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}