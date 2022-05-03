package com.example.prudentialfinance.ViewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.ReportTotalBalance;
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
    private MutableLiveData<Double> totalBalace;

    public MutableLiveData<Double> getTotalBalace(Map<String, String> headers, String date) {
        if( totalBalace == null)
        {
            totalBalace = new MutableLiveData<Double>();
            retrieveTotalBalance(headers, date);
        }
        return totalBalace;
    }

    public void setTotalBalace(MutableLiveData<Double> totalBalace) {
        this.totalBalace = totalBalace;
    }

    public LiveData<List<TransactionDetail>> getTransactions(Map<String, String> headers) {
        if( transactions == null )
        {
            transactions =  new MutableLiveData<List<TransactionDetail>>();
            retrieveDetailTransactions(headers);
        }
        return transactions;
    }


    public void setTransactions(MutableLiveData<List<TransactionDetail>> transactions) {
        this.transactions = transactions;
    }

    /**
     * @author Phong-Kaster
     *
     * get all latest transactions in the last 7 days
     *
     * @param headers headers is used to attach to HTTP Request headers
     * */
    public void retrieveDetailTransactions(Map<String, String> headers)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
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

    /**
     * @author Phong-Kaster
     *
     * get account's remaining in the 7 days
     *
     * @param headers headers is used to attach to HTTP Request headers
     * */
    public void retrieveTotalBalance(Map<String, String> headers,String date)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        Call<ReportTotalBalance> container = api.reportTotalBalace(headers, date);

        /*Step 3*/
        container.enqueue(new Callback<ReportTotalBalance>() {
            @Override
            public void onResponse(Call<ReportTotalBalance> call, Response<ReportTotalBalance> response) {
                if(response.isSuccessful())
                {
                    ReportTotalBalance resource = response.body();
                    System.out.println("Line 133");
                    System.out.println("result:" + resource.getResult());
                    System.out.println("total balance:" + resource.getWeek());
                    System.out.println("method:" + resource.getMethod());
                    totalBalace.setValue(resource.getWeek());
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
            public void onFailure(Call<ReportTotalBalance> call, Throwable t) {

            }
        });
    }
}
