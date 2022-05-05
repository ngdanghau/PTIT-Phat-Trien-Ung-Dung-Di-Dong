package com.example.prudentialfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String account_type;
    private String email;
    private String firstname;
    private int id;
    private boolean is_active;
    private String date;
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

        GlobalVariable globalVariable = ((GlobalVariable) this.getApplication());


        SharedPreferences preferences = this.getApplication().getSharedPreferences(globalVariable.getAppName(), this.MODE_PRIVATE);
        String accessToken  = preferences.getString("accessToken",null);
        Boolean isFirstOpen  = preferences.getBoolean("isFirstOpen",true);

        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);




//        /**
//         * retrieve user's profile and relative dat. The result could happen 2 situations
//         * First situation: had access token and go into dashboard directly
//         * Second situation: this is first time open application and having 2 intro screen
//         * */
        Thread welcomeThread = new Thread()
        {
            @Override
            public void run()
            {
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

                                /*First situation*/
                                if( result == 1 )
                                {
                                    globalVariable.setAuthUser( resource.getData() );
                                    globalVariable.setAccessToken(accessToken);
                                    i = new Intent(MainActivity.this, HomeActivity.class);
                                }
                                /*Second situation*/
                                else{
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
                }
                else{
                    Intent i = new Intent(MainActivity.this, isFirstOpen ? IntroduceActivity.class : LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        };
        welcomeThread.start();
    }
}