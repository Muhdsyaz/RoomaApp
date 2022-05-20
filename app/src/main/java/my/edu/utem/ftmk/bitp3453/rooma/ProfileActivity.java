package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout layoutProfile, layoutProfileMenu;
    TextView tvEmail, tvName, tvPhone, tvAddress, tvAds, tvEdit, tvLogout;
    MaterialIconView mvProfileMenu;
    CircleImageView ivProfilePic;

    String url;

    BottomNavigationView bottomNav;

    int total = 0;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.profile);

        //perform ItemSelectedListener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.postAds:
                        startActivity(new Intent(getApplicationContext(),PostAdsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        //set variable for textview
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvAds = findViewById(R.id.tvAds);
        tvEdit = findViewById(R.id.tvEdit);
        tvLogout = findViewById(R.id.tvLogout);

        //declare ImageView
        ivProfilePic = findViewById(R.id.ivProfilePic);

        //set variable for layout
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutProfileMenu = findViewById(R.id.layoutProfileMenu);

        //set variable for material icon
        mvProfileMenu = findViewById(R.id.mvProfileMenu);

        mvProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.VISIBLE);
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.INVISIBLE);
            }
        });

        layoutProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Logout Confirmation");
                dialog.setMessage("Are you sure you want to logout?" );
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Logout".
                        signOut();

                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".

                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));


            }
        });

        displayProfile();
        numberLiveAds();
    }

    public void displayProfile(){

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ProfileActivity", "DocumentSnapshot data: " + document.getData());

                        tvEmail.setText(document.getData().get("Email").toString());
                        tvName.setText(document.getData().get("FullName").toString());
                        tvPhone.setText(document.getData().get("PhoneNum").toString());
                        tvAddress.setText(document.getData().get("Address").toString());

                        url = document.getData().get("PictureURL").toString();
//                        new ProfileActivity.FetchImage(url).start();

                        if(url == ""){

                        }else{
                            Picasso.with(ProfileActivity.this).load(url).into(ivProfilePic);
                        }


                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });

    }

    public void numberLiveAds(){


        db.collection("advertisements")
                .whereEqualTo("ownerUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("status", "live")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                total++;

                            }

                            tvAds.setText(String.valueOf(total));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void toHome(View v){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void toFavorite(View v){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
        startActivity(intent);
    }

    public void toPostAds(View v){
        Intent intent = new Intent(getApplicationContext(),PostAdsActivity.class);
        startActivity(intent);
    }

    public void toEditProfile(View v){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        startActivity(intent);
    }

    public void toAdsHistory(View v){
        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
        startActivity(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}