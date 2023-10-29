package com.dst.ayyapatelugu.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Adapter.CalenderAdapter;
import com.dst.ayyapatelugu.Model.CalenderDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.ApiClient;

import java.time.Year;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalenderActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    TextView txtYear;

    private String Calender="";
    private String previousYear="";
    private  String currentYear="";
    private  String nextYear="";
    CalenderAdapter calenderAdapter;
    ImageView imageLeft,imageRight;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user_profile_background);
        txtYear=findViewById(R.id.txt_calender);
        imageLeft=findViewById(R.id.image_left);
        imageRight=findViewById(R.id.image_right);
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

        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextYear != null && !nextYear.isEmpty()) {
                    Calender = nextYear; // Update Calender with the next year
                    VerifyMethod(Calender);

                } else {
                    // Data is missing, handle this case (e.g., show a message)

                    Toast.makeText(CalenderActivity.this, "No data available for the next year.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previousYear != null && !previousYear.isEmpty()) {
                    Calender = previousYear;
                    VerifyMethod(Calender);
                } else {
                    // Data is missing, handle this case (e.g., show a message)
                    Toast.makeText(CalenderActivity.this, "No data available for the next year.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        recyclerView =findViewById(R.id.recycler_calender);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CalenderActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        VerifyMethod(Calender);

    }

    private void VerifyMethod(String calender) {
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
        RequestBody YearPart = RequestBody.create(MediaType.parse("text/plain"), calender);
        Call<CalenderDataResponse> call=apiClient.calenderData(YearPart);
        call.enqueue(new Callback<CalenderDataResponse>() {
            @Override
            public void onResponse(Call<CalenderDataResponse> call, Response<CalenderDataResponse> response) {
               CalenderDataResponse dataResponse=response.body();
                if (dataResponse.getErrorCode().equals("202")) {

                    Toast.makeText(CalenderActivity.this, "Invalid Request, no data found for your request", Toast.LENGTH_SHORT).show();

                } else if (dataResponse.getErrorCode().equals("200")) {



                    List<CalenderDataResponse.Result> results=dataResponse.getResult();
                    for (int i = 0; i < results.size(); i++) {
                        currentYear=results.get(i).getYear();
                        previousYear=results.get(i).getPrevYear();
                        nextYear=results.get(i).getNextYear();
                        List<CalenderDataResponse.Result.Poojas> topicsList = results.get(i).getPoojasList();
                        calenderAdapter = new CalenderAdapter(CalenderActivity.this, topicsList);
                        recyclerView.setAdapter(calenderAdapter);
                    }
                    txtYear.setText(currentYear);
                } else {
                    Toast.makeText(CalenderActivity.this, "Data Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CalenderDataResponse> call, Throwable t) {

            }
        });

    }
}