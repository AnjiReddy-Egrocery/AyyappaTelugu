package com.dst.ayyapatelugu.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.BuildConfig;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dst.ayyapatelugu.Model.TempleMapDataResponse;
import com.dst.ayyapatelugu.R;
import com.dst.ayyapatelugu.Services.APiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class DevlyaluActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    private GoogleMap mMap;
    private APiInterface apiClient;
    private Context context;
    ImageButton zoomInButton, zoomOutButton;
    private float currentZoomLevel = 200.0f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private LatLng userLocation;


    private List<TempleMapDataResponse.Result> temples;

    private double newRadius;
    private static final int PAGE_SIZE = 50;
    private int currentPage = 1;
    private boolean isMapReady = false;
    private boolean isFetchingData = false;
    private static final double INITIAL_RADIUS = 5.00; // 5km
    private double currentRadius = INITIAL_RADIUS;
    private List<Marker> markers = new ArrayList<>();
    private static final long DEBOUNCE_INTERVAL = 500; // milliseconds
    private long lastZoomTime = 0;
    private Handler debounceHandler = new Handler();


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devlyalu);

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
            displayCurrentUserLocation();

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
       /* fetchTempleData();
        initMap();
        enableStrictMode();
        new FetchMarkersTask(currentCameraPosition, currentZoom).execute();*/
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }


    }


    private void initMap() {
        //displayCurrentUserLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_devalayalu_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            currentZoomLevel = 15.0f;  // You can adjust this value based on your requirements
            displayCurrentUserLocation();
            moveCameraToUserLocation();
        } else {

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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 0.5f));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Location permission is granted
            mMap.setMyLocationEnabled(true);

            // Display the user's current location and move the camera to it
            displayCurrentUserLocation();
            moveCameraToUserLocation();

            if (mMap != null) {
                // Set the OnCameraIdleListener only if mMap is not null
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        long currentTime = System.currentTimeMillis();

                        // Apply debounce: wait for a certain interval after the last zoom event
                        if (currentTime - lastZoomTime > DEBOUNCE_INTERVAL) {
                            lastZoomTime = currentTime;

                            // Remove any existing callbacks to avoid multiple calls
                            debounceHandler.removeCallbacksAndMessages(null);

                            // Delay the execution of fetchLocationDataAndAddMarkers by DEBOUNCE_INTERVAL
                            debounceHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Add padding to the visible region
                                    LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                                    LatLngBounds paddedBounds = addPaddingToBounds(bounds, 0.01); // Adjust padding as needed

                                    int numberOfMarkers = fetchLocationDataAndAddMarkers(currentRadius);
                                    Log.e("Ramana", "Number of Markers: " + numberOfMarkers);
                                }
                            }, DEBOUNCE_INTERVAL);
                        }
                    }
                });

                // Fetch and display markers with a fixed radius of 5 km initially
                fetchLocationDataAndAddMarkers(INITIAL_RADIUS);
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

    public interface MarkerCallback {
        void onMarkersLoaded(int numberOfMarkers);
    }

    private LatLngBounds addPaddingToBounds(LatLngBounds bounds, double padding) {
        double north = bounds.northeast.latitude + padding;
        double east = bounds.northeast.longitude + padding;
        double south = bounds.southwest.latitude - padding;
        double west = bounds.southwest.longitude - padding;
        return new LatLngBounds(new LatLng(south, west), new LatLng(north, east));
    }

    private double calculateRadius(LatLngBounds bounds) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.getCenter().latitude, bounds.getCenter().longitude,
                bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0];
    }


    private void displayCurrentUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                        }
                    }
                });
    }

    private void moveCameraToUserLocation() {
        if (userLocation != null && mMap != null) {
            //todo set zoomlevel automatically
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, currentZoomLevel));
          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        }

    }

    private void setupMarkerClickListeners() {
        mMap.setInfoWindowAdapter(new DevlyaluActivity.CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
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

    private int fetchLocationDataAndAddMarkers(double radius) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double userLatitude = location.getLatitude();
                double userLongitude = location.getLongitude();

                // Check if the map is ready
                if (isMapReady) {
                    LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                    double newRadius = calculateRadius(bounds);

                    // Call the backend API to get temple data within the specified radius
                    Call<TempleMapDataResponse> call = apiClient.getTempleMapList();
                    call.enqueue(new Callback<TempleMapDataResponse>() {
                        @Override
                        public void onResponse(Call<TempleMapDataResponse> call, Response<TempleMapDataResponse> response) {
                            if (response.isSuccessful()) {
                                TempleMapDataResponse templeMapDataResponse = response.body();
                                if (templeMapDataResponse != null && templeMapDataResponse.getErrorCode().equals("200")) {
                                    List<TempleMapDataResponse.Result> nearbyTemples = filterTemplesByRadius(templeMapDataResponse.getResult(), userLatitude, userLongitude, radius);


                                    // Clear existing markers
                                    clearMarkers();

                                    // Add new markers
                                    addMarkers(nearbyTemples);
                                   // mMap.animateCamera(1000);
                                    //return nearbyTemples.size();
                                } else {
                                    // Handle error
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TempleMapDataResponse> call, Throwable t) {
                            // Handle failure
                        }
                    });
                }
            }
        });
        return 0;
    }

    private void clearMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for calculating distance between two coordinates
        double R = 1; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    private List<TempleMapDataResponse.Result> filterTemplesByRadius(List<TempleMapDataResponse.Result> temples, double userLatitude, double userLongitude, double radius) {
        List<TempleMapDataResponse.Result> nearbyTemples = new ArrayList<>();
        for (TempleMapDataResponse.Result temple : temples) {
            double templeLatitude = Double.parseDouble(temple.getLatitude());
            double templeLongitude = Double.parseDouble(temple.getLongitude());

            double distance = calculateHaversineDistance(userLatitude, userLongitude, templeLatitude, templeLongitude);



            if (distance <= radius) {
                //Log.e("Ramana","Distance"+distance);
                nearbyTemples.add(temple);
            }
        }
        return nearbyTemples;
    }

    private void addMarkers(List<TempleMapDataResponse.Result> temples) {

        if (temples != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (TempleMapDataResponse.Result location : temples) {
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
                            markers.add(marker);
                        //    markerCount++;

                        }

                    }
                }
            });

        }


    }


    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        View mContentsView;

        CustomInfoWindowAdapter() {
            mContentsView = getLayoutInflater().inflate(R.layout.custom_temple_window, null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            TextView title = mContentsView.findViewById(R.id.window_title);
            title.setText(marker.getTitle());

            TextView txtLocation = mContentsView.findViewById(R.id.window_location);
            String address = (String) marker.getTag();
            txtLocation.setText(address);

            // Handle the "Start Navigation" button click
            Button startNavigationButton = mContentsView.findViewById(R.id.start_navigation_button);
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

            // Calculate a new radius based on the current zoom level
            double newRadius = calculateNewRadius(currentZoomLevel);

            Log.e("Ramana","NewRadius"+newRadius);
            //Log.e("Ramana", "New Zoom Level: " + currentZoomLevel);

            // Fetch location data and add markers based on the new radius
            fetchLocationDataAndAddMarkers(newRadius);
        }
    }
    private void zoomInMap() {
        if (mMap != null) {
            currentZoomLevel += 1.0f;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    // This method will be called when the zoom animation is complete
                    // Delay the execution to ensure the zoom animation finishes completely
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Calculate a new radius based on the current zoom level
                            double newRadius = calculateNewRadius(currentZoomLevel);

                            // Fetch location data and add markers based on the new radius
                            fetchLocationDataAndAddMarkers(newRadius);
                        }
                    }, 100); // Adjust the delay duration as needed
                }

                @Override
                public void onCancel() {
                    // Handle cancellation if needed
                }
            });
        }
    }

    private double calculateNewRadius(float zoomLevel) {
        // Adjust this formula based on your requirements
        return INITIAL_RADIUS / Math.pow(2, zoomLevel - 15);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayCurrentUserLocation();
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }


    protected void onResume() {
        super.onResume();
        // Reload map and markers when the activity is resumed
        if (mMap == null) {
            initMap();
            displayCurrentUserLocation();
        } else {

        }
    }


}