package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    List<TransactionDetail> transactions = new ArrayList<>();
    HomeFragmentViewModel viewModel;
    TransactionRecycleViewAdapter adapter;
    RecyclerView transactionRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        transactionRecycleView = findViewById(R.id.dashboardRecentTractions);
        setViewModel();
//        setRecycleView();
    }

    private void setViewModel() {
        /*Step 1*/
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        /*Step 2*/
//        viewModel.retrieveDetailTransactions(this);



        /*Step 3*/
        viewModel.getTransactions(this).observe(this, new Observer<List<TransactionDetail>>() {
            @Override
            public void onChanged(List<TransactionDetail> transactionDetails) {
                System.out.println("transaction size: " + transactionDetails.size());
                adapter = new TransactionRecycleViewAdapter(Dashboard.this, viewModel.getTransactions(Dashboard.this).getValue());
                transactionRecycleView.setAdapter(adapter);

                LinearLayoutManager manager =new LinearLayoutManager(Dashboard.this);
                transactionRecycleView.setLayoutManager(manager);
            }
        });

        //# callback setRecycleView()
    }

    private void setRecycleView() {

        Account account = new Account(1,"BIDV", 2000,"0312","Ngan hang BIDV");
        Category category = new Category(20,"Tank",2,"#6CFF5B","Xe tang");
        TransactionDetail detail = new TransactionDetail();
        detail.setAmount(254);
        detail.setDescription("abc");
        detail.setName("Monday");
        detail.setReference("germany");
        detail.setTransactiondate("2022-05-02");
        detail.setId(32);
        detail.setType(2);
        detail.setAccount(account);
        detail.setCategory(category);

        TransactionDetail detail2 = new TransactionDetail();
        detail2.setAmount(123000);
        detail2.setDescription("abc");
        detail2.setName("Tueday");
        detail2.setReference("UK");
        detail2.setTransactiondate("2022-05-02");
        detail2.setId(32);
        detail2.setType(2);
        detail2.setAccount(account);
        detail2.setCategory(category);

//        transactions.add(detail);
//        transactions.add(detail2);


        adapter = new TransactionRecycleViewAdapter(this, viewModel.getTransactions(this).getValue());
        transactionRecycleView.setAdapter(adapter);

        LinearLayoutManager manager =new LinearLayoutManager(this);
        transactionRecycleView.setLayoutManager(manager);
    }
}