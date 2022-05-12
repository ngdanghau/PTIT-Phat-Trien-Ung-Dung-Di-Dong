package com.example.prudentialfinance.ViewModel.Report;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Report.CategoryReportResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<CategoryReportResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<CategoryReportResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers, String type, String date){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryReportResponse> container;
        if(type == "income"){
            container = api.incomeByDate(headers, date);
        }else {
            container = api.expenseByDate(headers, date);
        }
        container.enqueue(new Callback<CategoryReportResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryReportResponse> call, @NonNull Response<CategoryReportResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryReportResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryReportResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}