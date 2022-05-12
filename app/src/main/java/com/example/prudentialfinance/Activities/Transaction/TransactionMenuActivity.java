package com.example.prudentialfinance.Activities.Transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.prudentialfinance.R;

public class TransactionMenuActivity extends AppCompatActivity {

    private AppCompatButton income, expense, statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_menu);
        setControl();
        setEvent();
    }



    private void setControl() {
        income = findViewById(R.id.transactionMenuButtonCreateIncome);
        expense = findViewById(R.id.transactionMenuButtonCreateExpense);
        statement = findViewById(R.id.transactionMenuButtonCreateStatement);
    }

    private void setEvent() {
        income.setOnClickListener(view->{
            Intent intent = new Intent(TransactionMenuActivity.this, TransactionCreationActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        });

        expense.setOnClickListener(view->{
            Intent intent = new Intent(TransactionMenuActivity.this, TransactionCreationActivity.class);
            intent.putExtra("type", "2");
            startActivity(intent);
        });

        statement.setOnClickListener(view->{
            Toast.makeText(this,"Tạo sao kê nhưng mà chưa có", Toast.LENGTH_SHORT).show();
        });
    }
}