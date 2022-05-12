package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prudentialfinance.Activities.BudgetsActivity;
import com.example.prudentialfinance.Activities.General.CategoriesActivity;
import com.example.prudentialfinance.Activities.General.GoalActivity;
import com.example.prudentialfinance.Activities.Transaction.TransactionActivity;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ImageButton buttonTransaction;
    private LoadingDialog loadingDialog;

    private ImageButton buttonCategory;
    private ImageButton buttonBudget;

    private ImageButton buttonButtonGoal;
    private RecyclerView recycleView;

    private HomeFragmentViewModel viewModel;
    private TextView name, remaining, seeAll;
    private CircleImageView avatar;

    private RelativeLayout transactionsContainer;
    private TextView notice;

    private User AuthUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TransactionRecycleViewAdapter adapter;



    private List<TransactionDetail> objects = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        /*get parameters from home activity sends to this fragment*/
        assert this.getArguments() != null;
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
        setRecycleView(HomeFragment.this);

        viewModel.getAnimation().observe(getViewLifecycleOwner(), aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });



        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactionDetails -> {
            if( transactionDetails.size() > 0)
            {
                objects.clear();
                objects.addAll(transactionDetails);
                adapter.notifyDataSetChanged();


                notice.setVisibility(View.GONE);
                transactionsContainer.setVisibility(View.VISIBLE);
            }
            else
            {
                notice.setVisibility(View.VISIBLE);
                transactionsContainer.setVisibility(View.GONE);
            }
        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.instanciate(headers);
            objects.clear();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    /**
     * @author Phong-Kaster
     * set up button and recycle view to listen event
     * */
    private void setControl(View view) {

        loadingDialog = new LoadingDialog(getActivity());
        swipeRefreshLayout = view.findViewById(R.id.homeFragmentSwipeRefreshLayout);

        buttonTransaction       = view.findViewById(R.id.fragmentHomeButtonTransactions);
        buttonCategory  = view.findViewById(R.id.fragmentHomeButtonCategory);

        buttonBudget = view.findViewById(R.id.fragmentHomeButtonBudget);
        buttonButtonGoal        = view.findViewById(R.id.fragmentHomeButtonGoal);

        recycleView  = view.findViewById(R.id.fragmentHomeRecentTransactions);
        name = view.findViewById(R.id.fragmentHomeAuthName);

        avatar = view.findViewById(R.id.fragmentHomeAuthAvatar);
        remaining = view.findViewById(R.id.fragmentHomeAuthRemaining);

        seeAll = view.findViewById(R.id.homeFragmentSeeAll);
        notice = view.findViewById(R.id.homeFragmentNotice);

        transactionsContainer = view.findViewById(R.id.homeFragmentTransactionLayout);
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
        viewModel.instanciate(headers);

        /*Step 2*/
        viewModel.getTotalBalance().observe((LifecycleOwner) context, aDouble -> {
            String value = Helper.formatNumber(aDouble);
            remaining.setText( value + " VND" );
        });
    }


    /**
     * @author Phong-Kaster
     * @param fragment is the current context of the fragment  */
    private void setRecycleView(HomeFragment fragment) {
        /*Step 1*/
        adapter = new TransactionRecycleViewAdapter(fragment.getContext(), objects);
        recycleView.setAdapter(adapter);


        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(fragment.getContext());
        recycleView.setLayoutManager(manager);
    }

    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setEvent() {

        buttonTransaction.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), TransactionActivity.class);
            startActivity(intent);
        });

        buttonCategory.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
            startActivity(intent);
        });

        buttonButtonGoal.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), GoalActivity.class);
            startActivity(intent);
        });

        buttonBudget.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), BudgetsActivity.class);
            startActivity(intent);
        });

        avatar.setOnClickListener(view -> {
            SettingsFragment fragment = new SettingsFragment();
            ((HomeActivity)requireActivity()).enableFragment(fragment);
        });

        seeAll.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), TransactionActivity.class);
            startActivity(intent);
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