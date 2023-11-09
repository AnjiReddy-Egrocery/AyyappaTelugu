package com.dst.ayyapatelugu.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.MapDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnadanamActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    private GoogleMap mMap;
    private APiInterface apiClient;
    private Context context;

    private static final int SEARCH_RADIUS_METERS = 5000; // 5 km
    String locations;
    ImageButton zoomInButton, zoomOutButton;

    private float currentZoomLevel = 15.0f;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadanam);

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

        zoomInButton = findViewById(R.id.zoom_in_button);
        zoomOutButton = findViewById(R.id.zoom_out_button);

       if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are granted, fetch the user's location
            displayCurrentUserLocation();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            } else {
                Log.e("MapFragment", "Map fragment is null.");
            }
        }

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomInMap();
            }
        });
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOutMap();
            }
        });

        context = this;

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
        apiClient = retrofit.create(APiInterface.class);


    }

    private void zoomOutMap() {
        if (mMap != null){
            currentZoomLevel -= 1.0f;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));
        }
    }

    private void zoomInMap() {
        if (mMap != null){
            currentZoomLevel+=1.0f;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));
        }
    }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 // Location permission granted, display the user's current location
                 displayCurrentUserLocation();
             }
         }
     }
    private void displayCurrentUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Create a LatLng object with the user's location
                        LatLng userLocation = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, currentZoomLevel));
                    }
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Location permission is granted
            mMap.setMyLocationEnabled(true); // Enable the "My Location" blue dot on the map
            displayCurrentUserLocation();
        } else {
            // Request location permissions here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }


        Call<MapDataResponse> call=apiClient.getMapList();
        call.enqueue(new Callback<MapDataResponse>() {
            @Override
            public void onResponse(Call<MapDataResponse> call, Response<MapDataResponse> response) {

                if(response.isSuccessful()){
                    MapDataResponse mapDataResponse=response.body();

                    if (mapDataResponse.getErrorCode().equals("200")){
                        List<MapDataResponse.Result> list = mapDataResponse.getResult();

                        for (int i = 0; i < list.size(); i++) {
                            String latitudeStr = list.get(i).getLatitude();
                            String longitudeStr = list.get(i).getLongitude();

                            String name=list.get(i).getAnnadhanamNameTelugu();
                            locations = list.get(i).getLocation();

                            // Check if the latitude and longitude strings are not empty before parsing
                            if (!latitudeStr.isEmpty() && !longitudeStr.isEmpty()) {
                                double latitude = Double.parseDouble(latitudeStr);
                                double longitude = Double.parseDouble(longitudeStr);

                                LatLng location = new LatLng(latitude, longitude);
                                String address = getAddressFromLocation(latitude, longitude);



                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(location)

                                        .title(name));
                                marker.setTag(address);


                                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        // Show the info window for the clicked marker
                                        marker.showInfoWindow();
                                        return true; // Return true to consume the click event
                                    }
                                });

                                // Handle the "Start Navigation" button click
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        // Handle the start navigation button click here
                                        // You can use the marker.getTitle() to get the Annadhanam Name
                                        // and launch a navigation intent or perform any other action.
                                        // Example: Start a navigation intent
                                        startNavigation(marker.getPosition());
                                    }
                                });
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
                            }
                        }


                    }

                }

            }

            @Override
            public void onFailure(Call<MapDataResponse> call, Throwable t) {

            }
        });


    }
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressStr = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStr.append(address.getAddressLine(i)).append("\n");
                }
                return addressStr.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not found";
    }
    private void startNavigation(LatLng position) {
        String destinationStr = position.latitude + "," + position.longitude;
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationStr);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Use the Google Maps app

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Handle the case where Google Maps is not installed on the device.
            // You can open a web map or provide an alternative navigation solution.
        }
    }


    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        View mContentsView;

        CustomInfoWindowAdapter() {
            mContentsView = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            TextView title = mContentsView.findViewById(R.id.info_window_title);
            title.setText(marker.getTitle());

            TextView txtLocation =mContentsView.findViewById(R.id.info_location);
            String address = (String) marker.getTag();
            txtLocation.setText(address);

            // Handle the "Start Navigation" button click
            Button startNavigationButton = mContentsView.findViewById(R.id.start_navigation_button);
            startNavigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the "Start Navigation" button click here
                    // You can use the marker.getPosition() to get the destination coordinates
                    // and launch a navigation intent or perform any other action.
                    startNavigation(marker.getPosition());
                }
            });

            return mContentsView;
        }
    }

}