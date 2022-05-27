package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DisplayAdvertisement extends AppCompatActivity {

    String adsID, favID, activity, reportRef;
    TextView tvTitle, tvMonthlyRent, tvDate, tvCategory, tvLocation, tvResType, tvFloor, tvBedroom, tvBathroom, tvSize, tvFurnishing, tvFacilities, tvYear, tvDeposit, tvOther, tvDescription;
    TextView tvEdit, tvDelete, tvSold, tvBump;

    ImageView ivAdsCover, ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen, ivPhotoLibrary, ivFavorite, ivFavoriteClicked;

    MaterialIconView mvBackBtn;
    Button btToMap, btReport, btDisable;

    LinearLayout layoutAdsPreview, layoutOption;
    HorizontalScrollView layoutHorizontal;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime, status;

    String houseURL, bedroomURL, bathroomURL, livingroomURL, kitchenURL;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_advertisement);

        db = FirebaseFirestore.getInstance();

        Random rand = new Random();
        int randomID = rand.nextInt(99999999)+1;
        int randomID2 = rand.nextInt(99999999)+1;
        favID = "Fav" + randomID;
        reportRef = "Ref" + randomID2;
//        reportRef = "Ref29714482";

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

        tvEdit = findViewById(R.id.tvEdit);
        tvDelete = findViewById(R.id.tvDelete);
        tvSold = findViewById(R.id.tvSold);
        tvBump = findViewById(R.id.tvBump);

        //define mvbutton
        mvBackBtn = findViewById(R.id.mvBackBtn);

        //define button
        btToMap = findViewById(R.id.btToMap);
        btReport = findViewById(R.id.btReport);
        btDisable = findViewById(R.id.btDisable);

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistingRef();
            }
        });

        btToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMap();
            }
        });

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equals("home")){
                    toHome();
                }
                else if(activity.equals("favorite")){
                    toFavorite();
                }
                else if(activity.equals("admin")){
                    toAdminManageAds();
                }
                else if(activity.equals("admin-disabled")){
                    toAdminDisabledAds();
                }
                else{
                    toAdsHistory();
                }
            }
        });


        //define layout
        layoutAdsPreview = findViewById(R.id.layoutAdsPreview);
        layoutHorizontal = findViewById(R.id.layoutHorizontal);
        layoutOption = findViewById(R.id.layoutOption);

        //if the previous activity is from ads history, then enable the layout
        if(activity.equals("liveAds")) {

            btReport.setVisibility(View.INVISIBLE);
            ivFavorite.setVisibility(View.INVISIBLE);
            ivFavoriteClicked.setVisibility(View.INVISIBLE);

            layoutOption.setVisibility(View.VISIBLE);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(DisplayAdvertisement.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Delete Advertisement");
                    dialog.setMessage("Are you sure you want to delete this advertisement? You cannot undone once this action is completed." );
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            deleteAds();

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

            tvSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatus();
                }
            });

            tvBump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bumpAds();
                }
            });
        }

        if(activity.equals("allAds")){
            btReport.setVisibility(View.INVISIBLE);
            ivFavorite.setVisibility(View.INVISIBLE);
            ivFavoriteClicked.setVisibility(View.INVISIBLE);

        }

        if(activity.equals("bumpAds")){
            btReport.setVisibility(View.INVISIBLE);
            ivFavorite.setVisibility(View.INVISIBLE);
            ivFavoriteClicked.setVisibility(View.INVISIBLE);

            layoutOption.setVisibility(View.VISIBLE);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(DisplayAdvertisement.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Delete Advertisement");
                    dialog.setMessage("Are you sure you want to delete this advertisement? You cannot undone once this action is completed." );
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            deleteAds();

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

            tvSold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatus();
                }
            });

            tvBump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unBumpAds();
                }
            });

            tvBump.setText("Unbump");
        }

        if(activity.equals("admin")){

            btDisable.setVisibility(View.VISIBLE);
            btDisable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DisplayAdvertisement.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Disable Advertisement");
                    dialog.setMessage("Disabling this advertisement will remove it from the list of advertisements in the Home page. " +
                            "Are you sure you want to disable this advertisement? " );
                    dialog.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            disableAds();

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

            //hide item
            btReport.setVisibility(View.INVISIBLE);
            ivFavorite.setVisibility(View.INVISIBLE);
            ivFavoriteClicked.setVisibility(View.INVISIBLE);

        }

        if(activity.equals("admin-disabled")){

            btDisable.setVisibility(View.VISIBLE);
            btDisable.setText("Enable Ads");
            btDisable.setBackgroundColor(Color.parseColor("#4CAF50"));

            btDisable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableAds();
                }
            });

            //hide item
            btReport.setVisibility(View.INVISIBLE);
            ivFavorite.setVisibility(View.INVISIBLE);
            ivFavoriteClicked.setVisibility(View.INVISIBLE);

        }

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
//        checkInitial();


    }

    public void displayAdvertisement(){

        DocumentReference docRef = db.collection("advertisements").document(adsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d("DisplayAdvertisementActivity", "DocumentSnapshot data: " + document.getData());

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

                        status = document.getData().get("status").toString();

                        if(!activity.equals("admin") && !activity.equals("admin-disabled")){
                            if(status.equals("disabled")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAdvertisement.this);

                                builder.setMessage("Please contact the admin to enable the advertisement.");
                                builder.setTitle("This advertisement has been disabled.");
                                builder.setCancelable(false);

                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }


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

    public void deleteAds(){

        // delete ads from collection advertisements
        db.collection("advertisements").document(adsID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");

                        Toast.makeText(getApplicationContext(), "The advertisement, id: " + adsID + " successfully deleted.", Toast.LENGTH_LONG).show();

                        toAdsHistory();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });

        // delete all ads that contain the deleted adsID in collection favorite
        db.collection("favorites")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                String favID = document.getData().get("favID").toString();

                                db.collection("favorites").document(favID)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");

//                                                Toast.makeText(getApplicationContext(), "The advertisement, id: " + adsID + " successfully deleted.", Toast.LENGTH_LONG).show();
//
//                                                toAdsHistory();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // delete all ads that contain the deleted adsID in collection reports
        db.collection("reports")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                String reportRef = document.getData().get("reportRef").toString();

                                db.collection("reports").document(reportRef)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");

//                                                Toast.makeText(getApplicationContext(), "The advertisement, id: " + adsID + " successfully deleted.", Toast.LENGTH_LONG).show();
//
//                                                toAdsHistory();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void checkExistingRef(){

        Log.e("checkExistingRef", reportRef);

        db.collection("reports")
                .whereEqualTo("reporterUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.e("Success", reportRef);

                            if(task.getResult().size() == 0 ) {

                                reportAds();

                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());

                                    String report = document.getString("reportRef");

                                    if(report.equals(reportRef)){
                                        Log.d("TAG", "Refno Exists");

                                        Random rand = new Random();
                                        int randomID2 = rand.nextInt(99999999)+1;
                                        reportRef = "Ref" + randomID2;

                                        checkExistingRef();

                                    }
                                    else{
                                        checkExistingReport();
                                    }
                                }
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void checkInitial(){
        db.collection("reports")
                .whereEqualTo("reporterUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAdvertisement.this);

                                builder.setMessage("This ads will be put under our surveillance.");
                                builder.setTitle("You already reported this ads.");
                                builder.setCancelable(false);

                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }

                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void checkExistingReport(){
        db.collection("reports")
                .whereEqualTo("reporterUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().size() == 0 ) {

                                reportAds();

                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAdvertisement.this);

                                builder.setMessage("This ads will be put under our surveillance.");
                                builder.setTitle("You already reported this ads.");
                                builder.setCancelable(false);

                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateStatus(){

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("status", "sold")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Advertisement sold.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error updating document", e);
                    }
                });

    }

    public void reportAds(){

        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(11,16);

        Map<String,Object> report = new HashMap<>();
        report.put("reportRef",reportRef);
        report.put("adsID", adsID);
        report.put("reporterUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        report.put("reportDate", todayDate);
        report.put("reportTime", todayTime);

        db.collection("reports").document(reportRef)
                .set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getApplicationContext(), "This advertisement has been report.", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error updating document", e);
            }
        });
    }

    public void bumpAds(){

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("status", "bump")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Advertisement bumped.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error updating document", e);
                    }
                });

    }

    public void unBumpAds(){

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("status", "live")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Advertisement unbumped.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error updating document", e);
                    }
                });

    }

    public void disableAds(){

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("status", "disabled")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdminManageAdvertisement.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Advertisement disabled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error updating document", e);
                    }
                });

        // delete all ads that contain the deleted adsID in collection reports
        db.collection("reports")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                String reportRef = document.getData().get("reportRef").toString();

                                db.collection("reports").document(reportRef)
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
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void enableAds(){

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("status", "live")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdminDisabledAdvertisement.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Advertisement enabled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Tag", "Error updating document", e);
                    }
                });

    }

    public void toHome(){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toFavorite(){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toAdsHistory(){
        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toMap(){
        Intent intent = new Intent(getApplicationContext(), GoogleMapActivity.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", activity);
        startActivity(intent);
    }

    public void toEditAds(View v){
        Intent intent = new Intent(getApplicationContext(), EditAdvertisement.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", activity);
        startActivity(intent);
    }

    public void toAdminManageAds(){
        Intent intent = new Intent(getApplicationContext(), AdminManageAdvertisement.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toAdminDisabledAds(){
        Intent intent = new Intent(getApplicationContext(), AdminDisabledAdvertisement.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}