package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Dashboard;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

     private static final String TAG = "HomeFragment";
     private ImageButton buttonTransaction;

     private ImageButton buttonIncomeStatistics;
     private ImageButton buttonExpenseStatistics;

     private ImageButton buttonButtonGoal;
     private RecyclerView recycleView;

     private List<TransactionDetail> transactions = new ArrayList<>();
     private TransactionRecycleViewAdapter adapter;

     private HomeFragmentViewModel viewModel;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String name = this.getArguments().getString("name");



        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");



        System.out.println("Bundle access token: " + accessToken);
        System.out.println("Bundle contentType: " + contentType);
        System.out.println("Bundle name: " + name);
        setControl(view);
        setViewModel(view);
        setEvent();

        return view;
    }


    /**
     * @author Phong-Kaster
     * set up button and recycle view to listen event
     * */
    private void setControl(View view) {
        buttonTransaction       = view.findViewById(R.id.fragmentHomeButtonTransactions);
        buttonIncomeStatistics  = view.findViewById(R.id.fragmentHomeButtonIncomeStatistics);
        buttonExpenseStatistics = view.findViewById(R.id.fragmentHomeButtonExpenseStatistics);
        buttonButtonGoal        = view.findViewById(R.id.fragmentHomeButtonGoal);

        recycleView  = view.findViewById(R.id.fragmentHomeRecentTransactions);

    }

    /**
     * @author Phong-Kaster
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve"})
    private void setViewModel(View view) {
        /*Step 1*/
        viewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(HomeFragmentViewModel.class);

        /*Step 2*/

        /*Step 3*/
        viewModel.getTransactions(view.getContext()).observe((LifecycleOwner) view.getContext(), new Observer<List<TransactionDetail>>() {

            @Override
            public void onChanged(List<TransactionDetail> transactionDetails) {
                adapter = new TransactionRecycleViewAdapter(view.getContext(), viewModel.getTransactions(view.getContext()).getValue());
                recycleView.setAdapter(adapter);

                LinearLayoutManager manager =new LinearLayoutManager(view.getContext());
                recycleView.setLayoutManager(manager);
            }
        });
    }


    private void setRecycleView(Context context) {

        Account account = new Account(1,"BIDV", 2000,"0312","Ngan hang BIDV");
        Category category = new Category(20,"Tank",2,"#6CFF5B","Xe tang");
        TransactionDetail detail = new TransactionDetail();
        detail.setAmount(254000);
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
        detail2.setName("Monday");
        detail2.setReference("UK");
        detail2.setTransactiondate("2022-05-02");
        detail2.setId(32);
        detail2.setType(2);
        detail2.setAccount(account);
        detail2.setCategory(category);

        List<TransactionDetail> array = new ArrayList<>();
        array.add(detail);
        array.add(detail2);



        adapter = new TransactionRecycleViewAdapter(context, viewModel.getTransactions(getContext()).getValue() );
        System.out.println("Size: " + adapter.getItemCount());
        recycleView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(manager);
    }

    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setEvent() {
        /**
         * DO NOT REMOVE THIS BLOCK OF CODE BELOW
         * IT REPRESENTS FOR TESTING DARK-MODE FOR EACH SCREEN
         * */
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getContext().setTheme(R.style.Theme_PrudentialFinance_Dark);
        } else {
            getContext().setTheme(R.style.Theme_PrudentialFinance);
        }

        buttonTransaction.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Transaction", Toast.LENGTH_SHORT).show();

            if( AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES )
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

        });

        buttonIncomeStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Income Statistics", Toast.LENGTH_LONG).show();
            }
        });
    }
}