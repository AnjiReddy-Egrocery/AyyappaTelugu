package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Adapter.AyyapakaryamListAdappter;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.KaryakaramamListModel;
import com.dst.ayyapatelugu.Model.KaryakarmamList;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.UnsafeTrustManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AyyappaKaryamListActivity extends AppCompatActivity {

    Toolbar toolbar;

    List<KaryakaramamListModel> karyakaramamListModels;

    RecyclerView recyclerView;
    AyyapakaryamListAdappter ayyapakaryamListAdappter;

    private static final String PREF_NAME = "AyyappaData";
    private static final String KEY_DATA = "karyakarmamList";
    ImageView imageAnadanam,imageNityaPooja;
    TextView textAndanam,txtNityaPooja;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_karyam_list);

        toolbar = findViewById(R.id.toolbar);
     /*   toolbar.setLogo(R.drawable.user_profile_background);
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
                Intent intent=new Intent(AyyappaKaryamListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaKaryamListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });
        txtNityaPooja = findViewById(R.id.txt_nitya_pooja);
        txtNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AyyappaKaryamListActivity.this, NityaPoojaActivity.class);
                startActivity(intent);

            }
        });
        imageNityaPooja = findViewById(R.id.img_nitya_pooja);
        imageNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaKaryamListActivity.this,NityaPoojaActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recycler_karyakaramam);
        karyakaramamListModels=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadSavedData();

        if (isNetworkAvailable()) {
            fetchFreshDataInBackground();
        }
      //  fetchDataFromApi();
    }

    private void fetchFreshDataInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchDataFromApi();
            }
        }).start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void fetchDataFromApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(UnsafeTrustManager.createTrustAllSslSocketFactory(), UnsafeTrustManager.createTrustAllTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypasses hostname verification
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ayyappatelugu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APiInterface apiClient = retrofit.create(APiInterface.class);
        Call<KaryakarmamList> call = apiClient.getKaryakaramamList();

        call.enqueue(new Callback<KaryakarmamList>() {
            @Override
            public void onResponse(Call<KaryakarmamList> call, Response<KaryakarmamList> response) {
                if (response.isSuccessful()) {
                    KaryakarmamList list = response.body();
                    karyakaramamListModels = new ArrayList<>(Arrays.asList(list.getResult()));

                    // Save data to SharedPreferences

                    saveDataToSharedPreferences(karyakaramamListModels);
                    // Display the data
                    displayData();
                }
            }

            @Override
            public void onFailure(Call<KaryakarmamList> call, Throwable t) {
                // Handle API call failure
                //Toast.makeText(AyyappaKaryamListActivity.this, "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
                loadSavedData();
            }
        });
    }

    private void displayData() {
        ayyapakaryamListAdappter = new AyyapakaryamListAdappter(AyyappaKaryamListActivity.this, karyakaramamListModels);
        recyclerView.setAdapter(ayyapakaryamListAdappter);
    }

    private void saveDataToSharedPreferences(List<KaryakaramamListModel> data) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(KEY_DATA, json);
        editor.apply();
    }

    private void loadSavedData() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(KEY_DATA, null);

        if (json != null) {
            // Data is available in SharedPreferences, parse and display it
            Type type = new TypeToken<List<KaryakaramamListModel>>(){}.getType();
            karyakaramamListModels = new Gson().fromJson(json, type);
            displayData();
        } else {
            // No data available, show an appropriate message
            fetchDataFromApi();
           // Toast.makeText(this, "No saved data available", Toast.LENGTH_SHORT).show();
        }
    }
}