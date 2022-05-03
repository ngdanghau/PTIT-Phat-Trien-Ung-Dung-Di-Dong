package com.example.prudentialfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;


public class RecoveryActivity extends AppCompatActivity {
    AppCompatButton btn_send;
    EditText txt_Email,txt_password;
    Alert alert;
    LinearLayout input_layout,recovery_layout,temp_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        setControl();
        setEvent();
    }

    private void setControl()
    {
        btn_send = findViewById(R.id.btn_Send);
        txt_Email = findViewById(R.id.emailRecovery);
        input_layout = findViewById(R.id.reset_pass_layout);
        alert = new Alert(RecoveryActivity.this);


        alert.normal();
    }

    private void setEvent()
    {

        final LoadingDialog loadingDialog = new LoadingDialog(RecoveryActivity.this);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit service = HTTPService.getInstance();
                HTTPRequest request = service.create(HTTPRequest.class);
                loadingDialog.startLoadingDialog();

                Call<Login> container = request.recovery(txt_Email.getText().toString().trim());
                container.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if(response.isSuccessful())
                        {
                            Login resource = response.body();
                            assert resource != null;
                            int result = resource.getResult();
                            loadingDialog.dismissDialog();

                            if( result == 1 )
                            {
                                alert.showAlert(resource.getMsg(),R.drawable.check_icon);


                                alert.btnOK.setOnClickListener(view ->{alert.dismiss();
                                       Intent intent = new Intent(RecoveryActivity.this, ResetPasswordActivity.class);
                                       intent.putExtra("email",resource.getEmail());
                                        startActivity(intent);});

                            }
                            else
                            {
                                loadingDialog.dismissDialog();
                                alert.showAlert(resource.getMsg(), R.drawable.info_icon);
                                alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                            }
                        }
                        else
                        {
                            loadingDialog.dismissDialog();
                            alert.showAlert("Response is failed", R.drawable.info_icon);
                            alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        t.printStackTrace();
                        loadingDialog.dismissDialog();
                        alert.showAlert("Something went wrong", R.drawable.info_icon);
                        alert.btnOK.setOnClickListener(view ->{alert.dismiss();});
                    }
                });

            }
        });
    }



}