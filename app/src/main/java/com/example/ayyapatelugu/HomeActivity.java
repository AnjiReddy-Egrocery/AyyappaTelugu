package com.example.ayyapatelugu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ayyapatelugu.Activity.AboutActivity;
import com.example.ayyapatelugu.Activity.AyyapaBooksListActivity;
import com.example.ayyapatelugu.Activity.AyyappaKaryamListActivity;
import com.example.ayyapatelugu.Activity.AyyappaMandaliListActivity;
import com.example.ayyapatelugu.Activity.AyyappaPetamListActivity;
import com.example.ayyapatelugu.Activity.AyyappaTourseDetailsACtivity;
import com.example.ayyapatelugu.Activity.GuruSwamiListActivity;
import com.example.ayyapatelugu.Adapter.ViewPagerAdapter;
import com.example.ayyapatelugu.User.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Configuration configuration;
    NavigationView mNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    ActionBarDrawerToggle toggle;

    Timer timer;

    int[] images={R.drawable.banarimage,R.drawable.banarimage2,R.drawable.banarimage3};

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView txtName,txtEmail;
    ImageView imageView;


    LinearLayout layoutAyyapaKaryam,layoutGuruswamiList,layoutAyyappaMandali,layoutAyyappaBooks,layoutTourse,layoutDecaration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setLogo(R.drawable.user_profile_background);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer);
        mNavigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        toolbar.setLogo(R.drawable.user_profile_background);
        toolbar.setTitle("స్వామి శరణం అయ్యప్ప");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);;
        toolbar.setLogoDescription(getResources().getString(R.string.title_tool_bar)); // set description for the logo
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewPagerAdapter = new ViewPagerAdapter(HomeActivity.this,images);
        mViewPager.setAdapter(mViewPagerAdapter);
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem((mViewPager.getCurrentItem()+1)%images.length);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 2000, 2000);
        mViewPager.setCurrentItem(0);
        layoutAyyapaKaryam=findViewById(R.id.layout_karyam);
        layoutGuruswamiList=findViewById(R.id.guru_swami_list);
        layoutAyyappaMandali=findViewById(R.id.ayyapa_mandali);
        layoutAyyappaBooks=findViewById(R.id.ayyappa_books);
        layoutTourse=findViewById(R.id.ayyapa_decaration);
        layoutDecaration=findViewById(R.id.ayyappa_tourse);

        layoutAyyapaKaryam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this,AyyappaKaryamListActivity.class);
                startActivity(intent);
            }
        });

        layoutGuruswamiList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,GuruSwamiListActivity.class);
                startActivity(intent);
            }
        });

        layoutAyyappaMandali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,AyyappaMandaliListActivity.class);
                startActivity(intent);
            }
        });

        layoutAyyappaBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this,AyyapaBooksListActivity.class);
                startActivity(intent);
            }
        });

        layoutDecaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this,AyyappaPetamListActivity.class);
                startActivity(intent);
            }
        });

        layoutTourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this, AyyappaTourseDetailsACtivity.class);
                startActivity(intent);
            }
        });
        View headerView = navigationView.getHeaderView(0);
        txtName=(TextView)headerView.findViewById(R.id.txt_name);
        txtEmail=(TextView)headerView.findViewById(R.id.txt_email);
        imageView=(ImageView)headerView.findViewById(R.id.nav_image);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(HomeActivity.this,gso);

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){

            String name= account.getDisplayName().toString();
            String email = account.getEmail().toString();
            String image= String.valueOf(account.getPhotoUrl());
            txtName.setText(name);
            txtEmail.setText(email);
            Picasso.get().load(image).into(imageView);


        }

    }


    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        //MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int action=item.getItemId();
        if (action == R.id.Ayyapa_karyam){

            Intent intent=new Intent(HomeActivity.this, AyyappaKaryamListActivity.class);
            startActivity(intent);

        }else if(action == R.id.guruswami_list){

            Intent intent=new Intent(HomeActivity.this, GuruSwamiListActivity.class);
            startActivity(intent);

        }else if(action == R.id.ayyappa_mandali){

            Intent intent=new Intent(HomeActivity.this, AyyappaMandaliListActivity.class);
            startActivity(intent);

        }else if(action == R.id.ayyappa_decration){

            Intent intent=new Intent(HomeActivity.this, AyyappaPetamListActivity.class);
            startActivity(intent);

        }else if(action == R.id.ayyappa_tours){

            Intent intent=new Intent(HomeActivity.this, AyyappaTourseDetailsACtivity.class);
            startActivity(intent);

        }else if (action == R.id.ayyappa_books){

            Intent intent =new Intent(HomeActivity.this, AyyapaBooksListActivity.class);
            startActivity(intent);
        }else if(action == R.id.ayyappa_details){

            Intent intent=new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if( action == R.id.log_out){
           gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   finish();
                   Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                   startActivity(intent);
               }
           });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}