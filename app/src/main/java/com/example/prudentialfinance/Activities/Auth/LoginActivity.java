package com.example.prudentialfinance.Activities.Auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Auth.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private  AppCompatButton buttonSignIn;
    private EditText username, password;
    private LoginViewModel viewModel;
    private TextView loginTextViewCreateAccount, txt_forgotpassword;
    GlobalVariable state;

    LoadingDialog loadingDialog;
    Alert alert;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    ImageButton loginSignInWithGoogle, loginSignInWithFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setControl();
        setComponent();
        setEvent();
    }

    private void setComponent() {
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setControl() {
        buttonSignIn = findViewById(R.id.loginButtonSignIn);
        loginSignInWithGoogle = findViewById(R.id.loginSignInWithGoogle);
        loginSignInWithFacebook = findViewById(R.id.loginSignInWithFacebook);
        loginTextViewCreateAccount = findViewById(R.id.loginTextViewCreateAccount);
        username = findViewById(R.id.loginTextViewUsername);
        password = findViewById(R.id.loginTextViewPassword);
        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
    }

    /***
     * @author Phong-Kaster
     *
     * this function set Access token after loginning successfully. Then, this token can be utilized in
     * header. To get Header, we call (Model/Global Variable) getHeader
     *
     */
    private void setAuthorizedToken( String accessToken) {
        state = ((GlobalVariable) this.getApplication());
        state.setAccessToken(accessToken.trim());

        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), this.MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        Alert alert = new Alert(LoginActivity.this);
        alert.normal();

        alert.btnOK.setOnClickListener(view -> {
            alert.dismiss();
        });

        txt_forgotpassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RecoveryActivity.class);
            startActivity(intent);
        });




        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                setAuthorizedToken( object.getAccessToken() );
                state.setAuthUser(object.getData());
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công !", Toast.LENGTH_LONG).show();
                finish();
            } else {
                setAuthorizedToken( "" );
                state.setAuthUser(null);
                alert.showAlert(getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });


        buttonSignIn.setOnClickListener(view->{
            String user = username.getText().toString();
            String pass = password.getText().toString();

            viewModel.login(user, pass);
        });

        loginTextViewCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginSignInWithGoogle.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            loginWithGoogle.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            });


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            viewModel.loginGoogle(idToken);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            alert.showAlert(getString(R.string.alertTitle), "signInResult:failed code=" + e.getStatusCode(), R.drawable.ic_close);
        }
    }
}