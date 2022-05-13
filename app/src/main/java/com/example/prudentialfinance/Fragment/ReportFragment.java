package com.example.prudentialfinance.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.prudentialfinance.Container.Report.CategoryReport;
import com.example.prudentialfinance.Container.Report.DateRange;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CategoryReportRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Report.ReportViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportFragment extends Fragment{

    ImageButton btnMenu;
    PopupMenu popupMenu;
    View view;
    ViewGroup container;
    TextView topTitle;
    RadioGroup rGroup;
    RadioButton checkedRadioButton;

    ReportViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;
    User authUser;
    SiteSettings appInfo;

    RecyclerView lvCategory;
    CategoryReportRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<CategoryReport> data;
    SwipeRefreshLayout swipeRefreshLayout;
    String typeCategory;
    String typeDate;
    DateRange dateRange;

    public ReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report, container, false);
        this.container = container;

        setComponent();
        setControl();
        setEvent();

        loadData();
        return view;
    }

    private void setComponent() {
        assert this.getArguments() != null;
        authUser = this.getArguments().getParcelable("authUser");
        appInfo = this.getArguments().getParcelable("appInfo");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        // initialize headers to attach HTTP Request
        headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        loadingDialog = new LoadingDialog(this.getActivity());
        alert = new Alert(this.getContext(), 1);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    private void setEvent() {
        btnMenu.setOnClickListener(view -> {
            popupMenu.show();
        });

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.incomeMenu:
                    typeCategory = "income";
                    topTitle.setText(getString(R.string.reportIncome));
                    break;
                case R.id.expenseMenu:
                    typeCategory = "expense";
                    topTitle.setText(getString(R.string.reportExpense));
                    break;
            }
            viewModel.getData(headers, typeCategory, typeDate);
            return true;
        });



        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(getViewLifecycleOwner(), object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                dateRange.setFrom(object.getDate().getFrom());
                dateRange.setTo(object.getDate().getTo());
                data.clear();
                data.addAll(object.getData());
                adapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        rGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checked = (RadioButton) radioGroup.findViewById(i);
            boolean isChecked = checked.isChecked();
            if(isChecked){
                switch (checked.getId()){
                    case R.id.btnWeek:
                        typeDate = "week";
                        break;
                    case R.id.btnMonth:
                        typeDate = "month";
                        break;
                    case R.id.btnYear:
                        typeDate = "year";
                        break;
                }
                viewModel.getData(headers, typeCategory, typeDate);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, typeCategory, typeDate);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setControl() {
        btnMenu = view.findViewById(R.id.btnMenu);
        topTitle = view.findViewById(R.id.topTitle);
        rGroup = view.findViewById(R.id.rGroup);
        checkedRadioButton = rGroup.findViewById(rGroup.getCheckedRadioButtonId());

        popupMenu = new PopupMenu(getContext(), btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.category_menu, popupMenu.getMenu());

        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

    }

    private void loadData() {
        dateRange = new DateRange("", "");
        typeCategory = "income";
        typeDate = "week";
        data = new ArrayList<>();
        viewModel.getData(headers, typeCategory, typeDate);

        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        lvCategory.setLayoutManager(manager);

        adapter = new CategoryReportRecycleViewAdapter(getActivity().getApplicationContext(), data, dateRange, appInfo);
        lvCategory.setAdapter(adapter);
    }
}