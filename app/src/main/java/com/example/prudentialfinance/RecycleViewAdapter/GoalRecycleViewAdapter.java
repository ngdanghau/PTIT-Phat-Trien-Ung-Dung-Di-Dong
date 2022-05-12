package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class GoalRecycleViewAdapter extends RecyclerView.Adapter<GoalRecycleViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Goal> objects;

    public GoalRecycleViewAdapter(Context content, ArrayList<Goal> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_goal_element, parent, false);
        return new GoalRecycleViewAdapter.ViewHolder(view, parent);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal entry = objects.get(position);

        holder.goal_avt.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC);
        holder.goal_name.setText(Helper.truncate_string(entry.getName(),70, "...", true));
        holder.goal_deadline.setText(Helper.truncate_string(entry.getDeadline(), 70, "...", true));
        Context parentContext = holder.parent.getContext();
        holder.goal_layout.setOnClickListener(view -> {
            System.out.println(entry.getName());
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton goal_avt;
        private TextView goal_name, goal_deadline;
        private LinearLayout goal_layout;
        private ViewGroup parent;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            setControl(itemView);
            this.parent = parent;
        }

        private void setControl(View itemView)
        {
            goal_avt = itemView.findViewById(R.id.goal_avatar);
            goal_name = itemView.findViewById(R.id.goal_name);
            goal_deadline = itemView.findViewById(R.id.goal_deadline);
            goal_layout = itemView.findViewById(R.id.goal_layout);
        }
    }
}
