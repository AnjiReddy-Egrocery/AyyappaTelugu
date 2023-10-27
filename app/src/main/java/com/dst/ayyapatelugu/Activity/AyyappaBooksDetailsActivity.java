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

public class AyyappaBooksDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtName, txtAuthor, txtPublished, txtPages, txtPrice;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyappa_books_details);

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

        txtName = findViewById(R.id.txt_name);
        txtAuthor = findViewById(R.id.txt_author);
        txtPublished = findViewById(R.id.txt_date);
        txtPages = findViewById(R.id.txt_pages);
        txtPrice = findViewById(R.id.txt_price);
        imageView = findViewById(R.id.img);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("Name");
        String Author = bundle.getString("Author");
        String publish = bundle.getString("Published");
        String pages = bundle.getString("Pages");
        String prices = bundle.getString("Price");
        String imagePath = bundle.getString("ImageAuth");

        txtName.setText(name);
        txtAuthor.setText(Author);
        txtPublished.setText(publish);
        txtPages.setText(pages);
        txtPrice.setText(prices);
        Picasso.get().load(imagePath).into(imageView);


    }
}