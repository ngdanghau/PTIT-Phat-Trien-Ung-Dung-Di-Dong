package com.example.prudentialfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Model.GlobalVariable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVariable state = ((GlobalVariable) this.getApplication());


        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        String accessToken  = preferences.getString("accessToken",null);
        Boolean isFirstOpen  = preferences.getBoolean("isFirstOpen",true);

        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        Thread welcomeThread = new Thread() {
            @Override
            public void run() {

                if(accessToken != null){
                    Call<Login> container = api.profile("JWT " + accessToken);
                    container.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                            if(response.isSuccessful())
                            {
                                Login resource = response.body();
                                assert resource != null;
                                int result = resource.getResult();

                                Intent i;
                                if( result == 1 )
                                {
                                    state.setAccessToken(accessToken);
                                    i = new Intent(MainActivity.this, DashboardActivity.class);
                                }else{
                                    i = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                                    preferences.edit().putString("accessToken", "").apply();
                                }

                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {

                        }
                    });
                }else{
                    Intent i = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}