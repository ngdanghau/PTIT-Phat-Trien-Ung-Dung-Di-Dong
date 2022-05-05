package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.Card.CardIntroduceActivity;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CardRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.CardFragmentViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    private AppCompatButton buttonCreate;
    private ImageButton buttonGoBack;
    private RecyclerView recycleView;
    private CardFragmentViewModel viewModel;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        // Retrieve bundle's arguments
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        /*initialize headers to attach HTTP Request*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);
        setControl(view);
        setViewModel(view, headers);
        setEvent();
        return view;
    }

    private void setControl(View view)
    {
        recycleView = view.findViewById(R.id.cardFragmentRecycleView);
        buttonCreate = view.findViewById(R.id.cardFragmentButtonCreate);
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
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CardFragmentViewModel.class);

        /*Step 2*/
        viewModel.getAccounts(headers).observe((LifecycleOwner) context, accounts -> setRecycleView(view, headers));
    }

    private void setRecycleView(View view, Map<String, String> headers) {
        /*Step 0*/
        List<Account> accounts = viewModel.getAccounts(headers).getValue();
        Context context = view.getContext();

        /*Step 1*/
        CardRecycleViewAdapter adapter = new CardRecycleViewAdapter(context, accounts);
        recycleView.setAdapter(adapter);

        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(manager);
    }

    private void setEvent()
    {
        buttonCreate.setOnClickListener(view->
        {
            Intent intent = new Intent(getContext(), CardIntroduceActivity.class);
            startActivity(intent);
        });
    }
}