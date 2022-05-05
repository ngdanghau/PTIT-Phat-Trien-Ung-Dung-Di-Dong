package com.example.prudentialfinance.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Adapter.SettingsAdapter;
import com.example.prudentialfinance.LoginActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    ArrayList<Setting> data = new ArrayList<>();
    SettingsAdapter settingsAdapter;
    ListView lvSettings;
    ImageView ivAvatar;
    TextView fullName, email;
    User authUser;

    AppCompatButton logout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authUser = this.getArguments().getParcelable("AuthUser");
        }
    }

    private void setAuthorizedToken( ) {
        GlobalVariable state = ((GlobalVariable) getActivity().getApplication());
        state.setAccessToken("");
        state.setAuthUser(null);

        SharedPreferences preferences = state.getSharedPreferences(state.getAppName(), state.MODE_PRIVATE);
        preferences.edit().putString("accessToken", "").apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        data.add(new Setting("personal_information", "Thông tin cá nhân", R.drawable.ic_baseline_person_24));
        data.add(new Setting("change_password", "Đổi mật khẩu", R.drawable.ic_baseline_lock_open_24));

        data.add(new Setting("header", "", 0));
        data.add(new Setting("categories", "Thể loại", R.drawable.ic_baseline_category_24));
        data.add(new Setting("goals", "Mục tiêu", R.drawable.ic_baseline_stars_24));


        if(authUser.getAccount_type().equals("admin")){
            data.add(new Setting("header", "", 0));
            data.add(new Setting("site_settings","Cài đặt website", R.drawable.ic_baseline_web_24));
            data.add(new Setting("email_settings","Cài đặt Email", R.drawable.ic_baseline_email_24));

            data.add(new Setting("header", "", 0));
            data.add(new Setting("user_management","Quản lý người dùng", R.drawable.ic_baseline_people_24));
        }

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        lvSettings = view.findViewById(R.id.lvSettings);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);


        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(50)
                .oval(false)
                .build();

        Picasso
                .get()
                .load(HTTPService.UPLOADS_URL + "/"+authUser.getAvatar())
                .fit()
                .transform(transformation)
                .into(ivAvatar);

        fullName.setText(authUser.getFirstname() + " " + authUser.getLastname());
        email.setText(authUser.getEmail());


        settingsAdapter = new SettingsAdapter(getActivity().getApplicationContext(), data);
        lvSettings.setAdapter(settingsAdapter);

        view.findViewById(R.id.logout).setOnClickListener(view1 -> {
            setAuthorizedToken();
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().onBackPressed();
        });

        return view;
    }
}