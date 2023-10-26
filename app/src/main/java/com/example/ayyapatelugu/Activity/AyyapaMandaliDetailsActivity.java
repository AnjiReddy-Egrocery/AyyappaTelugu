package com.example.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayyapatelugu.Adapter.AyyapamandaliAdapter;
import com.example.ayyapatelugu.Adapter.AyyapamandaliDetailsAdapter;
import com.example.ayyapatelugu.Adapter.GuruSwamiDetailsAdapter;
import com.example.ayyapatelugu.Model.BajanaManadaliListModel;
import com.example.ayyapatelugu.Model.BajanaMandaliList;
import com.example.ayyapatelugu.R;
import com.example.ayyapatelugu.Services.APiInterface;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AyyapaMandaliDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView txtname,txtGuruName,txtVillage,txtNumber,txtEmail,txtDiscription;
    ImageView imageView;

    TabLayout tabLayout;
    ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyapa_mandali_details);


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

        txtname = findViewById(R.id.txt_name);
        txtGuruName = findViewById(R.id.txt_guru_name);
        txtVillage = findViewById(R.id.txt_village);
        txtNumber = findViewById(R.id.txt_number);
        txtEmail = findViewById(R.id.txt_email);
        imageView= findViewById(R.id.image_view);
        txtDiscription =findViewById(R.id.txt_discription);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("ItemName");
        String guruName=bundle.getString("ItemGuruName");
        String village=bundle.getString("ItemCity");
        String number=bundle.getString("ItemNumber");
        String email=bundle.getString("ItemEmail");
        String discription=bundle.getString("Discription");


        txtname.setText(name);
        txtGuruName.setText(guruName);
        txtVillage.setText(village);
        txtNumber.setText(number);
        txtEmail.setText(email);
        txtDiscription.setText(Html.fromHtml(Html.fromHtml(discription).toString()));


        String  image_path= bundle.getString("imagePath");
        Picasso.get().load(image_path).into(imageView);

    }
}