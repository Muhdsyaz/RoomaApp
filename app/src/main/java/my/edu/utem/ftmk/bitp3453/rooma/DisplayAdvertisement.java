package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DisplayAdvertisement extends AppCompatActivity {

    String adsID, favID, activity;
    TextView tvTitle, tvMonthlyRent, tvDate, tvCategory, tvLocation, tvResType, tvFloor, tvBedroom, tvBathroom, tvSize, tvFurnishing, tvFacilities, tvYear, tvDeposit, tvOther, tvDescription;

    ImageView ivAdsCover, ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen, ivPhotoLibrary, ivFavorite, ivFavoriteClicked;

    MaterialIconView mvBackBtn;

    LinearLayout layoutAdsPreview;
    HorizontalScrollView layoutHorizontal;

    String houseURL, bedroomURL, bathroomURL, livingroomURL, kitchenURL;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_advertisement);

        db = FirebaseFirestore.getInstance();

        Random rand = new Random();
        int randomID = rand.nextInt(99999999)+1;
        favID = "fav" + randomID;

        Intent intent = getIntent();

        adsID = intent.getStringExtra("adsID");
        activity = intent.getStringExtra("activity");
        Log.e("DisplayAds,  ", "AdsID: " + adsID + ", From: " + activity);

        //declare imageview
        ivAdsCover = findViewById(R.id.ivAdsCover);
        ivHouse = findViewById(R.id.ivHouse);
        ivBedroom = findViewById(R.id.ivBedroom);
        ivBathroom = findViewById(R.id.ivBathroom);
        ivLivingRoom = findViewById(R.id.ivLivingRoom);
        ivKitchen = findViewById(R.id.ivKitchen);

        ivPhotoLibrary = findViewById(R.id.ivPhotoLibrary);
        ivFavorite = findViewById(R.id.ivFavorite);
        ivFavoriteClicked = findViewById(R.id.ivFavoriteClicked);

        //declare textview
        tvTitle = findViewById(R.id.tvTitle);
        tvMonthlyRent = findViewById(R.id.tvMonthlyRent);
        tvDate = findViewById(R.id.tvDate);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        tvResType = findViewById(R.id.tvResType);
        tvFloor = findViewById(R.id.tvFloor);
        tvBedroom = findViewById(R.id.tvBedroom);
        tvBathroom = findViewById(R.id.tvBathroom);
        tvSize = findViewById(R.id.tvSize);
        tvFurnishing = findViewById(R.id.tvFurnishing);
        tvFacilities = findViewById(R.id.tvFacilities);
        tvYear = findViewById(R.id.tvYear);
        tvDeposit = findViewById(R.id.tvDeposit);
        tvOther = findViewById(R.id.tvOther);
        tvDescription = findViewById(R.id.tvDescription);

        //define mvbutton
        mvBackBtn = findViewById(R.id.mvBackBtn);

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equals("home")){
                    toHome();
                }
                else if(activity.equals("favorite")){
                    toFavorite();
                }
                else if(activity.equals("liveAds")){
                    toAdsHistory();
                }
                else{
                    toAdsHistory();
                }
            }
        });

        //define layout
        layoutAdsPreview = findViewById(R.id.layoutAdsPreview);
        layoutHorizontal = findViewById(R.id.layoutHorizontal);

        //if image house is clicked, then show layouthorizontal
        ivPhotoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHorizontal.setVisibility(View.VISIBLE);
            }
        });

        layoutAdsPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHorizontal.setVisibility(View.INVISIBLE);
            }
        });

        layoutHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        checkFavorite();

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFavoriteClicked.setVisibility(View.VISIBLE);
                favoriteAds();
//                checkExistingAds();

                ivFavoriteClicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivFavoriteClicked.setVisibility(View.INVISIBLE);
                        uncheckFavorite();
                    }
                });
            }
        });


        displayAdvertisement();


    }

    public void displayAdvertisement(){

        DocumentReference docRef = db.collection("advertisements").document(adsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("DisplayAdvertisementActivity", "DocumentSnapshot data: " + document.getData());

                        houseURL = document.getData().get("houseURL").toString();
                        bedroomURL = document.getData().get("bedroomURL").toString();
                        bathroomURL = document.getData().get("bathroomURL").toString();
                        livingroomURL = document.getData().get("livingroomURL").toString();
                        kitchenURL = document.getData().get("kitchenURL").toString();

                        // assign value to image view
                        Picasso.with(getApplicationContext()).load(houseURL).into(ivAdsCover);
                        Picasso.with(getApplicationContext()).load(houseURL).into(ivHouse);
                        Picasso.with(getApplicationContext()).load(bedroomURL).into(ivBedroom);
                        Picasso.with(getApplicationContext()).load(bathroomURL).into(ivBathroom);
                        Picasso.with(getApplicationContext()).load(livingroomURL).into(ivLivingRoom);
                        Picasso.with(getApplicationContext()).load(kitchenURL).into(ivKitchen);

                        //assign value to textview
                        tvTitle.setText(document.getData().get("title").toString());
                        tvMonthlyRent.setText("RM " + document.getData().get("monthlyRent").toString());
                        tvDate.setText(document.getData().get("postDate").toString() + " " + document.getData().get("postTime").toString());
                        tvCategory.setText(document.getData().get("category").toString());
                        tvLocation.setText(document.getData().get("state").toString() + " > " + document.getData().get("city").toString());
                        tvResType.setText(document.getData().get("resType").toString());

                        if(document.getData().get("floorRange") == null){
                            tvFloor.setText("");
                        }
                        else{
                            tvFloor.setText(document.getData().get("floorRange").toString());
                        }

                        if(document.getData().get("bedroom") == null){
                            tvBedroom.setText("");
                        }
                        else{
                            tvBedroom.setText(document.getData().get("bedroom").toString());
                        }

                        if(document.getData().get("bathroom") == null){
                            tvBathroom.setText("");
                        }
                        else{
                            tvBathroom.setText(document.getData().get("bathroom").toString());
                        }

                        tvSize.setText(document.getData().get("propertySize").toString() + " sq.ft.");
                        tvFurnishing.setText(document.getData().get("furnishing").toString());
                        tvFacilities.setText(document.getData().get("facilities").toString());
                        tvYear.setText(document.getData().get("finishYear").toString());
                        tvDeposit.setText("RM " + document.getData().get("deposit").toString());
                        tvOther.setText(document.getData().get("convenience").toString());
                        tvDescription.setText(document.getData().get("description").toString());

                    } else {
                        Log.d("DisplayAdvertisementActivity", "No such document");
                    }
                } else {
                    Log.d("DisplayAdvertisementActivity", "get failed with ", task.getException());
                }
            }
        });

    }

    public void checkFavorite(){

        db.collection("favorites")
                .whereEqualTo("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                favID = document.getData().get("favID").toString();

                                ivFavoriteClicked.setVisibility(View.VISIBLE);

                                ivFavoriteClicked.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ivFavoriteClicked.setVisibility(View.INVISIBLE);

                                        db.collection("favorites").document(favID)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "Error deleting document", e);
                                                    }
                                                });
                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void checkExistingAds(){

        db.collection("favorites")
                .whereEqualTo("favID", favID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                String ads = document.getString("favID");

                                if(ads.equals(favID)){
                                    checkExistingAds();
                                    Log.d("TAG", "This ads is already favorite by this user.");

                                }
                            }
                        }
                        if(task.getResult().size() == 0 ) {
                            //if no ads, call method upload image which will store data in database
                            favoriteAds();

                        }
                    }
                });
    }

    public void favoriteAds(){
        // Add a new document with a generated id.
        Map<String, Object> favorite = new HashMap<>();
        favorite.put("favID", favID);
        favorite.put("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        favorite.put("adsID", adsID);
        favorite.put("favorite", "true");


        db.collection("favorites").document(favID)
                .set(favorite).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "Advertisement has been favorited!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error updating document", e);
            }
        });

    }

    public void uncheckFavorite(){
        db.collection("favorites").document(favID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });

    }

    public void toHome(){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void toFavorite(){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
        startActivity(intent);
    }

    public void toAdsHistory(){
        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
        startActivity(intent);
    }

    public void toLiveAds(){
        Intent intent = new Intent(getApplicationContext(),LiveAdsFragment.class);
        startActivity(intent);
    }

    public void toAllAds(){
        Intent intent = new Intent(getApplicationContext(),AllAdsFragment.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}