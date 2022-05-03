package com.example.prudentialfinance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.Settings.PasswordActivity;
import com.example.prudentialfinance.Settings.ProfileActivity;
import com.example.prudentialfinance.Settings.SiteSettingsActivity;

import java.util.ArrayList;

public class SettingsAdapter extends BaseAdapter {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private Context context;
    ArrayList<Setting> data;

    public SettingsAdapter(@NonNull Context context, @NonNull ArrayList<Setting> object) {
        this.context = context;
        this.data = object;
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position).getIcon() == 0 ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Setting getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Setting entry = data.get(position);
        int rowType = getItemViewType(position);

        switch (rowType) {
            case TYPE_ITEM:
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_settings_element,null);
                ImageButton iconLeft = convertView.findViewById(R.id.iconLeftList);
                TextView text = convertView.findViewById(R.id.textList);
                iconLeft.setImageResource(entry.getIcon());
                text.setText(entry.getTitle());
                break;
            case TYPE_SEPARATOR:
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_settings_element_header,null);
                break;
        }

        convertView.setOnClickListener(view1 -> {
            if(rowType == TYPE_SEPARATOR) return;
            switch (entry.getId()){
                case "personal_information":
                    parent.getContext().startActivity(new Intent(parent.getContext(), ProfileActivity.class));
                    break;
                case "change_password":
                    parent.getContext().startActivity(new Intent(parent.getContext(), PasswordActivity.class));
                    break;
                case "site_settings":
                    parent.getContext().startActivity(new Intent(parent.getContext(), SiteSettingsActivity.class));
                    break;
            }
        });

        return convertView;
    }
}
