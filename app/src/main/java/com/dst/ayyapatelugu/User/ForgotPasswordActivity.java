package com.dst.ayyapatelugu.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dst.ayyapatelugu.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button changePasswordButton;
    EditText newPasswordEditText,confirmPasswordEditText;

    String newPassword,confirmPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        changePasswordButton = findViewById(R.id.but_change_pwd);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newPasswordEditText = findViewById(R.id.edt_new_ped);
                confirmPasswordEditText = findViewById(R.id.edt_confirm_ped);

                newPassword = newPasswordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                if (isValidPasswordChange(newPassword, confirmPassword)) {
                    // Display a success message or navigate to login page
                    Toast.makeText(ForgotPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                } else {
                    // Display an error message or handle the password change failure
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid password change", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isValidPasswordChange(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }
}