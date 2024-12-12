package com.dst.ayyapatelugu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.NityaPoojaModel;
import com.dst.ayyapatelugu.Model.SignUpWithGmail;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.UnsafeTrustManager;
import com.dst.ayyapatelugu.User.LoginActivity;
import com.dst.ayyapatelugu.User.RegisterActivity;
import com.squareup.picasso.Picasso;

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

public class NityaPoojaActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView txtName,textDiscription;


    ImageView imageView;

    ImageView imageAnadanam;
    TextView textAndanam;

    String activityId="29";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nitya_pooja);

        toolbar = findViewById(R.id.toolbar);

       /* toolbar.setLogo(R.drawable.user_profile_background);


        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable nav = toolbar.getNavigationIcon();
        if (nav != null) {

            nav.setTint(getResources().getColor(R.color.white));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageAnadanam=findViewById(R.id.layout_image_anadanam);
        imageAnadanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NityaPoojaActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NityaPoojaActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        txtName = findViewById(R.id.txt_name);
        textDiscription = findViewById(R.id.webview);
        imageView = findViewById(R.id.image_view);


        resultMethod(activityId);

    }

    private void resultMethod(String activityId) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(UnsafeTrustManager.createTrustAllSslSocketFactory(), UnsafeTrustManager.createTrustAllTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypasses hostname verification
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ayyappatelugu.com/") // Replace with your API URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APiInterface apiClient = retrofit.create(APiInterface.class);
        RequestBody activityIdPart = RequestBody.create(MediaType.parse("text/plain"), activityId);
        Call<NityaPoojaModel> call = apiClient.PostActivityId(activityIdPart);
        call.enqueue(new Callback<NityaPoojaModel>() {
            @Override
            public void onResponse(Call<NityaPoojaModel> call, Response<NityaPoojaModel> response) {
                if (response.isSuccessful()) {

                    NityaPoojaModel nityaPoojaModel = response.body();
                    if (nityaPoojaModel.getErrorCode().equals("200")) {
                        String title = "";
                        String subtitle = "";
                        String discription = "";
                        String photo="";
                        List<NityaPoojaModel.Result> results = nityaPoojaModel.getResult();
                        title=results.get(0).getTitle();
                        subtitle=results.get(0).getSmallDescription();
                        discription=results.get(0).getDescription();
                        photo=results.get(0).getImage();
                        String url= "https://www.ayyappatelugu.com/assets/activity/"+photo;
                        Log.e("Reddy", "displayname: " + title);
                        Log.e("Reddy", "email: " + subtitle);
                        Log.e("Reddy","Discription"+discription);
                        Log.e("Reddy", "photo: " + "https://www.ayyappatelugu.com/assets/activity/"+photo);

                        txtName.setText(title);
                        Picasso.get().load(url).into(imageView);
                        Spanned spanned= Html.fromHtml(discription);
                        String plainText=spanned.toString();
                        textDiscription.setText(plainText);




                    }

                } else {
                    Toast.makeText(NityaPoojaActivity.this, "Data Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<NityaPoojaModel> call, Throwable t) {

            }
        });
    }
}