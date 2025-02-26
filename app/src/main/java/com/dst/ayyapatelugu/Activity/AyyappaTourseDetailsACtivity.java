package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Adapter.AyyappaTourseDetailsAdapter;

import com.dst.ayyapatelugu.DataBase.SharedPreferencesManager;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.YatraList;
import com.dst.ayyapatelugu.Model.YatraListModel;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.gson.Gson;


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

public class AyyappaTourseDetailsACtivity extends AppCompatActivity {

    Toolbar toolbar;

    List<YatraListModel> yatraListModels;

    RecyclerView recyclerView;

    AyyappaTourseDetailsAdapter ayyappaTourseDetailsAdapter;

    ImageView imageAnadanam,imageNityaPooja;
    TextView textAndanam,txtNityaPooja;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_tourse_details);

        toolbar = findViewById(R.id.toolbar);
        /*toolbar.setLogo(R.drawable.user_profile_background);
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
                Intent intent=new Intent(AyyappaTourseDetailsACtivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaTourseDetailsACtivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        txtNityaPooja = findViewById(R.id.txt_nitya_pooja);
        txtNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AyyappaTourseDetailsACtivity.this, NityaPoojaActivity.class);
                startActivity(intent);

            }
        });
        imageNityaPooja = findViewById(R.id.img_nitya_pooja);
        imageNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AyyappaTourseDetailsACtivity.this,NityaPoojaActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.frecycler_tourse_list);
        yatraListModels=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        YatraList savedYatraList = SharedPreferencesManager.getYatraList(this);
        if (savedYatraList != null) {
            yatraListModels = new ArrayList<>(Arrays.asList(savedYatraList.getResult()));
            ayyappaTourseDetailsAdapter = new AyyappaTourseDetailsAdapter(AyyappaTourseDetailsACtivity.this, yatraListModels);
            recyclerView.setAdapter(ayyappaTourseDetailsAdapter);
        }
        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
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
        Call<YatraList> call = apiClient.getYatraList();

        call.enqueue(new Callback<YatraList>() {
            @Override
            public void onResponse(Call<YatraList> call, Response<YatraList> response) {
                YatraList yatraList = response.body();
                yatraListModels = new ArrayList<>(Arrays.asList(yatraList.getResult()));

                // Save YatraList object to SharedPreferences
                 SharedPreferencesManager.saveYatraList(AyyappaTourseDetailsACtivity.this, yatraList);

                ayyappaTourseDetailsAdapter = new AyyappaTourseDetailsAdapter(AyyappaTourseDetailsACtivity.this, yatraListModels);
                recyclerView.setAdapter(ayyappaTourseDetailsAdapter);
            }

            @Override
            public void onFailure(Call<YatraList> call, Throwable t) {
                if (yatraListModels != null && !yatraListModels.isEmpty()) {
                    Toast.makeText(AyyappaTourseDetailsACtivity.this,"Failed to fetch new data. Using cached data.",Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure when no cached data is available
                    Toast.makeText(AyyappaTourseDetailsACtivity.this,"Failed to fetch data. Please check your network connection.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}