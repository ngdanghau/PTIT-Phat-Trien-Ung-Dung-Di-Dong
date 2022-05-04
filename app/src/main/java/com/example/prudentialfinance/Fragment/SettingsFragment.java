package com.example.prudentialfinance.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.prudentialfinance.Adapter.SettingsAdapter;
import com.example.prudentialfinance.LoginActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    ArrayList<Setting> data = new ArrayList<>();
    SettingsAdapter settingsAdapter;
    ListView lvSettings;
    String accountType;

    AppCompatButton logout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountType = this.getArguments().getString("accountType");
        }
    }

    private void setAuthorizedToken( String accessToken) {
        String token = "JWT " +  accessToken.trim();
        GlobalVariable state = ((GlobalVariable) getActivity().getApplication());
        state.setAccessToken(token);

        SharedPreferences preferences = state.getSharedPreferences(state.getAppName(), state.MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        data.add(new Setting("personal_information", "Thông tin cá nhân", R.drawable.profile_color));
        data.add(new Setting("change_password", "Đổi mật khẩu", R.drawable.lock_color));

        if(accountType.equals("admin")){
            data.add(new Setting("header", "", 0));
            data.add(new Setting("setting", "Cài đặt", R.drawable.settings));
            data.add(new Setting("site_settings","Cài đặt website", R.drawable.ic_baseline_web_24));
            data.add(new Setting("email_settings","Cài đặt Email", R.drawable.ic_baseline_email_24));

            data.add(new Setting("header", "", 0));
            data.add(new Setting("user_management","Quản lý người dùng", R.drawable.ic_baseline_people_24));
        }

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        lvSettings = view.findViewById(R.id.lvSettings);

        settingsAdapter = new SettingsAdapter(getActivity().getApplicationContext(), data);
        lvSettings.setAdapter(settingsAdapter);

        view.findViewById(R.id.logout).setOnClickListener(view1 -> {
            setAuthorizedToken("");
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().onBackPressed();
        });

        return view;
    }
}