package com.example.prudentialfinance.RecycleViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TransactionRecycleViewAdapter extends RecyclerView.Adapter<TransactionRecycleViewAdapter.ViewHolder> {

    private List<TransactionDetail> objects;
    private Context context;

    public TransactionRecycleViewAdapter(Context context, List<TransactionDetail> objects) {
        this.objects = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public TransactionRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_transaction_element, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionDetail detail = objects.get(position);

        String transactionName     = detail.getName();
        String transactionCategory = detail.getCategory().getName();
        String transactionAmount   = String.valueOf(detail.getAmount() );
        String transactionDate     = detail.getTransactiondate();

        holder.name.setText(transactionName);
        holder.category.setText(transactionCategory);
        holder.amount.setText(transactionAmount);
        holder.date.setText(transactionDate);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, transactionName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView category;
        TextView amount;
        TextView date;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            name = itemView.findViewById(R.id.fragmentHomeTransactionName);
            category = itemView.findViewById(R.id.fragmentHomeTransactionCategory);
            amount = itemView.findViewById(R.id.fragmentHomeTransactionAmount);
            date = itemView.findViewById(R.id.fragmentHomeTransactionDate);
            parentLayout = itemView.findViewById(R.id.fragmentHomeTransactionLayout);
        }
    }
}
