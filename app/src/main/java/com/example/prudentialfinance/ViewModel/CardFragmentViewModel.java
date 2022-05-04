package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.AccountGetAll;
import com.example.prudentialfinance.Model.Account;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Account>> accounts;

    /**GETTER AND SETTER**/
    public MutableLiveData<List<Account>> getAccounts(Map<String, String> headers) {
        if( accounts == null)
        {
            accounts = new MutableLiveData<>();
            retrieveAccounts(headers);
        }
        return accounts;
    }

    public void setAccounts(MutableLiveData<List<Account>> accounts) {
        this.accounts = accounts;
    }

    /**FUNCTIONS**/
    private void retrieveAccounts(Map<String, String> headers) {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountGetAll> container = api.accountGetAll2(headers);


        /*Step 3*/
        container.enqueue(new Callback<AccountGetAll>() {
            @Override
            public void onResponse(@NonNull Call<AccountGetAll> call, @NonNull Response<AccountGetAll> response) {
                if(response.isSuccessful())
                {
                    AccountGetAll resource = response.body();

                    assert resource != null;
                    List<Account> array = resource.getData();

                    accounts.postValue(array);
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
            public void onFailure(@NonNull Call<AccountGetAll> call,@NonNull Throwable t) {

            }
        });
    }
}
