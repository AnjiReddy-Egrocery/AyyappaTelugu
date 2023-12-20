package com.dst.ayyapatelugu.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.SignUpWithGmail;
import com.dst.ayyapatelugu.Model.UserDataResponse;
import com.dst.ayyapatelugu.Model.VerifyUserDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


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

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int CREDENTIAL_PICKER_REQUEST = 120;
    LinearLayout layoutLogin;
    EditText edtFirstName, edtLastName, edtNumber, edtEmail, edtPassword, edtReenterPassword;
    Button butRegister;

   // LinearLayout linearSignUpWithGmail;

    boolean isAllFieldsChecked = false;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SIGN_UP_WITH_GOOGLE = 9002;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressBar progressBar;

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
        //linearSignUpWithGmail=findViewById(R.id.layout_signup_gmail);
        progressBar = findViewById(R.id.progressBar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        edtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCredentialPicker();
            }
        });

        /*linearSignUpWithGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpWithGoogle(false);
            }
        });*/
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
                String reEnterPassword = edtReenterPassword.getText().toString().trim();


                if (!doPasswordsMatch(password, reEnterPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password and ConfirmPassword do not match", Toast.LENGTH_SHORT).show();

                }

                if (isValidFirstName(name)
                        && isValidLastName(lastname)
                        && isValidEmail(email)
                        && isValidMobileNumber(number)
                        && isValidPassword(password)
                        && doPasswordsMatch(password, reEnterPassword)) {

                    validationMethod(name, lastname, number, email, password);
                }
            }
        });
    }

    private void startCredentialPicker() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(getApplicationContext()).getHintPickerIntent(hintRequest);
        try
        {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0,new Bundle());
        }
        catch (IntentSender.SendIntentException e)
        {
            e.printStackTrace();
        }

    }

    public void ShowHidePass(View view) {

        if(view.getId()==R.id.show_pass_btn){
            if(edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.visiablityoff);
                //Show Password
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.visiablity);
                //Hide Password
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHideConfirmPass(View view) {

        if(view.getId()==R.id.show_Confirmpass_btn){
            if(edtReenterPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.visiablityoff);
                //Show Password
                edtReenterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.visiablity);
                //Hide Password
                edtReenterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void signUpWithGoogle(boolean isSignUp) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, isSignUp ? RC_SIGN_UP_WITH_GOOGLE : RC_SIGN_IN);
    }

    private boolean doPasswordsMatch(String password, String reEnterPassword) {


        return password.equals(reEnterPassword);
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please enter your password",Toast.LENGTH_SHORT).show();
            return false;
        }
        return password.length() >= 6;
    }

    private boolean isValidMobileNumber(String number) {
        if (TextUtils.isEmpty(number)){
            Toast.makeText(RegisterActivity.this,"Please enter your mobile number",Toast.LENGTH_SHORT).show();
            return false;
        }
        return Patterns.PHONE.matcher(number).matches();
    }

    private boolean isValidLastName(String lastname) {
         if (TextUtils.isEmpty(lastname)){
             Toast.makeText(RegisterActivity.this,"Please enter your last name",Toast.LENGTH_SHORT).show();
             return false;
         }
         return true;

    }

    private boolean isValidFirstName(String name) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"Please Enter Your emailId",Toast.LENGTH_SHORT).show();
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void validationMethod(String name, String lastname, String number, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.INVISIBLE);
                    UserDataResponse userDataResponse = response.body();
                    if (userDataResponse.getErrorCode().equals("203")) {
                        Toast.makeText(RegisterActivity.this, "Email and Mobile Number alerady Exists", Toast.LENGTH_SHORT).show();
                    } else if (userDataResponse.getErrorCode().equals("200")) {
                        String registerId = "";
                        String otp = "";
                        Log.e("USERDADA", "list: " + userDataResponse.getResult());
                        List<UserDataResponse.Result> list = userDataResponse.getResult();
                        for (int i = 0; i < list.size(); i++) {
                            registerId = list.get(i).getRegisterId();
                            otp = list.get(i).getOtp();
                            Log.e("registerId", "registerId: " + registerId);
                        }
                        Toast.makeText(RegisterActivity.this, "User Registration Completed Successfully", Toast.LENGTH_LONG).show();
                        /*Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        intent.putExtra("registerId", registerId);
                        intent.putExtra("otp", otp);
                        startActivity(intent);*/

                        conformationDialog(registerId,otp);

                        //otpVerfication(registerId,otp);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Data Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void conformationDialog(String registerId, String otp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);

        CheckBox checkBox = dialogView.findViewById(R.id.simpleCheckBox);
        Button cancelButton = dialogView.findViewById(R.id.but_cancel);
        Button acceptButton = dialogView.findViewById(R.id.but_accept);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Enable or disable the "Accept" button based on checkbox state
            acceptButton.setEnabled(isChecked);
        });

        // Set up listener for the "Accept" button
        acceptButton.setOnClickListener(v -> {
            // Check if the user has agreed to the privacy policy
            if (checkBox.isChecked()) {
                otpVerfication(registerId,otp);
            } else {
                // User didn't agree, show a message or take appropriate action
                Toast.makeText(this, "Please agree to the privacy policy", Toast.LENGTH_SHORT).show();
            }

            // Dismiss the dialog
            builder.create().dismiss();
        });

        // Set up listener for the "Cancel" button
        cancelButton.setOnClickListener(v -> {
            // Perform any necessary actions on cancel button click
            // For example, dismiss the dialog
            builder.create().dismiss();
        });

        builder.create().dismiss();
    }

    private void otpVerfication(String registerId, String otp) {
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
        RequestBody registerIdPart = RequestBody.create(MediaType.parse("text/plain"), registerId);
        RequestBody otpPart = RequestBody.create(MediaType.parse("text/plain"), otp);

        Call<VerifyUserDataResponse> call = apiClient.verifyData(registerIdPart,otpPart);
        call.enqueue(new Callback<VerifyUserDataResponse>() {
            @Override
            public void onResponse(Call<VerifyUserDataResponse> call, Response<VerifyUserDataResponse> response) {
                if (response.isSuccessful()){
                    VerifyUserDataResponse verifyUserDataResponse=response.body();
                    if (verifyUserDataResponse.getErrorCode().equals("201")){
                        Toast.makeText(RegisterActivity.this,"Enter OTP is Invalid",Toast.LENGTH_LONG).show();
                    }else if (verifyUserDataResponse.getErrorCode().equals("200")){
                        Toast.makeText(RegisterActivity.this, "User Registration Completed Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyUserDataResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == RC_SIGN_IN || requestCode == RC_SIGN_UP_WITH_GOOGLE) {
            GoogleSignInAccount account = handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
            if (account != null) {
                if (requestCode == RC_SIGN_IN) {
                    // Handle Google Sign-In for Login
                    handleSignInResult(account);
                } else if (requestCode == RC_SIGN_UP_WITH_GOOGLE) {
                    // Handle Google Sign-In for Sign Up
                    performSignUp(account);
                }
            } else {
                // Handle sign-in/sign-up failure
                //showToast("Failed to sign in/sign up. Please try again.");
            }
        }*/

        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
        {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            edtNumber.setText(credentials.getId().substring(3)); //get the selected phone number
//Do what ever you want to do with your selected phone number here


        }
        else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(RegisterActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }

    }

    private GoogleSignInAccount handleSignInResult(GoogleSignInResult signInResultFromIntent) {
        if (signInResultFromIntent.isSuccess()) {
            return signInResultFromIntent.getSignInAccount();
        } else {
            // Handle sign-in failure
            return null;
        }
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        // Handle the initial sign-in result
        String displayName = account.getDisplayName();
        String email = account.getEmail();
        // Add your sign-in logic or navigate to the appropriate activity
        //showToast("Sign-in successful! Display Name: " + displayName + ", Email: " + email);

        // Example: Navigate to HomeActivity for login
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void performSignUp(GoogleSignInAccount account) {
        // Extract information from the account and perform sign-up

        String displayName = account.getDisplayName();
        String email = account.getEmail();
        String profilepic = String.valueOf(account.getPhotoUrl());

        // Now, send this information to your server for user registration
        sendSignUpDataToServer(displayName, email, profilepic);
    }

    private void sendSignUpDataToServer(String displayName, String email, String profilepic) {

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

        RequestBody displayPar = RequestBody.create(MediaType.parse("text/plain"), displayName);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody photoPart = RequestBody.create(MediaType.parse("text/plain"), profilepic);

        Call<SignUpWithGmail> call = apiClient.PostSignUp(displayPar, emailPart, photoPart);
        call.enqueue(new Callback<SignUpWithGmail>() {
            @Override
            public void onResponse(Call<SignUpWithGmail> call, Response<SignUpWithGmail> response) {
                if (response.isSuccessful()) {

                    SignUpWithGmail signUpWithGmail = response.body();
                    if (signUpWithGmail.getErrorCode().equals("200")) {
                        String displayName = "";
                        String email = "";
                        String photo = "";
                        List<SignUpWithGmail.Result> results = signUpWithGmail.getResult();
                        for (int i = 0; i < results.size(); i++) {
                            displayName = results.get(i).getFullName();
                            email = results.get(i).getUserEmail();
                            photo = results.get(i).getProfilePic();

                            Log.e("Reddy", "displayname: " + displayName);
                            Log.e("Reddy", "email: " + email);
                            Log.e("Reddy", "photo: " + photo);

                        }

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("displayname", displayName);
                        intent.putExtra("email", email);
                        intent.putExtra("profilepic", photo);
                        startActivity(intent);


                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Data Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpWithGmail> call, Throwable t) {

            }
        });
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Connection failed, show error message
        Toast.makeText(RegisterActivity.this, "Connection to Google Play services failed. Please try again.", Toast.LENGTH_LONG).show();
    }


}