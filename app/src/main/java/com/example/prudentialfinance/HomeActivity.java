package com.example.prudentialfinance;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prudentialfinance.Fragment.CardFragment;
import com.example.prudentialfinance.Fragment.HomeFragment;
import com.example.prudentialfinance.Fragment.MenuFragment;
import com.example.prudentialfinance.Fragment.ReportFragment;
import com.example.prudentialfinance.Fragment.SettingsFragment;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.databinding.ActivityHomeBinding;

import java.lang.ref.WeakReference;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ActivityHomeBinding binding;
    private Fragment fragment = null;

    public static WeakReference<HomeActivity> weakActivity;

    public static HomeActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*instantiate weak activity*/
        weakActivity = new WeakReference<>(HomeActivity.this);

        /*bind data from home activity*/
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*set up default fragment is home fragment*/
        fragment = new HomeFragment();
        enableFragment(fragment);


        /*Binding bottomNavigationMenu*/
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.shortcutHome:
                    fragment = new HomeFragment();
                    break;
                case R.id.shortcutCard:
                    fragment = new CardFragment();
                    break;
                case R.id.shortcutReport:
                    fragment = new ReportFragment();
                    break;
                case R.id.shortcutSettings:
                    fragment = new SettingsFragment();
                    break;
            }
            enableFragment(fragment);
            return true;
        });


        binding.fab.setOnClickListener(view->{
            Fragment fragment = new MenuFragment();
            enableFragment(fragment);
        });
    }

    /**
     * @author Phong-Kaster
     * activate a fragment right away
     * */
    public void enableFragment(Fragment fragment)
    {
        /*Step 1*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        /*Step 2*/
        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();
        String accessToken = headers.get("Authorization");
        String contentType = headers.get("Content-Type");

        User AuthUser = ((GlobalVariable)getApplication()).getAuthUser();


        /*Step 3*/
        Bundle bundle = new Bundle();

        bundle.putString("accessToken", accessToken);
        bundle.putString("contentType", contentType);
        if(AuthUser != null){
            bundle.putParcelable("AuthUser", AuthUser);
        }
        fragment.setArguments(bundle);


        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, "myFragment");
        transaction.commit();
    }
}