package com.dst.ayyapatelugu.Activity;

import static com.dst.ayyapatelugu.Services.UnsafeTrustManager.createTrustAllSslSocketFactory;
import static com.dst.ayyapatelugu.Services.UnsafeTrustManager.createTrustAllTrustManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.dst.ayyapatelugu.Adapter.GuruSwamiListAdapter;

import com.dst.ayyapatelugu.DataBase.SharedPreferencesHelper;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.GuruSwamiList;
import com.dst.ayyapatelugu.Model.GuruSwamiModelList;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.ApiClient;
import com.dst.ayyapatelugu.Services.UnsafeTrustManager;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private Retrofit retrofit;
    ImageView imageAnadanam,imageNityaPooja;
    TextView textAndanam,txtNityaPooja;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_swami_list);

        toolbar = findViewById(R.id.toolbar);
       /* toolbar.setLogo(R.drawable.user_profile_background);
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
                Intent intent=new Intent(GuruSwamiListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuruSwamiListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        txtNityaPooja = findViewById(R.id.txt_nitya_pooja);
        txtNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GuruSwamiListActivity.this, NityaPoojaActivity.class);
                startActivity(intent);

            }
        });
        imageNityaPooja = findViewById(R.id.img_nitya_pooja);
        imageNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuruSwamiListActivity.this,NityaPoojaActivity.class);
                startActivity(intent);
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