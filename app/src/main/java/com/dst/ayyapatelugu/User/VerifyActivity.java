package com.dst.ayyapatelugu.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.UserDataResponse;
import com.dst.ayyapatelugu.Model.VerifyUserDataResponse;
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

public class VerifyActivity extends AppCompatActivity {

    EditText edtOtp;
    Button butVerify;
    String registationId="",otp="";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        edtOtp = findViewById(R.id.edt_otp);
        butVerify = findViewById(R.id.but_verify);

        Bundle bundle = getIntent().getExtras();
        registationId = bundle.getString("registerId");
        otp=bundle.getString("otp");
        edtOtp.setText(otp);

        butVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               otp=edtOtp.getText().toString().trim();

               if (!isValidOtp(otp)){
                   edtOtp.setError("Please Enter Valid otp");
               }else {
                   VerificationMethod(registationId,otp);
               }

                if (isValidOtp(otp)){

                    VerificationMethod(registationId,otp);
                } else {
                    Toast.makeText(VerifyActivity.this, "Validation failed. Please check your input.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private boolean isValidOtp(String otp) {
        return otp.length() == 4;
    }

    private void VerificationMethod(String registationId, String otp) {
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
        RequestBody registerIdPart = RequestBody.create(MediaType.parse("text/plain"), registationId);
        RequestBody otpPart = RequestBody.create(MediaType.parse("text/plain"), otp);

        Call<VerifyUserDataResponse> call = apiClient.verifyData(registerIdPart,otpPart);
        call.enqueue(new Callback<VerifyUserDataResponse>() {
            @Override
            public void onResponse(Call<VerifyUserDataResponse> call, Response<VerifyUserDataResponse> response) {
                if (response.isSuccessful()){
                    VerifyUserDataResponse verifyUserDataResponse=response.body();
                    if (verifyUserDataResponse.getErrorCode().equals("201")){
                        Toast.makeText(VerifyActivity.this,"Enter OTP is Invalid",Toast.LENGTH_LONG).show();
                    }else if (verifyUserDataResponse.getErrorCode().equals("200")){
                        Toast.makeText(VerifyActivity.this,"User Verification Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyUserDataResponse> call, Throwable t) {

            }
        });
    }
}