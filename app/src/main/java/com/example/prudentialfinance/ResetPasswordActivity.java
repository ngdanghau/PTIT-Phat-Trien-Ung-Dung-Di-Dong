package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    String email,hash;
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
        Retrofit service = HTTPService.getInstance();
        HTTPRequest request = service.create(HTTPRequest.class);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.startLoadingDialog();

                Call<Login> container = request.process_reset(email,otp.getText().toString(),"check");
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
                                                  hash = resource.getHash();
                                                  Toast.makeText(ResetPasswordActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                                                  changeLayout(); // CHANGE LAYOUT INPUT OTP -> INPUT NEW PASSWORD
                                              }
                                              else
                                              {
                                                  alert.showAlert("Oops",resource.getMsg(), R.drawable.ic_close);
                                                  alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                              }
                                          }
                                          else
                                          {
                                              loadingDialog.dismissDialog();
                                              alert.showAlert("Oops","Response is not success", R.drawable.ic_check);
                                              alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                          }
                                      }

                                      @Override
                                      public void onFailure(Call<Login> call, Throwable t) {
                                          loadingDialog.dismissDialog();
                                          alert.showAlert("Oops","Something went wrong!", R.drawable.ic_check);
                                          alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                                      }
                                  });
            }
        });


        btn_resetpass.setOnClickListener(view -> {
            loadingDialog.startLoadingDialog();
            String password =  txt_password.getText().toString();
            String password_confirm = txt_confirmpassword.getText().toString();
            Call<Login> container = request.reset_pass(email,hash,password,password_confirm,"reset");
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
                            alert.showAlert("Success",resource.getMsg(),R.drawable.ic_check);
                            alert.btnOK.setOnClickListener(view ->
                            {
                                alert.dismiss();
                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        }
                        else
                        {
                            alert.showAlert("Oops",resource.getMsg(), R.drawable.ic_close);
                            alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                        }
                    }
                    else
                    {
                        loadingDialog.dismissDialog();
                        alert.showAlert("Oops","Response is not success", R.drawable.ic_check);
                        alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    alert.showAlert("Oops","Something went wrong!", R.drawable.ic_check);
                    alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                }
            });
        });
    }

    private void validatePassword()
    {
//        VALIDATE
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