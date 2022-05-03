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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.ReportTotalBalance;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HeaderMap;

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
     private TextView name, remaining;
     private CircleImageView avatar;

     private User AuthUser;

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


        /*get parameters from home activity sends to this fragment*/
        AuthUser = this.getArguments().getParcelable("AuthUser");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        /*initialize headers to attach HTTP Request*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        /*establish necessary functions*/
        setControl(view);
        setViewModel(view, headers);
        setEvent();
        setScreen();
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
        name = view.findViewById(R.id.fragmentHomeAuthName);
        avatar = view.findViewById(R.id.fragmentHomeAuthAvatar);
        remaining = view.findViewById(R.id.fragmentHomeAuthRemaining);
    }

    /**
     * @author Phong-Kaster
     *
     * @param view is the current context of the fragment
     * @param headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve"})
    private void setViewModel(View view, Map<String, String> headers) {

        Context context = view.getContext();

        /*Step 1*/
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(HomeFragmentViewModel.class);

        /*Step 2*/
        viewModel.getTransactions(headers).observe((LifecycleOwner) context, new Observer<List<TransactionDetail>>() {

            @Override
            public void onChanged(List<TransactionDetail> transactionDetails) {
                setRecycleView(context, headers);
            }
        });

        /*Step 3*/
        String date = "week";

        viewModel.getTotalBalace(headers, date).observe((LifecycleOwner) context, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                String value = Helper.formatDoubleNumber(aDouble);
                remaining.setText( value  );
            }
        });
    }


    /**
     * @author Phong-Kaster
     * @param context is the current context of the fragment
     * @param headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * set up RecyecleView for latest transactions
     * */
    private void setRecycleView(Context context, Map<String, String> headers) {

        List<TransactionDetail> latestTransactions = viewModel.getTransactions(headers).getValue();
        /*Step 1*/
        adapter = new TransactionRecycleViewAdapter(context, latestTransactions );
        recycleView.setAdapter(adapter);


        /*Step 2*/
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

    /**
     * @author Phong-Kaster
     * after receiving data, show them on the screen of Home Fragment
     * */
    private void setScreen()
    {
        String fullName = "Xin chào, "+ AuthUser.getFirstname() + " " + AuthUser.getLastname();
        name.setText(fullName);
    }
}