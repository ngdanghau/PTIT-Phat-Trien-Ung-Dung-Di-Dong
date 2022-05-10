package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.TransactionCreate;
import com.example.prudentialfinance.Container.TransactionRemove;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    /**
     * @author Phong-Kaster
     * create a whole new transaction
     * */
    public void createTransaction(Map<String ,String> headers,
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
        Call<TransactionCreate> container = api.transactionCreate(headers,
                categoryId,
                accountId,
                name,
                amount,
                reference,
                transactionDate,
                type,
                description  );

        /*Step 3*/
        container.enqueue(new Callback<TransactionCreate>() {
            @Override
            public void onResponse(@NonNull Call<TransactionCreate> call,
                                   @NonNull Response<TransactionCreate> response) {
                if( response.isSuccessful())
                {
                    TransactionCreate resource = response.body();

                    assert resource != null;
                    int result = resource.getResult();
                    String msg = resource.getMsg();
                    System.out.println("Transaction View Model - createTransaction - result: " + result);
                    System.out.println("Transaction View Model - createTransaction - msg: " + msg);
                    transactionCreation.setValue(result);
                }
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println("Transaction View Model - createTransaction");
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionCreate> call,
                                  @NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * remove a transaction
     *
     * headers is the header of HTTP Request
     * id is the transaction's id that is being removing
     * */
    public void eradicateTransaction(Map<String ,String> headers, String id)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<TransactionRemove> container = api.transactionRemove(headers, id);

        /*Step 3*/
        container.enqueue(new Callback<TransactionRemove>() {
            @Override
            public void onResponse(@NonNull Call<TransactionRemove> call,
                                   @NonNull Response<TransactionRemove> response) {
                if(response.isSuccessful())
                {
                    TransactionRemove resource = response.body();

                    assert resource != null;
                    int result = resource.getResult();
                    transactionRemoval.setValue(result);
                }
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println("Transaction View Model - eradicateTransaction");
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionRemove> call,
                                  @NonNull Throwable t) {

            }
        });
    }
}
