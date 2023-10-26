package com.example.ayyapatelugu.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayyapatelugu.HomeActivity;
import com.example.ayyapatelugu.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.regex.Pattern;

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

                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }

            }
        });

        linearAuth=findViewById(R.id.layout_auth);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(LoginActivity.this,gso);


        linearAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }

    private void SignIn() {

        Intent intent=gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this,"Something went Wrong",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean CheckAllFields() {

        boolean valid = true;

       // String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();



        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }
}

