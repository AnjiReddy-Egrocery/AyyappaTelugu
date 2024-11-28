package com.dst.ayyapatelugu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Adapter.ViewAllTempleListAdapter;
import com.dst.ayyapatelugu.DataBase.SharedPreferencesManager;
import com.dst.ayyapatelugu.Model.TemplesList;
import com.dst.ayyapatelugu.Model.TemplesListModel;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.dst.ayyapatelugu.Services.UnsafeTrustManager;

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

public class TemplesFragment extends Fragment {
    RecyclerView recyclerviewTemples;

    List<TemplesListModel> templeList;

    ViewAllTempleListAdapter viewAllTempleListAdapter;


    private Retrofit retrofit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.temples_fragment,container,false);
        recyclerviewTemples = view.findViewById(R.id.recycler_temples);
        templeList = new ArrayList<>();

        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerviewTemples.setLayoutManager(layoutManager);

        fechedDatafromShredPreferences();

        return view;
    }

    private void fechedDatafromShredPreferences() {
        List<TemplesListModel> templesListModels= SharedPreferencesManager.getTemplesList(getContext());
        if (templesListModels != null && !templesListModels.isEmpty()) {
            // Data exists in SharedPreferences, update RecyclerView
            updateRecyclerView(templesListModels);
        } else {
            // Data doesn't exist in SharedPreferences, fetch from the network
            fetchDataFromDataBase();
        }
    }

    private void fetchDataFromDataBase() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(UnsafeTrustManager.createTrustAllSslSocketFactory(), UnsafeTrustManager.createTrustAllTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypasses hostname verification
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ayyappatelugu.com/") // Replace with your API URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APiInterface apiClient = retrofit.create(APiInterface.class);
        Call<TemplesList> modelCall=apiClient.getTempleList();
        modelCall.enqueue(new Callback<TemplesList>() {
            @Override
            public void onResponse(Call<TemplesList> call, Response<TemplesList> response) {
                TemplesList list=response.body();
                templeList=new ArrayList<>(Arrays.asList(list.getResult()));

                SharedPreferencesManager.saveTempleList(getContext(), templeList);

                updateRecyclerView(templeList);

            }

            @Override
            public void onFailure(Call<TemplesList> call, Throwable t) {
                templeList = SharedPreferencesManager.getTemplesList(getContext());
                if (templeList != null && !templeList.isEmpty()) {
                    // Update the RecyclerView
                    updateRecyclerView(templeList);
                }

            }
        });

    }

    private void updateRecyclerView(List<TemplesListModel> templesListModels) {

        viewAllTempleListAdapter = new ViewAllTempleListAdapter(getContext(),templesListModels);
        recyclerviewTemples.setAdapter(viewAllTempleListAdapter);

    }
}
