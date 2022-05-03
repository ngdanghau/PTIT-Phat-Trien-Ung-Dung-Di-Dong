package com.example.prudentialfinance.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.prudentialfinance.Adapter.SettingsAdapter;
import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.Settings.ProfileActivity;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    ArrayList<Setting> data = new ArrayList<>();
    SettingsAdapter settingsAdapter;
    ListView lvSettings;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        data.add(new Setting("Thông tin cá nhân", R.drawable.profile_color));
        data.add(new Setting("Đổi mật khẩu", R.drawable.lock_color));
        data.add(new Setting("", 0));
        data.add(new Setting("Cài đặt", R.drawable.settings));
        data.add(new Setting("Cài đặt website", R.drawable.settings));
        data.add(new Setting("Quản lý người dùng", R.drawable.settings));

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        lvSettings = view.findViewById(R.id.lvSettings);

        settingsAdapter = new SettingsAdapter(getActivity().getApplicationContext(), data);
        lvSettings.setAdapter(settingsAdapter);

//        view.findViewById(R.id.topTitle).setOnClickListener(view1 -> {
//            startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
//        });
        return view;
    }
}