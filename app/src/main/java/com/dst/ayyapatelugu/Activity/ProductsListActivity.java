package com.dst.ayyapatelugu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dst.ayyapatelugu.Adapter.AyyapamandaliAdapter;
import com.dst.ayyapatelugu.Adapter.GuruSwamiListAdapter;
import com.dst.ayyapatelugu.Adapter.ProductListAdapter;
import com.dst.ayyapatelugu.DataBase.SharedPreferencesHelper;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.BajanaManadaliListModel;
import com.dst.ayyapatelugu.Model.BajanaMandaliList;
import com.dst.ayyapatelugu.Model.GuruSwamiList;
import com.dst.ayyapatelugu.Model.GuruSwamiModelList;
import com.dst.ayyapatelugu.Model.ProductList;
import com.dst.ayyapatelugu.Model.ProductListModel;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
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

public class ProductsListActivity extends AppCompatActivity {
    Toolbar toolbar;

    List<ProductListModel> productList;

    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;

    SharedPreferencesHelper sharedPreferencesHelper;

    private Retrofit retrofit;

    ImageView imageAnadanam,imageNityaPooja;
    TextView textAndanam,txtNityaPooja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

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
                Intent intent=new Intent(ProductsListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });

        textAndanam = findViewById(R.id.layout_txt_anadanam);
        textAndanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductsListActivity.this,AnadanamActivity.class);
                startActivity(intent);
            }
        });
        txtNityaPooja = findViewById(R.id.txt_nitya_pooja);
        txtNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductsListActivity.this, NityaPoojaActivity.class);
                startActivity(intent);

            }
        });
        imageNityaPooja = findViewById(R.id.img_nitya_pooja);
        imageNityaPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductsListActivity.this,NityaPoojaActivity.class);
                startActivity(intent);
            }
        });


        sharedPreferencesHelper= new SharedPreferencesHelper(this);

        recyclerView = findViewById(R.id.recycler_product_list);
        productList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String cachedData = getSharedPreferences("AyyappaProductPrefs", Context.MODE_PRIVATE)
                .getString("Product_list", null);

        if (cachedData != null && !cachedData.isEmpty()) {
            // Use cached data
            productList = parseCachedData(cachedData);
            productListAdapter = new ProductListAdapter(ProductsListActivity.this, productList);
            recyclerView.setAdapter(productListAdapter);
        }  HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
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

        Call<ProductList> call = apiClient.getProductList();
        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.isSuccessful()) {

                    Log.d("API Response", response.body().toString());
                    // Handle successful response
                    ProductList data = response.body();

                    // Save the data to SharedPreferences for future use
                    saveDataToSharedPreferences(data);

                    // Update your UI with the received data
                    productList = data != null ? Arrays.asList(data.getResult()) : new ArrayList<>();
                    productListAdapter = new ProductListAdapter(ProductsListActivity.this, productList);
                    recyclerView.setAdapter(productListAdapter);
                } else {
                    // Handle unsuccessful response
                    // You might want to show an error message or handle it in some way
                    Log.e("API Response", "Unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                // Handle failure, for example, show an error message
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });
    }


    private void saveDataToSharedPreferences(ProductList data) {
        // Save the data to SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("AyyappaProductPrefs", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data.getResult());
        editor.putString("Product_list", json);
        editor.apply();
    }

    private List<ProductListModel> parseCachedData(String cachedData) {
        // Use Gson to parse the JSON string back to your data model
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductListModel>>() {}.getType();
        return gson.fromJson(cachedData, listType);
    }
}