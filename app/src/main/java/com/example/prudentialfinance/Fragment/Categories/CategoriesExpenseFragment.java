package com.example.prudentialfinance.Fragment.Categories;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Categories.CategoriesExpenseViewModel;

public class CategoriesExpenseFragment extends Fragment {

    private CategoriesExpenseViewModel mViewModel;

    public static CategoriesExpenseFragment newInstance() {
        return new CategoriesExpenseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories_expense_fragment, container, false);
    }


}