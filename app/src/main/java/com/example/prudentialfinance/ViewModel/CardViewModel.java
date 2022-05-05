package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.AccountCreate;
import com.example.prudentialfinance.Container.AccountEdit;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardViewModel extends ViewModel {
    private MutableLiveData<Integer> accountCreation;
    private MutableLiveData<Integer> accountUpdate;

    public MutableLiveData<Integer> getAccountUpdate(Map<String, String> headers, int id, String name, String balance, String description, String accountnumber) {
        if( accountUpdate == null)
        {
            accountUpdate = new MutableLiveData<>();
        }
        updateAccount(headers, id, name, balance, description, accountnumber);
        return accountUpdate;
    }



    public void setAccountUpdate(MutableLiveData<Integer> accountUpdate) {
        this.accountUpdate = accountUpdate;
    }

    public MutableLiveData<Integer> getAccountCreation(Map<String, String> headers, String name, int balance, String description, String accountnumber) {

        if( accountCreation == null)
        {
            accountCreation = new MutableLiveData<>();
            createAccount(headers, name, balance, description, accountnumber);
        }

        return accountCreation;
    }

    public void setAccountCreation(MutableLiveData<Integer> accountCreation) {
        this.accountCreation = accountCreation;
    }

    /**
     * @author Phong-Kaster
     * send HTTP Request to create account
     * */
    public void createAccount(Map<String, String> headers, String name, int balance, String description, String accountnumber)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountCreate> container = api.accountCreate(headers, name, balance, description, accountnumber);


        /*Step 3*/
        container.enqueue(new Callback<AccountCreate>() {
            @Override
            public void onResponse(@NonNull Call<AccountCreate> call,@NonNull Response<AccountCreate> response) {
                if(response.isSuccessful())
                {
                    AccountCreate resource = response.body();
                    int result = resource.getResult();
                    accountCreation.setValue(result);
                }
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountCreate> call,@NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * send HTTP Request to update account
     * */
    private void updateAccount(Map<String, String> headers, int id, String name, String balance, String description, String accountnumber) {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountEdit> container = api.accountUpdate(headers, id,name, balance, description, accountnumber);

        /*Step 3*/
        container.enqueue(new Callback<AccountEdit>() {
            @Override
            public void onResponse(Call<AccountEdit> call, Response<AccountEdit> response) {
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
                if(response.isSuccessful())
                {
                    AccountEdit resource = response.body();

                    assert resource != null;

                    int result = resource.getResult();
                    int account = resource.getAccount();
                    String msg = resource.getMsg();
                    String method = resource.getMethod();

                    System.out.println("RESULT:"+result);
                    System.out.println("ACCOUNT:"+account);
                    System.out.println("MSG: "+msg);
                    System.out.println("METHOD:"+method);
                    accountUpdate.setValue(result);

                }

            }

            @Override
            public void onFailure(Call<AccountEdit> call, Throwable t) {

            }
        });

    }
}
