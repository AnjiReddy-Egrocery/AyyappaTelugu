package com.dst.ayyapatelugu.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.UserDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;


import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout layoutLogin;
    EditText edtFirstName, edtLastName, edtNumber, edtEmail, edtPassword, edtReenterPassword;
    Button butRegister;

    boolean isAllFieldsChecked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFirstName = findViewById(R.id.edt_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtNumber = findViewById(R.id.edt_number);
        edtEmail = findViewById(R.id.edt_mail);
        edtPassword = findViewById(R.id.edt_password);
        edtReenterPassword = findViewById(R.id.edt_reenter_password);
        layoutLogin = findViewById(R.id.layout_login);
        butRegister = findViewById(R.id.but_register);

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = edtFirstName.getText().toString().trim();
                String lastname = edtLastName.getText().toString().trim();
                String number = edtNumber.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String reEnterPassword=edtReenterPassword.getText().toString().trim();

                if (isValidFirstName(name)
                        && isValidLastName(lastname)
                        && isValidEmail(email)
                        && isValidMobileNumber(number)
                        && isValidPassword(password)
                        && doPasswordsMatch(password,reEnterPassword)) {

                    validationMethod(name, lastname, number, email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Validation failed. Please check your input.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean doPasswordsMatch(String password, String reEnterPassword) {
        return password.equals(reEnterPassword);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean isValidMobileNumber(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    private boolean isValidLastName(String lastname) {
        return !TextUtils.isEmpty(lastname);
    }

    private boolean isValidFirstName(String name) {
        return !TextUtils.isEmpty(name);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void validationMethod(String name, String lastname, String number, String email, String password) {
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
        RequestBody firstnamePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody lastnamePart = RequestBody.create(MediaType.parse("text/plain"), lastname);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mobileNumberPart = RequestBody.create(MediaType.parse("text/plain"), number);
        RequestBody pwdPart = RequestBody.create(MediaType.parse("text/plain"), password);
        Call<UserDataResponse> call = apiClient.postData(firstnamePart, lastnamePart, emailPart, mobileNumberPart, pwdPart);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    UserDataResponse userDataResponse = response.body();
                    if (userDataResponse.getErrorCode().equals("203")) {
                        Toast.makeText(RegisterActivity.this, "Email and Mobile Number alerady Exists", Toast.LENGTH_SHORT).show();
                    } else if (userDataResponse.getErrorCode().equals("200")) {
                        String registerId = "";
                        String otp="";
                        Log.e("USERDADA", "list: " + userDataResponse.getResult());
                        List<UserDataResponse.Result> list = userDataResponse.getResult();
                        for (int i = 0; i < list.size(); i++) {
                            registerId = list.get(i).getRegisterId();
                            otp=list.get(i).getOtp();
                            Log.e("registerId", "registerId: " + registerId);
                        }
                        Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        intent.putExtra("registerId", registerId);
                        intent.putExtra("otp",otp);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Data Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {

            }
        });
    }
}