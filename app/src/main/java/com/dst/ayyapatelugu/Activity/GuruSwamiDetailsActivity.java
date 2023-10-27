package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dst.ayyapatelugu.Adapter.GuruSwamiDetailsAdapter;
import com.dst.ayyapatelugu.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class GuruSwamiDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    TextView txtName, txtNumber, txtTemple, txtcity;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_swami_details);

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

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("వివరణ"));
        tabLayout.addTab(tabLayout.newTab().setText("స్వీయ చరిత్ర"));
        tabLayout.addTab(tabLayout.newTab().setText("సందేశం"));
        final GuruSwamiDetailsAdapter adapter = new GuruSwamiDetailsAdapter(GuruSwamiDetailsActivity.this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        txtName = findViewById(R.id.txt_name);
        txtNumber = findViewById(R.id.txt_number);
        txtTemple = findViewById(R.id.txt_temple);
        txtcity = findViewById(R.id.txt_city);
        imageView = findViewById(R.id.image_view);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("Name");
        String number = bundle.getString("Number");
        String temple = bundle.getString("Temple");
        String city = bundle.getString("City");
        String imagePath = bundle.getString("Image");

        txtName.setText(name);
        txtNumber.setText(number);
        txtTemple.setText(temple);
        txtcity.setText(city);
        Picasso.get().load(imagePath).into(imageView);


    }
}