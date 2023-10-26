package com.example.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;


import com.example.ayyapatelugu.Adapter.AyyapamandaliAdapter;
import com.example.ayyapatelugu.Adapter.AyyappaPetamListAdapteer;
import com.example.ayyapatelugu.Model.decoratorListModel;
import com.example.ayyapatelugu.Model.decoratormodelResult;
import com.example.ayyapatelugu.R;
import com.example.ayyapatelugu.Services.APiInterface;

import com.google.gson.JsonObject;

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
    List<decoratormodelResult> decoratorList;

    RecyclerView recyclerView;
    AyyappaPetamListAdapteer ayyappaPetamListAdapteer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_petam_list);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable nav = toolbar.getNavigationIcon();
        if(nav != null) {
            nav.setTint(getResources().getColor(R.color.white));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_decorator);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
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
        APiInterface apiClient=retrofit.create(APiInterface.class);
        Call<decoratorListModel> call=apiClient.getDecoratorsList();

        call.enqueue(new Callback<decoratorListModel>() {
            @Override
            public void onResponse(Call<decoratorListModel> call, Response<decoratorListModel> response) {
                decoratorListModel dectors=response.body();
                decoratorList=new ArrayList<>(Arrays.asList(dectors.getResult()));
                ayyappaPetamListAdapteer =new AyyappaPetamListAdapteer(AyyappaPetamListActivity.this,decoratorList);
                recyclerView.setAdapter(ayyappaPetamListAdapteer);
            }

            @Override
            public void onFailure(Call<decoratorListModel> call, Throwable t) {

            }
        });

    }
}