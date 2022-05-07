package com.example.prudentialfinance.Activities.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.UserRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Users.ListUserViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class UserManagementActivity extends AppCompatActivity {

    ListUserViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;
    GlobalVariable global;

    RecyclerView lvUsers;
    UserRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<User> data;
    int positionItem;
    SwipeRefreshLayout swipeRefreshLayout;
    User entry;

    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        setComponent();

        setControl();

        setEvent();

        loadData();

        setSwipe();
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(ListUserViewModel.class);
    }

    private void setSwipe() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                entry = data.get(position);
                positionItem = position;
                data.remove(position);
                adapter.notifyItemRemoved(position);

                viewModel.deteteItem(headers, entry.getId());

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(UserManagementActivity.this, R.color.colorRed))
                        .addActionIcon(R.drawable.ic_close)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(lvUsers);

    }

    private void loadData() {
        data = new ArrayList<>();
        viewModel.getData(headers, "");

        manager = new LinearLayoutManager(getApplicationContext());
        lvUsers.setLayoutManager(manager);

        adapter = new UserRecycleViewAdapter(getApplicationContext(), data);
        lvUsers.setAdapter(adapter);
    }

    private void setEvent() {
        btnBack.setOnClickListener(view -> finish());

        alert.btnOK.setOnClickListener(view -> alert.dismiss());


        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                data.clear();
                data.addAll(object.getData());
                adapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        viewModel.getOneUser().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if(object.getMethod().equals("DELETE") ){
                if( object.getResult() == 1){
                    Snackbar.make(lvUsers,  entry.getEmail(), Snackbar.LENGTH_LONG)
                            .setAction("Khôi phục", view -> viewModel.restoreUser(headers, entry.getId())).show();
                    return;
                }else{
                    data.add(positionItem, entry);
                    adapter.notifyDataSetChanged();
                }
            }

            if (object.getResult() == 1) {
                data.add(positionItem, entry);
                adapter.notifyDataSetChanged();
            }else{
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, "");
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.backBtn);
        lvUsers = findViewById(R.id.lvUsers);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }
}