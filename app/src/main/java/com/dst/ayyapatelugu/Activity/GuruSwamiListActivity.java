package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.dst.ayyapatelugu.Adapter.GuruSwamiListAdapter;

import com.dst.ayyapatelugu.DataBase.SharedPreferencesHelper;
import com.dst.ayyapatelugu.Model.GuruSwamiList;
import com.dst.ayyapatelugu.Model.GuruSwamiModelList;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;


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

public class GuruSwamiListActivity extends AppCompatActivity {

    Toolbar toolbar;

    List<GuruSwamiModelList> guruswamiList;

    RecyclerView recyclerView;

    GuruSwamiListAdapter guruSwamiListAdapter;

    SharedPreferencesHelper sharedPreferencesHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_swami_list);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
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

        sharedPreferencesHelper= new SharedPreferencesHelper(this);

        recyclerView = findViewById(R.id.recycler_guruswami_list);
        guruswamiList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<GuruSwamiModelList> cachedList = sharedPreferencesHelper.getGuruSwamiList();
        if (cachedList != null && !cachedList.isEmpty()) {
            // Step 2: Load cached data into the RecyclerView
            guruswamiList .addAll(cachedList);
            updateRecyclerView();

        }

        if (cachedList == null || cachedList.isEmpty()) {
            fetchGuruSwamiList();
        }



    }

    private void updateRecyclerView() {
        guruSwamiListAdapter = new GuruSwamiListAdapter(GuruSwamiListActivity.this, guruswamiList);
        recyclerView.setAdapter(guruSwamiListAdapter);
    }

    private void fetchGuruSwamiList() {
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
        Call<GuruSwamiList> call = apiClient.getGuruSwamiList();

        call.enqueue(new Callback<GuruSwamiList>() {
            @Override
            public void onResponse(Call<GuruSwamiList> call, Response<GuruSwamiList> response) {
                GuruSwamiList guruSwamiList = response.body();
                guruswamiList = new ArrayList<>(Arrays.asList(guruSwamiList.getResult()));

                // Update the RecyclerView with the latest data

                sharedPreferencesHelper.saveGuruSwamiList(guruswamiList);
                updateRecyclerView();
                //sharedPreferencesHelper.saveLastUpdateTime(System.currentTimeMillis());
            }

            @Override
            public void onFailure(Call<GuruSwamiList> call, Throwable t) {
                // Handle the failure scenario if needed
                // You might want to show an error message to the user

                List<GuruSwamiModelList> cachedList = sharedPreferencesHelper.getGuruSwamiList();
                if (cachedList != null && !cachedList.isEmpty()) {
                    // Step 2: Load cached data into the RecyclerView
                    guruswamiList .addAll(cachedList);
                    updateRecyclerView();

                }


            }
        });
    }


}