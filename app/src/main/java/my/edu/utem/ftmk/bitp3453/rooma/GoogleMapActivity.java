package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.IOException;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    boolean isPermissionGranted;
    GoogleMap mGoogleMap;
    FloatingActionButton fab;
    private FusedLocationProviderClient mLocationClient;

    private int GPS_REQUEST_CODE = 9001;

    LinearLayout layoutBack, layoutDetail;
    Button btBack;

    TextView tvAddress, tvCategory, tvSize, tvBedroom, tvBathroom;

    String adsID, activity;

    double latitude, longitude;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        adsID = intent.getStringExtra("adsID");
        activity = intent.getStringExtra("activity");
        Log.e("GoogleMap,  ", "AdsID: " + adsID + ", From: " + activity);

        tvAddress = findViewById(R.id.tvAddress);
        tvCategory = findViewById(R.id.tvCategory);
        tvSize = findViewById(R.id.tvSize);
        tvBedroom = findViewById(R.id.tvBedroom);
        tvBathroom = findViewById(R.id.tvBathroom);


        layoutBack = findViewById(R.id.layoutBack);
        layoutDetail = findViewById(R.id.layoutDetail);

        btBack = findViewById(R.id.btBack);

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDisplayAds();
            }
        });

        fab = findViewById(R.id.fab);

        checkMyPermission();

        initMap();

        displayAdsDetail();

        mLocationClient = new FusedLocationProviderClient(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrLoc();
            }
        });
    }

    private void initMap(){
        if(isPermissionGranted){
            if(isGPSEnable()) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                supportMapFragment.getMapAsync(this);
            }
        }
    }

    private boolean isGPSEnable(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnable){
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("GPS is required for this app to work. Please enable GPS")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void getCurrLoc(){
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());

            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng Latlng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(Latlng, 18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


    private void checkMyPermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(GoogleMapActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();;
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);

        addMarker();

        db.collection("advertisements")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                GeoPoint geoPoint = document.getGeoPoint("latlng");
                                LatLng latLng = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());

                                latitude = geoPoint.getLatitude();
                                longitude = geoPoint.getLongitude();

                                CameraUpdate point = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                                Log.e("GoogleMap,  ", "Lat: " + latitude + ", Long: " + longitude);

                                // moves camera to coordinates
                                mGoogleMap.moveCamera(point);

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addMarker(){

        db.collection("advertisements")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());


                                GeoPoint geoPoint = document.getGeoPoint("latlng");
                                LatLng latLng = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());

                                latitude = geoPoint.getLatitude();
                                longitude = geoPoint.getLongitude();

                                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));

                                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {

                                        layoutDetail.setVisibility(View.VISIBLE);
                                        btBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                layoutDetail.setVisibility(View.INVISIBLE);
                                            }
                                        });

                                        return false;
                                    }
                                });


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GPS_REQUEST_CODE){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(providerEnable){
                Toast.makeText(this,"GPS is enable", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"GPS is not enable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void displayAdsDetail(){

        DocumentReference docRef = db.collection("advertisements").document(adsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        tvAddress.setText(document.getData().get("address").toString());
                        tvCategory.setText(document.getData().get("category").toString());
                        tvSize.setText(document.getData().get("propertySize").toString() + " sq.ft");

                        if(document.getData().get("category").toString().equals("Room")){
                            tvBedroom.setText("1 Bedroom");
                            tvBathroom.setText("1 Bathroom");
                        }
                        else {
                            tvBedroom.setText(document.getData().get("bedroom").toString() + " Bedroom");
                            tvBathroom.setText(document.getData().get("bathroom").toString() + " Bathroom");
                        }

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


    }

    public void toDisplayAds(){
        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}