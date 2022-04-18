package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout layoutProfile, layoutProfileMenu;
    TextView tvEmail, tvName, tvPhone, tvAddress, tvLiveAds, tvEdit, tvLogout;
    MaterialIconView mvProfileMenu;
    CircleImageView ivProfilePic;

    String url;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();

        //set variable for textview
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvLiveAds = findViewById(R.id.tvLiveAds);
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
                signOut();
            }
        });

        displayProfile();
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

                        url = document.getData().get("Picture URL").toString();
                        new ProfileActivity.FetchImage(url).start();

                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            }
        });

    }

    class FetchImage extends Thread{

        String URL;
        Bitmap bitmap;

        FetchImage(String URL){

            this.URL = URL;

        }

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

//                    progressDialog = new ProgressDialog(ProfileActivity.this);
//                    progressDialog.setMessage("Getting your pic....");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                }
            });

            InputStream inputStream = null;
            try {
                inputStream = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }



            mainHandler.post(new Runnable() {
                @Override
                public void run() {

//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
                    ivProfilePic.setImageBitmap(bitmap);

                }
            });




        }
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