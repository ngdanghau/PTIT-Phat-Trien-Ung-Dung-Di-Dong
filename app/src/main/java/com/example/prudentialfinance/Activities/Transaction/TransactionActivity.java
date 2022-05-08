package com.example.prudentialfinance.Activities.Transaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;

import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private HomeFragmentViewModel viewModel;
    private ImageButton buttonGoBack, buttonCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();

        setControl();
        setViewModel(headers);
        setEvent();
    }



    private void setControl() {
        recycleView = findViewById(R.id.transactionRecycleView);
        buttonGoBack = findViewById(R.id.transactionButtonGoBack);
        buttonCreate = findViewById(R.id.transactionButtonCreate);
    }

    /**
     * @author Phong-Kaster
     *
     * @param headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve"})
    private void setViewModel(Map<String, String> headers) {
        /*Step 1*/
        viewModel = new ViewModelProvider( this).get(HomeFragmentViewModel.class);
        viewModel.instanciate(headers);

        /*Step 2*/
        viewModel.getTransactions().observe( this, transactionDetails -> {
            System.out.println(transactionDetails.size());
            setRecycleView();
        });
    }

    /**
     * @author Phong-Kaster
     * */
    private void setRecycleView() {

        List<TransactionDetail> latestTransactions = viewModel.getTransactions().getValue();
        /*Step 1*/
        TransactionRecycleViewAdapter adapter = new TransactionRecycleViewAdapter(this, latestTransactions);
        recycleView.setAdapter(adapter);


        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(manager);
    }

    private void setEvent()
    {
        buttonGoBack.setOnClickListener(view-> finish());

        buttonCreate.setOnClickListener(view -> Toast.makeText(this, "Open transaction create activity", Toast.LENGTH_SHORT).show());
    }
}