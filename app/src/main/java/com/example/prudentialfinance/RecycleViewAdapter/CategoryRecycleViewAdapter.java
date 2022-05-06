package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.General.AddCategoryActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<CategoryRecycleViewAdapter.ViewHolder> {
    private ArrayList<Category> objects;
    private Context context;


    public CategoryRecycleViewAdapter(Context context, ArrayList<Category> objects) {
        this.objects = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.activity_category_element, parent, false);
        return new ViewHolder(view, parent);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CategoryRecycleViewAdapter.ViewHolder holder, int position) {
        Category entry = objects.get(position);

        holder.cat_color.getBackground().setColorFilter(Color.parseColor(entry.getColor()), PorterDuff.Mode.SRC);
        holder.cat_name.setText(Helper.truncate_string(entry.getName(), 70, "...", true));
        holder.cat_desc.setText(Helper.truncate_string(entry.getDescription(), 70, "...", true));

        Context parentContext = holder.parent.getContext();
        holder.cat_layout.setOnClickListener(view1 -> {
            Intent intent = new Intent(parentContext, AddCategoryActivity.class);
            intent.putExtra("category_id", entry.getId());
            intent.putExtra("category_name", entry.getName());
            intent.putExtra("category_desc", entry.getDescription());
            intent.putExtra("category_type", entry.getType());
            intent.putExtra("category_color", entry.getColor());
            parentContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton cat_color;
        private TextView cat_name, cat_desc;
        private LinearLayout cat_layout;
        private ViewGroup parent;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            setControl(itemView);
            this.parent = parent;
        }

        private void setControl(View itemView)
        {
            cat_color = itemView.findViewById(R.id.cat_color);
            cat_name = itemView.findViewById(R.id.cat_name);
            cat_desc = itemView.findViewById(R.id.cat_desc);
            cat_layout = itemView.findViewById(R.id.cat_layout);
        }
    }
}
