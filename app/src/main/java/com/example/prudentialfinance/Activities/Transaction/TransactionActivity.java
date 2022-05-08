package com.example.prudentialfinance.Activities.Transaction;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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

        /*Step 3*/
        swipeToDelete(latestTransactions, recycleView, adapter);
    }

    private void setEvent()
    {
        buttonGoBack.setOnClickListener(view-> finish());

        buttonCreate.setOnClickListener(view -> Toast.makeText(this, "Open transaction create activity", Toast.LENGTH_SHORT).show());
    }

    /**
     * @author Phong-Kaster
     * Swipe from right to left to eradicate a transaction
     * */
    private void swipeToDelete(List<TransactionDetail> transactions, RecyclerView recycleView, TransactionRecycleViewAdapter adapter )
    {
        /*Step 1*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(TransactionActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getLayoutPosition();
                TransactionDetail eradicatedTransaction = transactions.get(position);
                transactions.remove(eradicatedTransaction);
                adapter.notifyItemRemoved(position);

                Snackbar.make(recycleView, eradicatedTransaction.getName(), Snackbar.LENGTH_SHORT)
                        .setAction("Khôi phục", view ->{
                            transactions.add(position, eradicatedTransaction);
                            adapter.notifyItemInserted(position);
                        }).show();
            }

            public void onChildDraw (@NonNull Canvas c,
                                     @NonNull RecyclerView recyclerView,
                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                     float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(TransactionActivity.this, R.color.colorRed))
                        .addActionIcon(R.drawable.ic_baseline_delete_forever_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        /*Step 2*/
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycleView);
    }
}