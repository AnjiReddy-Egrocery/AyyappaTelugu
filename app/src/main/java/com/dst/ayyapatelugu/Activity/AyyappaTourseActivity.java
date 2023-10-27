package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dst.ayyapatelugu.R;
import com.squareup.picasso.Picasso;

public class AyyappaTourseActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView txtName, txtdays, txtdetails, txtamount;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_tourse);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("www.ayyappatelugu.com");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
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

        txtName = findViewById(R.id.txt);
        txtdays = findViewById(R.id.txt_add);
        txtdetails = findViewById(R.id.txt_details);
        txtamount = findViewById(R.id.txt_amount);
        imageView = findViewById(R.id.img);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("Name");
        String days = bundle.getString("Days");
        String details = bundle.getString("Details");
        String amount = bundle.getString("Amount");
        String imagePath = bundle.getString("imagePath");

        txtName.setText(name);
        txtdays.setText(days);
        txtdetails.setText(details);
        txtamount.setText(amount);
        Picasso.get().load(imagePath).into(imageView);


    }
}