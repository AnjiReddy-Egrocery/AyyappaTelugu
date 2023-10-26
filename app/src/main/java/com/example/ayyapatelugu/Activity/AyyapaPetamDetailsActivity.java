package com.example.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayyapatelugu.R;
import com.squareup.picasso.Picasso;

public class AyyapaPetamDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtFname,txtDname,txtCity,txtVillage,txtnumber,txtemail,txtSpecilization,txtDiscription,txtGName;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyapa_petam_details);

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

        imageView=findViewById(R.id.img);
        txtDname=findViewById(R.id.txt_name);
        txtFname=findViewById(R.id.txt_fname);
        txtCity=findViewById(R.id.txt_city);
        txtSpecilization=findViewById(R.id.txt_spec);
        txtVillage=findViewById(R.id.txt_village);
        txtnumber=findViewById(R.id.number);
        txtemail=findViewById(R.id.txt_email);
        txtDiscription=findViewById(R.id.txt_description);
        txtGName=findViewById(R.id.fname);

        Bundle bundle=getIntent().getExtras();

        String dname=bundle.getString("Dname");
        String fname=bundle.getString("Fname");
        String city= bundle.getString("City");
        String Specilization=bundle.getString("Specilization");
        String village=bundle.getString("Village");
        String number=bundle.getString("Number");
        String email=bundle.getString("Email");
        String discription=bundle.getString("Discription");
        String gName=bundle.getString("Fname");
        String imagePath=bundle.getString("ImagePath");

        txtDname.setText(dname);;
        txtFname.setText(fname);
        txtCity.setText(city);
        txtSpecilization.setText(Specilization);
        txtVillage.setText(village);
        txtnumber.setText(number);
        txtemail.setText(email);
        txtDiscription.setText(discription);
        txtGName.setText(gName);
        Picasso.get().load(imagePath).into(imageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDiscription.setText(Html.fromHtml(discription));

        }
    }
}