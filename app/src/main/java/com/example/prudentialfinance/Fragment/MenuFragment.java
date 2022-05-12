package com.example.prudentialfinance.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Menu;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CardRecycleViewAdapter;
import com.example.prudentialfinance.RecycleViewAdapter.MenuRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private ArrayList<Menu> menus = new ArrayList<>();
    private RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        /*Step 1 - add menu into array list*/
        menus.add(new Menu("income_transaction", "Thêm thu nhập", R.drawable.ic_baseline_arrow_circle_right_24));
        menus.add(new Menu("expense_transaction", "Thêm chi tiêu",R.drawable.ic_baseline_arrow_circle_left_24));
        menus.add(new Menu("category", "Thêm danh mục", R.drawable.ic_baseline_category_24));

        setControl(view);
        setRecycleView(view, menus);

        return view;
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.menuFragmentRecycleView);
    }

    private void setRecycleView(View view, ArrayList<Menu> menus) {
        /*Step 0*/
        Context context = view.getContext();

        /*Step 1*/
        MenuRecycleViewAdapter adapter = new MenuRecycleViewAdapter(context, menus);
        recyclerView.setAdapter(adapter);

        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
    }
}