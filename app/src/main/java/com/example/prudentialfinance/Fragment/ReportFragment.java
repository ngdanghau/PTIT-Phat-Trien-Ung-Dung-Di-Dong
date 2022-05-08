package com.example.prudentialfinance.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.R;

public class ReportFragment extends Fragment{

    ImageButton btnMenu;
    PopupMenu popupMenu;
    View view;
    ViewGroup container;
    TextView topTitle;
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

        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        btnMenu.setOnClickListener(view -> {
            popupMenu.show();
        });

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.incomeMenu:
                    topTitle.setText(getString(R.string.reportIncome));
                    return true;
                case R.id.expenseMenu:
                    topTitle.setText(getString(R.string.reportExpense));
                    return true;
            }
            return false;
        });
    }

    private void setControl() {
        btnMenu = view.findViewById(R.id.btnMenu);
        topTitle = view.findViewById(R.id.topTitle);

        popupMenu = new PopupMenu(getContext(), btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.category_menu, popupMenu.getMenu());

    }
}