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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.AyyappaTempleMapDataResponse;
import com.dst.ayyapatelugu.Model.MapDataResponse;
import com.dst.ayyapatelugu.Model.TempleMapDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AyyaappaDevlyaluActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    private GoogleMap mMap;
    private APiInterface apiClient;
    ImageButton zoomInButton, zoomOutButton;

    private float currentZoomLevel = 15.0f;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private Context context;
    private LatLng userLocation;

    List<AyyappaTempleMapDataResponse.Result> ayyappatemples;

    private static final double INITIAL_RADIUS = 5000; // 5km
    private double currentRadius = INITIAL_RADIUS;

    private Handler handler = new Handler();
    private Runnable fetchMarkersRunnable;
    private boolean isMapReady = false;

    private static final float THRESHOLD_ZOOM_LEVEL = 1.0f;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayyaappa_devlyalu);

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
            initMap();
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

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_ayyappadevlyalu_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            // Handle the case when mapFragment is null
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        isMapReady = true;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set the map type (e.g., normal, hybrid, satellite)
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Enable the compass
        mMap.getUiSettings().setCompassEnabled(true);

        // Enable the my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Customize the info window adapter if needed
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Location permission is granted
            mMap.setMyLocationEnabled(true);
            displayCurrentUserLocation();

            if (mMap != null) {
                // Set the OnCameraIdleListener only if mMap is not null
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        float currentZoomLevel = mMap.getCameraPosition().zoom;
                        Log.e("Reddy", "Current Zoom Level: " + currentZoomLevel);
                        if (currentZoomLevel >= THRESHOLD_ZOOM_LEVEL) {
                            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                            Log.e("Reddy", "Bounds: " + bounds.toString());
                            currentRadius = calculateRadius(bounds);
                            Log.e("Reddy", "Current Radius: " + currentRadius);
                            fetchLocationDataAndAddMarkers(currentRadius);

                        }else {
                            mMap.clear();
                        }

                    }
                });
                if (isMapReady) {
                    fetchLocationDataAndAddMarkers(currentRadius);
                }
            } else {
                Log.e("MapFragment", "GoogleMap object is null");
            }
        } else {
            // Request location permissions here
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        setupMarkerClickListeners();
    }

    private double calculateRadius(LatLngBounds bounds) {
        float[] result = new float[1];
        Log.e("Reddy", "Bounds: " + result);
        Location.distanceBetween(bounds.getCenter().latitude, bounds.getCenter().longitude,
                bounds.northeast.latitude, bounds.northeast.longitude, result);
        Log.e("Reddy", "Bounds: " + result);
        return result[0];
    }


    private void displayCurrentUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        userLocation = new LatLng(latitude, longitude);
                        if (mMap != null) {
                            moveCameraToUserLocation();
                        } else {
                            // Handle the case when mMap is null
                            initMap();
                        }

                    }
                });
    }

    private void moveCameraToUserLocation() {
        if (userLocation != null && mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, currentZoomLevel));
        }
    }

    private void setupMarkerClickListeners() {
        mMap.setInfoWindowAdapter(new AyyaappaDevlyaluActivity.CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                zoomInOnMarker(marker);
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startNavigation(marker.getPosition());
            }
        });
    }

    private void zoomInOnMarker(Marker marker) {
        LatLng markerPosition = marker.getPosition();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerPosition)
                .zoom(17.0f)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void fetchLocationDataAndAddMarkers(double radius) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double currentLatitude = location.getLatitude();
                        double currentLongitude = location.getLongitude();
                        if (isMapReady) {
                            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                            double newRadius = calculateRadius(bounds);
                            if (currentZoomLevel >= THRESHOLD_ZOOM_LEVEL) {
                                // Show markers
                                addMarkers(ayyappatemples);
                            } else {
                                // Hide markers
                                mMap.clear();
                            }
                            Call<AyyappaTempleMapDataResponse> call = apiClient.getAyyaooaTempleMapList();
                            call.enqueue(new Callback<AyyappaTempleMapDataResponse>() {
                                @Override
                                public void onResponse(Call<AyyappaTempleMapDataResponse> call, Response<AyyappaTempleMapDataResponse> response) {
                                    if (response.isSuccessful()) {
                                        AyyappaTempleMapDataResponse ayyappaTempleMapDataResponse = response.body();
                                        if (ayyappaTempleMapDataResponse != null && ayyappaTempleMapDataResponse.getErrorCode().equals("200")) {
                                            ayyappatemples = ayyappaTempleMapDataResponse.getResult();
                                            List<AyyappaTempleMapDataResponse.Result> nearbyTemples = filterTemplesByRadius(ayyappatemples, currentLatitude, currentLongitude, newRadius);
                                            mMap.clear();
                                            ayyappatemples = nearbyTemples;
                                            addMarkers(ayyappatemples);
                                        } else {
                                            Log.e("API Response", "Invalid response: " + response.code());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AyyappaTempleMapDataResponse> call, Throwable t) {
                                    Log.e("API Response", "Error fetching data: " + t.getMessage());
                                }
                            });
                        }
                    }
                });
    }



    private List<AyyappaTempleMapDataResponse.Result> filterTemplesByRadius(List<AyyappaTempleMapDataResponse.Result> ayyappatemples, double currentLatitude, double currentLongitude, double radius) {
        List<AyyappaTempleMapDataResponse.Result> nearbyTemples = new ArrayList<>();
        for (AyyappaTempleMapDataResponse.Result temple : ayyappatemples) {
            double templeLatitude = Double.parseDouble(temple.getLatitude());
            double templeLongitude = Double.parseDouble(temple.getLongitude());

            float[] distance = new float[1];
            Location.distanceBetween(currentLatitude, currentLongitude, templeLatitude, templeLongitude, distance);

            if (distance[0] <= radius) {
                nearbyTemples.add(temple);

            }
        }
        return nearbyTemples;
    }


    private void addMarkers(List<AyyappaTempleMapDataResponse.Result> ayyappatemples) {
        if (ayyappatemples != null && mMap != null) {
            for (AyyappaTempleMapDataResponse.Result location : ayyappatemples) {
                double latitude = Double.parseDouble(location.getLatitude());
                double longitude = Double.parseDouble(location.getLongitude());
                String name = location.getTempleNameTelugu();
                String locationAddress = getAddressFromLocation(latitude, longitude);

                if (!locationAddress.equals("Address not found")) {
                    LatLng locationLatLng = new LatLng(latitude, longitude);

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(locationLatLng)
                            .title(name)
                    );

                    marker.setTag(locationAddress);
                }
            }
        }
    }



    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        View mContentsView;

        CustomInfoWindowAdapter() {
            mContentsView = getLayoutInflater().inflate(R.layout.custom_ayyappa_window, null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            TextView title = mContentsView.findViewById(R.id.ayyappa_window_title);
            title.setText(marker.getTitle());

            TextView txtLocation = mContentsView.findViewById(R.id.ayyappa_location);
            String address = (String) marker.getTag();
            txtLocation.setText(address);

            // Handle the "Start Navigation" button click
            Button startNavigationButton = mContentsView.findViewById(R.id.start_navigation);
            startNavigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavigation(marker.getPosition());
                }
            });
            return mContentsView;
        }
    }

    private void startNavigation(LatLng position) {
        String destinationStr = position.latitude + "," + position.longitude;
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationStr);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Use the Google Maps app

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No navigation app installed. Please install a navigation app.", Toast.LENGTH_SHORT).show();
        }
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

    private void zoomOutMap() {
        if (mMap != null) {
            currentZoomLevel -= 1.0f;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));

        }
    }

    private void zoomInMap() {
        if (mMap != null) {
            currentZoomLevel += 1.0f;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayCurrentUserLocation();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Reload map and markers when the activity is resumed
        if (mMap == null) {
            initMap();
        } else {
            // Check if location permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Location permission is granted
                mMap.setMyLocationEnabled(true);
                displayCurrentUserLocation();
            } else {
                // Request location permissions here
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

}