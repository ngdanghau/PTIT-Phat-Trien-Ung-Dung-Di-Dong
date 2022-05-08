package com.example.prudentialfinance.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.TransactionCreate;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;

public class TransactionViewModel extends ViewModel {
    private final MutableLiveData<Integer> transactionCreation = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionUpdate = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionRemoval = new MutableLiveData<>();


    public MutableLiveData<Integer> getTransactionCreation() {
        return transactionCreation;
    }

    public MutableLiveData<Integer> getTransactionUpdate() {
        return transactionUpdate;
    }

    public MutableLiveData<Integer> getTransactionRemoval() {
        return transactionRemoval;
    }

    public void removeTransaction(Map<String ,String> headers,
                                  String categoryId,
                                  String accountId,
                                  String name,
                                  String amount,
                                  String reference,
                                  String transactionDate,
                                  String type,
                                  String description)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<TransactionCreate> containter = api.transactionCreate(headers,
                categoryId,accountId, name, amount, reference, transactionDate, type, description  );
    }
}
