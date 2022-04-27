package com.example.prudentialfinance.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.DashboardActivity;
import com.example.prudentialfinance.Model.GlobalVariable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private MutableLiveData<Login> object;
    private Retrofit service;
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * https://codingwithmitch.com/blog/getting-started-with-mvvm-android/#view-models
     *
     * A LiveData object is an observable data-holder class.
     * It's responsible for holding the data that's displayed in the view.
     * But it's not just any old data-holder, the data is observable.
     * Meaning that the data is actively being watched for changes.
     * If a change occurs, the view is updated automatically.
     *
     * MutableLiveData is a subclass of LiveData.
     * You need to use a MutableLiveData object if you want to allow the LiveData object to be changed.
     * LiveData can not be changed by itself.
     * That is, the setValue method is not public for LiveData.
     * But it is public for MutableLiveData.
     * */
    public LiveData<Login> getObject()
    {
        return this.object;
    }





    /**
     * @author Phong-Kaster
     * Step 1: create connect to our server by Retrofit 2. Here it is call HTTP Server
     *
     * Step 2: call api
     *
     * Step 3: wait and catch retured data and use it
     *
     * */
    public void login(Context context, String username, String password)
    {
        /*Step 1*/
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<Login> container = api.login(username, password);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                if(response.isSuccessful())
                {
                    Login resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();

                    if( result == 1 )
                    {
                        setAccessToken( resource.getAccessToken() );

                        Intent intent = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Đăng nhập thành công !", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context, "Tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });

    }

}
