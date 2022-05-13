package com.example.prudentialfinance.Activities.Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.NoticeDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;

import java.lang.ref.WeakReference;

public class TransactionInformationActivity extends AppCompatActivity {

    private TransactionDetail transaction;
    private Account atm;
    private NoticeDialog noticeDialog;

    private TextView name,date, amount, account, category, reference, description;
    private AppCompatButton buttonEdit;
    public static WeakReference<TransactionInformationActivity> weakActivity;

    public static TransactionInformationActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_information);

        /*instantiate weak activity*/
        weakActivity = new WeakReference<>(TransactionInformationActivity.this);

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
        setEvent();
    }

    private void setControl() {
        name = findViewById(R.id.transactionInforNameContent);
        date = findViewById(R.id.transactionInforDateContent);

        amount = findViewById(R.id.transactionInforAmountContent);
        account = findViewById(R.id.transactionInforAccountContent);

        category = findViewById(R.id.transactionInforCategoryContent);
        reference = findViewById(R.id.transactionInforReferenceContent);

        description = findViewById(R.id.transactionInforDescriptionContent);
        buttonEdit = findViewById(R.id.transactionInforButtonEdit);
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

    /**
     * @author Phong-Kaster
     * set event for each component
     * */
    private void setEvent()
    {
        buttonEdit.setOnClickListener(view->{
            /*Step 1*/
            Bundle bundle = new Bundle();
            bundle.putParcelable("transaction", transaction);
            bundle.putParcelable("atm", atm);
            /*Step 2*/
            Intent intent = new Intent(TransactionInformationActivity.this, TransactionUpdateActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    public void setContentWithValue(String nameContent, String dateContent,
                                    String amountContent, String accountContent,
                                    String categoryContent, String referenceContent,
                                    String descriptionContent)
    {
        name.setText(nameContent);
        date.setText(dateContent);

        amount.setText(amountContent);
        account.setText(accountContent);

        category.setText(categoryContent);
        reference.setText(referenceContent);

        description.setText(descriptionContent);
    }
}