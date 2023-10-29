package com.dst.ayyapatelugu.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.DataBase.SharedPrefManager;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.LoginDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView txtCreateAccount, txtFpwd;

    EditText edtEmail, edtPassword;
    Button butLogin;
    boolean isAllFieldsChecked = false;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    LinearLayout linearAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCreateAccount = findViewById(R.id.txt_create);
        txtFpwd = findViewById(R.id.txt_fwd);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        butLogin = findViewById(R.id.but_login);

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        txtFpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentforgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intentforgot);
            }
        });

        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String  loginMobile =edtEmail.getText().toString();
               String loginPassword=edtPassword.getText().toString();

                if (!isValidEmail(loginMobile)) {
                    edtEmail.setError("Invalid email address");
                    return; // Stop further processing
                }else if (!isValidPassword(loginPassword)) {
                    edtPassword.setError("Invalid password");
                    return; // Stop further processing
                }else {
                    LoginMethod(loginMobile,loginPassword);
                }
            }
        });

        linearAuth = findViewById(R.id.layout_auth);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(LoginActivity.this, gso);


        linearAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void LoginMethod(String parentEmail, String password) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ayyappatelugu.com/") // Replace with your API URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APiInterface apiClient = retrofit.create(APiInterface.class);
        RequestBody parentEmailPart = RequestBody.create(MediaType.parse("text/plain"), parentEmail);
        RequestBody passwordPart = RequestBody.create(MediaType.parse("text/plain"), password);

        Call<LoginDataResponse> call=apiClient.LoginData(parentEmailPart,passwordPart);
        call.enqueue(new Callback<LoginDataResponse>() {
            @Override
            public void onResponse(Call<LoginDataResponse> call, Response<LoginDataResponse> response) {
                if (response.isSuccessful()){
                    LoginDataResponse dataResponse=response.body();
                    if (dataResponse.getErrorCode().equals("201")){
                        Toast.makeText(LoginActivity.this,"Email and Password Doesn't Match",Toast.LENGTH_LONG).show();
                    }else if (dataResponse.getErrorCode().equals("200")){
                        SharedPrefManager.getInstance(getApplicationContext()).insertData(response.body());

                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);

                            startActivity(intent);



                    }
                }
            }

            @Override
            public void onFailure(Call<LoginDataResponse> call, Throwable t) {

            }
        });

    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean isValidEmail(String parentEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return parentEmail.matches(emailPattern);
    }

    private void SignIn() {

        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Something went Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

}

