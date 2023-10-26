package com.example.ayyapatelugu.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ayyapatelugu.HomeActivity;
import com.example.ayyapatelugu.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtMobileNum;
    Button butSendOtp;
    boolean isAllFieldsChecked = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtMobileNum=findViewById(R.id.edt_mobile_num);
        butSendOtp=findViewById(R.id.but_send_otp);

        butSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    Intent i = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private boolean CheckAllFields() {
        boolean valid = true;
        String number = edtMobileNum.getText().toString();

        if (number.isEmpty() || number.length() < 10 || number.length() > 10) {
            edtMobileNum.setError("Please Enter Valid Mobile Number");
            valid = false;
        } else {
            edtMobileNum.setError(null);
        }


        return valid;
    }
}