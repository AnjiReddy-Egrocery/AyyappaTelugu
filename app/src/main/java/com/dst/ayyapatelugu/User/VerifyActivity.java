package com.dst.ayyapatelugu.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dst.ayyapatelugu.R;

public class VerifyActivity extends AppCompatActivity {

    EditText edtOtp;
    Button butVerify;
    boolean isAllFieldsChecked = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        edtOtp = findViewById(R.id.edt_otp);
        butVerify = findViewById(R.id.but_verify);

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("registerId");
        edtOtp.setText(text);

        butVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = ValidationMethod();
                if (isAllFieldsChecked) {
                    Intent i = new Intent(VerifyActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private boolean ValidationMethod() {
        boolean valid = true;
        String otp = edtOtp.getText().toString();

        if (otp.isEmpty() || otp.length() < 6 || otp.length() > 6) {
            edtOtp.setError("Plz Enter valid Otp");
            valid = false;
        } else {
            edtOtp.setError(null);
        }


        return valid;
    }
}