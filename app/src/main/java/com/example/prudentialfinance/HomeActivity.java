package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.AccountGetAll;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Fragment.HomeFragment;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.example.prudentialfinance.databinding.ActivityHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private ActivityHomeBinding binding;
    private Fragment fragment = null;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    break;
                case R.id.shortcutAccount:
                    break;
                case R.id.shortcutSetting:
                    break;
            }
            enableFragment(fragment);
            return true;
        });

    }

    /**
     * @author Phong-Kaster
     * activate a fragment right away
     * */
    private void enableFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String name = "Hau";
        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();

        String accessToken = headers.get("Authorization");
        String contentType = headers.get("Content-Type");

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("accessToken", accessToken);
        bundle.putString("contentType", contentType);

        fragment.setArguments(bundle);

        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}