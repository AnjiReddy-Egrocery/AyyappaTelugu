package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.dst.ayyapatelugu.Adapter.AyyappaPetamListAdapteer;

import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.decoratorListModel;
import com.dst.ayyapatelugu.Model.decoratormodelResult;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.UnsafeTrustManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class AyyappaPetamListActivity extends AppCompatActivity {

    Toolbar toolbar;
    private List<decoratormodelResult> decoratorList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AyyappaPetamListAdapteer ayyappaPetamListAdapteer;
    private Retrofit retrofit;

    ImageView imageAnadanam,imageNityaPooja;
    TextView textAndanam,txtNityaPooja;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_petam_list);

        toolbar = findViewById(R.id.toolbar);
        /*toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));*/
        setSupportActionBar(toolbar);
        ;
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
                Intent intent=new Intent(AyyappaPetamListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaPetamListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        txtNityaPooja = findViewById(R.id.txt_nitya_pooja);
        txtNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AyyappaPetamListActivity.this, NityaPoojaActivity.class);
                startActivity(intent);

            }
        });
        imageNityaPooja = findViewById(R.id.img_nitya_pooja);
        imageNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaPetamListActivity.this,NityaPoojaActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recycler_decorator);
        decoratorList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<decoratormodelResult> cachedData = loadDataFromSharedPreferences();

        if (cachedData != null && !cachedData.isEmpty()) {
            // Data is available in SharedPreferences, use it
            decoratorList.addAll(cachedData);
            updateRecyclerView();  // This line updates the RecyclerView immediately
        }

        if (cachedData == null || cachedData.isEmpty()) {
            fetchDataFromApiAndSaveToSQLite();
        }

    }


    private void fetchDataFromApiAndSaveToSQLite() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Change to Level.BASIC for less detail

        // Create OkHttpClient without SSL bypassing
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Add the logging interceptor
                .build();

        // Initialize Retrofit with the OkHttpClient
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ayyappatelugu.com/") // Ensure this is your correct base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APiInterface apiClient = retrofit.create(APiInterface.class);
        Call<decoratorListModel> call = apiClient.getDecoratorsList();


        call.enqueue(new Callback<decoratorListModel>() {
            @Override
            public void onResponse(Call<decoratorListModel> call, Response<decoratorListModel> response) {
                if (response.isSuccessful()) {
                    decoratorListModel decorators = response.body();
                    decoratorList = new ArrayList<>(Arrays.asList(decorators.getResult()));


                    // Save data to SharedPreferences
                    saveDataToSharedPreferences(decoratorList);
                    updateRecyclerView();
                    // Save data to SQLite dat updateRecyclerView();abase


                } else {
                    Log.e("SQLite", "Error: " + response.code() + " " + response.message());
                }
            }
            @Override
            public void onFailure(Call<decoratorListModel> call, Throwable t) {
                // Handle API failure if needed
                Log.e("SQLite", "API call failed: " + t.getMessage());
                List<decoratormodelResult> cachedData = loadDataFromSharedPreferences();
                if (cachedData != null && !cachedData.isEmpty()) {
                    decoratorList.addAll(cachedData);
                    updateRecyclerView();
                }


            }
        });
    }

    private void updateRecyclerView() {
        ayyappaPetamListAdapteer = new AyyappaPetamListAdapteer(AyyappaPetamListActivity.this, decoratorList);
        recyclerView.setAdapter(ayyappaPetamListAdapteer);
    }


    private void saveDataToSharedPreferences(List<decoratormodelResult> decoratorList) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(decoratorList);

        editor.putString("decoratorList", json);
        editor.apply();
    }

    private List<decoratormodelResult> loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("decoratorList", null);

        Type type = new TypeToken<List<decoratormodelResult>>() {}.getType();
        return gson.fromJson(json, type);
    }
}