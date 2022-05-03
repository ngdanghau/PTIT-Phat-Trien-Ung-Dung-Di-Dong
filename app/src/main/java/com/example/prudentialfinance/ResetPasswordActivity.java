package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Helpers.OTPEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPasswordActivity extends AppCompatActivity {

    OTPEditText otp;
    AppCompatButton btn_send,btn_resetpass;
    Alert alert;
    EditText txt_password,txt_confirmpassword;
    LinearLayout otp_layout,input_layout;
    TextView txt_title,txt_descrip;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setControl();
        setEvent();

    }

    private void setControl(){
        email = getIntent().getStringExtra("email");
        otp = findViewById(R.id.et_otp);
        btn_send = findViewById(R.id.btn_SendOtp);
        btn_resetpass = findViewById(R.id.btn_ResetPass);

        otp_layout = findViewById(R.id.otp_layout);
        input_layout = findViewById(R.id.reset_pass_layout);

        txt_password = findViewById(R.id.txt_password);
        txt_confirmpassword = findViewById(R.id.txt_confirmpassword);

        txt_title = findViewById(R.id.reset_pass_title);
        txt_descrip = findViewById(R.id.reset_pass_description);
        alert = new Alert(this);
        alert.normal();
    }

    private void setEvent(){
        final LoadingDialog loadingDialog = new LoadingDialog(ResetPasswordActivity.this);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit service = HTTPService.getInstance();
                HTTPRequest request = service.create(HTTPRequest.class);
                loadingDialog.startLoadingDialog();

                Call<Login> container = request.reset_pass(email,otp.getText().toString());
                container.enqueue(new Callback<Login>() {
                                      @Override
                                      public void onResponse(Call<Login> call, Response<Login> response) {
                                          if(response.isSuccessful()) // TO DO: FIX SOURCE WEB API STILL CAN't ENTER OTP
                                          {
                                              Login resource = response.body();
                                              assert resource != null;
                                              int result = resource.getResult();
                                              loadingDialog.dismissDialog();

                                              if( result == 1 )
                                              {
                                                  alert.showAlert(resource.getMsg(),R.drawable.check_icon);
                                                  alert.btnOK.setOnClickListener(view ->
                                                  {
                                                      alert.dismiss();
                                                      changeLayout(); // CHANGE LAYOUT INPUT OTP -> INPUT NEW PASSWORD
                                                  });
                                              }
                                              else
                                              {
                                                  alert.showAlert(resource.getMsg()+"\n"+otp.getText(), R.drawable.info_icon);
                                                  alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                              }
                                          }
                                          else
                                          {
                                              loadingDialog.dismissDialog();
                                              alert.showAlert("response is not success", R.drawable.info_icon);
                                              alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                          }
                                      }

                                      @Override
                                      public void onFailure(Call<Login> call, Throwable t) {
                                          loadingDialog.dismissDialog();
                                          alert.showAlert("Something went wrong!", R.drawable.info_icon);
                                          alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                      }
                                  });
            }
        });


    }

    private void changeLayout()
    {

        otp_layout.setVisibility(View.INVISIBLE);
        btn_send.setVisibility(View.INVISIBLE);
// ------------------------------------------------------
        txt_title.setText(R.string.reset_password_btn_reset);
        txt_descrip.setText(R.string.reset_password_description);
        input_layout.setVisibility(View.VISIBLE);
        btn_resetpass.setVisibility(View.VISIBLE);

    }
}