package com.example.prudentialfinance.Activities.Card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prudentialfinance.Helpers.NoticeDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CardViewModel;

import java.util.Map;

public class CardCreationActivity extends AppCompatActivity {

    private ImageButton buttonGoBack;
    private AppCompatButton buttonCreate;
    private TextView cardNumber, cardBalance, cardBank, cardDescription;
    private CardViewModel viewModel;
    private Map<String, String > headers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_card_creation);

        headers = ((GlobalVariable)getApplication()).getHeaders();


        setControl();
        setViewModel();
        setEvent();
    }

    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setControl() {
        buttonGoBack = findViewById(R.id.cardCreationButtonGoBack);
        buttonCreate = findViewById(R.id.cardCreationButtonCreate);
        cardNumber = findViewById(R.id.cardCreationCardNumber);
        cardBalance = findViewById(R.id.cardCreationCardBalance);
        cardDescription = findViewById(R.id.cardCreationCardDescription);
        cardBank = findViewById(R.id.cardCreationCardBank);
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
    private void setEvent() {
        buttonGoBack.setOnClickListener(view->{
            finish();
        });

        buttonCreate.setOnClickListener(view->{
            String number = cardNumber.getText().toString();
            int balance =  Integer.parseInt(cardBalance.getText().toString());
            String bank = cardBank.getText().toString();
            String description = cardDescription.getText().toString();

            viewModel.createAccount(headers, bank, balance, description, number);
        });

        viewModel.getAccountCreation().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if( integer == 1)
                {
                    NoticeDialog dialog = new NoticeDialog();
                    dialog.showDialog(CardCreationActivity.this, R.layout.activity_card_creation_successfully);
                }
                else
                {
                    NoticeDialog dialog = new NoticeDialog();
                    dialog.showDialog(CardCreationActivity.this, R.layout.activity_card_creation_failed);
                }
            }
        });
    }


}