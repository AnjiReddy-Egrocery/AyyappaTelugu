package com.example.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayyapatelugu.Adapter.AyyapakaryamListAdappter;
import com.example.ayyapatelugu.HomeActivity;
import com.example.ayyapatelugu.Model.KaryakaramamListModel;
import com.example.ayyapatelugu.Model.KaryakarmamList;
import com.example.ayyapatelugu.R;
import com.example.ayyapatelugu.Services.APiInterface;


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_karyam_list);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
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

        recyclerView=findViewById(R.id.recycler_karyakaramam);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
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
        Call<KaryakarmamList> call=apiClient.getKaryakaramamList();

        call.enqueue(new Callback<KaryakarmamList>() {
            @Override
            public void onResponse(Call<KaryakarmamList> call, Response<KaryakarmamList> response) {
                KaryakarmamList list=response.body();
                karyakaramamListModels=new ArrayList<>(Arrays.asList(list.getResult()));

                ayyapakaryamListAdappter=new AyyapakaryamListAdappter(AyyappaKaryamListActivity.this,karyakaramamListModels);
                recyclerView.setAdapter(ayyapakaryamListAdappter);
            }

            @Override
            public void onFailure(Call<KaryakarmamList> call, Throwable t) {

            }
        });







    }
}