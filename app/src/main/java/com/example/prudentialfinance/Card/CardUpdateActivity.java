package com.example.prudentialfinance.Card;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.AccountEdit;
import com.example.prudentialfinance.Helpers.NoticeDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CardViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardUpdateActivity extends AppCompatActivity {

    private ImageButton buttonGoBack;
    private AppCompatButton buttonCreate;
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


    }


    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setControl() {
        buttonGoBack = findViewById(R.id.cardUpdateButtonGoBack);
        buttonCreate = findViewById(R.id.cardUpdateButtonCreate);

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
            viewModel.getAccountUpdate().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    System.out.println(integer);
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

        });
    }
}