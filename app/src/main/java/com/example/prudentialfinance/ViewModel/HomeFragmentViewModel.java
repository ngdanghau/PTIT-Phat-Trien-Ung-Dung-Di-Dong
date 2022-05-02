package com.example.prudentialfinance.ViewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "HomeFragmentViewModel";
    private MutableLiveData<List<TransactionDetail>> transactions;



    public LiveData<List<TransactionDetail>> getTransactions(Context context) {
        if( transactions == null )
        {
            transactions =  new MutableLiveData<List<TransactionDetail>>();
            retrieveDetailTransactions(context);
        }
        return transactions;
    }

    public void setTransactions(MutableLiveData<List<TransactionDetail>> transactions) {
        this.transactions = transactions;
    }


    public void retrieveDetailTransactions(Context context)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Map<String, String> headers = ( (GlobalVariable) context.getApplicationContext()).getHeaders();
        Call<HomeLatestTransactions> container = api.homeLatestTransactions(headers);


        /*Step 3*/
        container.enqueue(new Callback<HomeLatestTransactions>() {
            @Override
            public void onResponse(@NonNull Call<HomeLatestTransactions> call, @NonNull Response<HomeLatestTransactions> response) {
                if(response.isSuccessful())
                {
                    HomeLatestTransactions resource = response.body();

                    assert resource != null;
                    List<TransactionDetail> array = resource.getData();

                    for( TransactionDetail e: array)
                    {
                        System.out.println(e.getName());
                    }

                    transactions.setValue(array);
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
            public void onFailure(Call<HomeLatestTransactions> call, Throwable t) {

            }
        });
    }
}
