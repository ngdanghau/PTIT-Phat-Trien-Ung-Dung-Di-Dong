package com.example.prudentialfinance.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.AvatarUpload;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    User AuthUser;
    ImageButton backBtn;
    AppCompatImageView ivEdit;
    ImageView ivAvatar;
    TextView tvEmail;
    EditText firstname, lastname;
    AppCompatButton saveBtn;

    GlobalVariable global;

    LoadingDialog loadingDialog;
    Alert alert;
    Retrofit service = HTTPService.getInstance();
    HTTPRequest api = service.create(HTTPRequest.class);
    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getApplication();
        global = (GlobalVariable) getApplication();
        loadingDialog = new LoadingDialog(ProfileActivity.this);
        alert = new Alert(ProfileActivity.this);

        AuthUser = global.getAuthUser();
        setControl();
        setEvent();

    }

    private void setEvent() {
        alert.normal();


        ivEdit.setOnClickListener(view -> {
            verifyStoragePermissions(this);

            Intent intent = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickerImageActivity.launch(intent);

        });

        backBtn.setOnClickListener(view -> finish());


        alert.btnOK.setOnClickListener(view -> alert.dismiss());


        saveBtn.setOnClickListener(view -> {
            String firstName = firstname.getText().toString().trim();
            String lastName = lastname.getText().toString().trim();
            String action = "save";


            loadingDialog.startLoadingDialog();

            Map<String, String > headers = global.getHeaders();

            Call<Login> container = api.updateProfile(headers, action, firstName, lastName);
            container.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    loadingDialog.dismissDialog();
                    if(response.isSuccessful())
                    {
                        Login resource = response.body();
                        assert resource != null;
                        int result = resource.getResult();

                        if( result == 1 )
                        {
                            global.setAuthUser(resource.getData());
                            Toast.makeText(ProfileActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            alert.showAlert("Oops!", resource.getMsg(), R.drawable.ic_close);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
                }
            });
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> pickerImageActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        selectedImage = data.getData();

                        loadImgToElement(selectedImage.toString(), true);
                    }
                }
            });

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        tvEmail = findViewById(R.id.signUpEmail);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        saveBtn = findViewById(R.id.saveBtn);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivEdit = findViewById(R.id.ivEdit);

        firstname.setText(AuthUser.getFirstname());
        lastname.setText(AuthUser.getLastname());
        tvEmail.setText(AuthUser.getEmail());

        loadImgToElement(HTTPService.UPLOADS_URL + "/"+ AuthUser.getAvatar(), false);

    }

    private void uploadAvatar(){
        // get real path
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        loadingDialog.startLoadingDialog();

        String token = global.getAccessToken();
        File file = new File(picturePath);

        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), "avatar");
        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("file", file.getName(), requestBodyFile);

        Call<AvatarUpload> container = api.uploadAvatar(token, action, fileData);
        container.enqueue(new Callback<AvatarUpload>() {
            @Override
            public void onResponse(@NonNull Call<AvatarUpload> call, @NonNull Response<AvatarUpload> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful())
                {
                    AvatarUpload resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();
                    if( result == 1 )
                    {
                        global.getAuthUser().setAvatar(resource.getImage());
                        Toast.makeText(ProfileActivity.this, resource.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        alert.showAlert("Oops!", resource.getMsg(), R.drawable.ic_close);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AvatarUpload> call, @NonNull Throwable t) {
                loadingDialog.dismissDialog();
                alert.showAlert("Oops!", "Oops! Something went wrong. Please try again later!", R.drawable.ic_close);
            }
        });
    }

    private void loadImgToElement(String img, boolean isUpload){

        Picasso
                .get()
                .load(img)
                .fit()
                .transform(Helper.getRoundedTransformationBuilder())
                .into(ivAvatar,  new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if(isUpload && selectedImage != null){
                            uploadAvatar();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1
            );
        }
    }
}