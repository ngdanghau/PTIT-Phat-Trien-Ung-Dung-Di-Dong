package com.example.prudentialfinance.Activities.Card;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Helpers.NoticeDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CardViewModel;

import java.util.Map;

public class CardUpdateActivity extends AppCompatActivity {

    private ImageButton buttonGoBack;
    private AppCompatButton buttonCreate;
    private ImageButton buttonRemove;

    private EditText cardNumber, cardBalance, cardBank, cardDescription;
    private CardViewModel viewModel;
    private Map<String, String > headers = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_update);



        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*get headers from global variable*/
        headers = ((GlobalVariable)getApplication()).getHeaders();


        /*get account from recycle view*/
        Account account = (Account) getIntent().getSerializableExtra("account");


        setControl();
        setViewModel();
        setEvent(account, headers);

        viewModel.getAccountRemoval().observe(CardUpdateActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if( s.length() > 0)
                {
                    NoticeDialog dialog = new NoticeDialog();
                    dialog.showDialogWithContent(CardUpdateActivity.this, s.trim());
                }
            }
        });

        viewModel.getAccountUpdate().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if( integer == 1)
                {
                    NoticeDialog dialog = new NoticeDialog();
                    dialog.showDialog(CardUpdateActivity.this, R.layout.activity_card_creation_successfully);
                }
                else
                {
                    NoticeDialog dialog = new NoticeDialog();
                    dialog.showDialog(CardUpdateActivity.this, R.layout.activity_card_creation_failed);
                }
            }
        });
    }


    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setControl() {
        buttonGoBack = findViewById(R.id.cardUpdateButtonGoBack);
        buttonCreate = findViewById(R.id.cardUpdateButtonCreate);
        buttonRemove = findViewById(R.id.cardUpdateButtonRemove);


        cardNumber = findViewById(R.id.cardUpdateCardNumber);
        cardBalance = findViewById(R.id.cardUpdateCardBalance);
        cardDescription = findViewById(R.id.cardUpdateCardDescription);
        cardBank = findViewById(R.id.cardUpdateCardBank);
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    /**
     * @author Phong-Kaster
     *
     * @view is the current context of the fragment
     * @headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint("SetTextI18n")
    private void setEvent(Account account,  Map<String, String > headers) {
        cardNumber.setFocusable(false);
        if( account == null)
            return;
        /*Step 1*/
        cardBank.setText( account.getName());
        cardDescription.setText( account.getDescription());
        cardBalance.setText( String.valueOf( account.getBalance()) );
        cardNumber.setText( account.getAccountnumber() );


        /*Step 2*/
        buttonGoBack.setOnClickListener(view->{
            finish();
        });


        /*Step 3*/
        buttonCreate.setOnClickListener(view->{
            int id = account.getId();
            String name = cardBank.getText().toString().trim();
            String balance =  cardBalance.getText().toString().trim();
            String description = cardDescription.getText().toString().trim();
            String number = cardNumber.getText().toString().trim();


            viewModel.updateAccount(headers,id, name, balance, description, number);

        });

        /*Step 4*/
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = account.getId();
                viewModel.deleteAccount(headers, id);
            }
        });
    }
}