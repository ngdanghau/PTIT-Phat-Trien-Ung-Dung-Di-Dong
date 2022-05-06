package com.example.prudentialfinance.Activities.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.prudentialfinance.Adapter.CategoryAdapter;
import com.example.prudentialfinance.Fragment.Categories.CategoriesExpenseFragment;
import com.example.prudentialfinance.Fragment.Categories.CategoriesIncomeFragment;
import com.example.prudentialfinance.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoriesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setControl();

        setEvent();
    }

    private void setEvent() {
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.addFragment(new CategoriesIncomeFragment(), getResources().getString(R.string.income));
        categoryAdapter.addFragment(new CategoriesExpenseFragment(), getResources().getString(R.string.expense));

        viewPager.setAdapter(categoryAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(categoryAdapter.getFragmentTitle(position))).attach();
    }

    private void setControl() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
}