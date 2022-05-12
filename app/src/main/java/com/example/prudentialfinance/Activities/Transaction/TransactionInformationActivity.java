package com.example.prudentialfinance.Activities.Transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.NoticeDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;

public class TransactionInformationActivity extends AppCompatActivity {

    private TransactionDetail transaction;
    private Account atm;
    private NoticeDialog noticeDialog;

    private TextView name,date, amount, account, category, reference, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_information);

        setControl();

        /*Get bundle from transaction recycle view */
        Bundle bundle = getIntent().getExtras();
        if( bundle == null)
        {
            noticeDialog.showDialogWithContent(this,"Đã xảy ra sự cố, vui lòng thử lại");
            return;
        }

        transaction = (TransactionDetail) bundle.get("detail");
        atm = (Account) bundle.get("account");


        setControl();
        setContent();
    }

    private void setControl() {
        name = findViewById(R.id.transactionInforNameContent);
        date = findViewById(R.id.transactionInforDateContent);

        amount = findViewById(R.id.transactionInforAmountContent);
        account = findViewById(R.id.transactionInforAccountContent);

        category = findViewById(R.id.transactionInforCategoryContent);
        reference = findViewById(R.id.transactionInforReferenceContent);

        description = findViewById(R.id.transactionInforDescriptionContent);
    }

    private void setContent()
    {
        String nameContent = transaction.getName().trim();
        String dateContent = transaction.getTransactiondate().trim();

        String amountContent = transaction.getAmount().toString().trim();
        String accountContent = atm.getName().trim();

        String categoryContent = transaction.getCategory().getName().trim();
        String referenceContent = transaction.getReference().trim().length() > 0 ?
                transaction.getReference().trim() : "Không có tham chiếu";

        String descriptionContent = transaction.getDescription().trim().length() > 0 ?
        transaction.getDescription().trim() : "Không có mô tả";

        name.setText(nameContent);
        date.setText(dateContent);

        amount.setText(amountContent);
        account.setText(accountContent);

        category.setText(categoryContent);
        reference.setText(referenceContent);

        description.setText(descriptionContent);
    }
}