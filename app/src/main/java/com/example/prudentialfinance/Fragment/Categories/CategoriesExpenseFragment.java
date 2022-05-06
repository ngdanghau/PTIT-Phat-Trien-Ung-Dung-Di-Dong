package com.example.prudentialfinance.Fragment.Categories;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CategoryRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Categories.CategoriesExpenseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoriesExpenseFragment extends Fragment {

    CategoriesExpenseViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;
    User authUser;

    RecyclerView lvCategory;
    CategoryRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<Category> data;

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.categories_expense_fragment, container, false);

        setComponent();

        setControl(view);

        setEvent();

        loadData();
        return view;
    }

    private void setControl(View view) {
        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    public void getData(String query){
        data.clear();
        viewModel.getData(headers, query);
    }

    private void loadData() {
        data = new ArrayList<>();
        viewModel.getData(headers, "");

        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        lvCategory.setLayoutManager(manager);

        adapter = new CategoryRecycleViewAdapter(getActivity().getApplicationContext(), data);
        lvCategory.setAdapter(adapter);
    }



    private void setComponent() {
        assert this.getArguments() != null;
        authUser = this.getArguments().getParcelable("authUser");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        // initialize headers to attach HTTP Request
        headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        loadingDialog = new LoadingDialog(this.getActivity());
        alert = new Alert(this.getContext(), 1);
        viewModel = new ViewModelProvider(this).get(CategoriesExpenseViewModel.class);
    }


    private void setEvent() {

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe((LifecycleOwner) this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe((LifecycleOwner) this, object -> {
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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, "");
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}