package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView tvEdit, tvDelete, tvSold, tvBump, tvEmail, tvEmail2, tvName, tvName2, tvPhone, tvAddress, tvRegDate, tvFalse, tvScam, tvOtherReport;

    EditText etReportDetail;

    ImageView ivAdsCover, ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen, ivPhotoLibrary, ivFavorite, ivFavoriteClicked, ivProfilePic, ivProfilePic2;

    MaterialIconView mvBackBtn, mvBackBtn2;
    Button btToMap, btReport, btReport2, btDisable, btBack, btBack2, btSubmit;

    CardView layoutContact;

    LinearLayout layoutAdsPreview, layoutOption, layoutOwnerInfo, layoutReport, layoutOther;
    HorizontalScrollView layoutHorizontal;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime, status, ownerUid, url;

    String houseURL, bedroomURL, bathroomURL, livingroomURL, kitchenURL;

    String reportMessage;

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
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivProfilePic2 = findViewById(R.id.ivProfilePic2);

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

        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);

        tvEmail2 = findViewById(R.id.tvEmail2);
        tvName2 = findViewById(R.id.tvName2);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvRegDate = findViewById(R.id.tvRegDate);

        tvFalse = findViewById(R.id.tvFalse);
        tvScam = findViewById(R.id.tvScam);
        tvOtherReport = findViewById(R.id.tvOtherReport);

        etReportDetail = findViewById(R.id.etReportDetail);

        //define mvbutton
        mvBackBtn = findViewById(R.id.mvBackBtn);
        mvBackBtn2 = findViewById(R.id.mvBackBtn2);

        //define button
        btToMap = findViewById(R.id.btToMap);
        btReport = findViewById(R.id.btReport);
        btDisable = findViewById(R.id.btDisable);
        btBack = findViewById(R.id.btBack);
        btReport2 = findViewById(R.id.btReport2);
        btBack2 = findViewById(R.id.btBack2);
        btSubmit = findViewById(R.id.btSubmit);

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutReport.setVisibility(View.VISIBLE);

                tvFalse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tvFalse.setTextColor(Color.parseColor("#F4717F"));
                        tvFalse.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        tvScam.setTextColor(Color.parseColor("#FFFFFF"));
                        tvScam.setBackgroundColor(Color.parseColor("#F4717F"));

                        reportMessage = tvFalse.getText().toString();

                        Log.e("Log", "Report message: " + reportMessage);

                    }
                });

                tvScam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tvScam.setTextColor(Color.parseColor("#F4717F"));
                        tvScam.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        tvFalse.setTextColor(Color.parseColor("#FFFFFF"));
                        tvFalse.setBackgroundColor(Color.parseColor("#F4717F"));

                        reportMessage = "ggggggggggg";

                        Log.e("Log", "Report message: " + reportMessage);

                    }
                });

                tvOtherReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutReport.setVisibility(View.INVISIBLE);
                        layoutOther.setVisibility(View.VISIBLE);

                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                reportMessage = etReportDetail.getText().toString();
                                Log.e("Log", "Report message: " + reportMessage);

                                if(reportMessage.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Please enter a brief explaination so that we can figure out what's wrong with this advertisement.", Toast.LENGTH_LONG).show();
                                }
                                else if(reportMessage.length() < 5){
                                    Toast.makeText(getApplicationContext(), "Reason input by user too short.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    checkExistingRef();
                                    etReportDetail.getText().clear();
                                    layoutOther.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                        mvBackBtn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layoutReport.setVisibility(View.VISIBLE);
                                layoutOther.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                });

                btBack2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutReport.setVisibility(View.INVISIBLE);

                        tvScam.setTextColor(Color.parseColor("#FFFFFF"));
                        tvScam.setBackgroundColor(Color.parseColor("#F4717F"));

                        tvFalse.setTextColor(Color.parseColor("#FFFFFF"));
                        tvFalse.setBackgroundColor(Color.parseColor("#F4717F"));

                        reportMessage = "";

                        Log.e("Log", "Report message: " + reportMessage);

                    }
                });

                btReport2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(reportMessage == null){
                            Toast.makeText(getApplicationContext(), "Please select a reason why are you reporting or explain what wrong with the advertisement.", Toast.LENGTH_LONG).show();
                        }
                        else if(reportMessage.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Please select a reason why are you reporting or explain what wrong with the advertisement.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            checkExistingRef();

                            tvScam.setTextColor(Color.parseColor("#FFFFFF"));
                            tvScam.setBackgroundColor(Color.parseColor("#F4717F"));

                            tvFalse.setTextColor(Color.parseColor("#FFFFFF"));
                            tvFalse.setBackgroundColor(Color.parseColor("#F4717F"));

                            reportMessage = "";

                            layoutReport.setVisibility(View.INVISIBLE);

                        }
                    }
                });

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
        layoutOwnerInfo = findViewById(R.id.layoutOwnerInfo);

        layoutContact = findViewById(R.id.layoutContact);
        layoutReport = findViewById(R.id.layoutReport);
        layoutOther = findViewById(R.id.layoutOther);

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

        layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOwnerInfo.setVisibility(View.VISIBLE);

                btBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutOwnerInfo.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });


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


                                layoutOption.setVisibility(View.INVISIBLE);
                            }
                        }

                        ownerUid = document.getData().get("ownerUid").toString();
                        displayOwner(ownerUid);

                        //assign value to textview
                        tvTitle.setText(document.getData().get("title").toString());
                        tvMonthlyRent.setText("RM " + document.getData().get("monthlyRent").toString());

                        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        date = new Date();
                        todayDate = (formatter.format(date)).substring(0,10);
                        todayTime = (formatter.format(date)).substring(11,16);

                        String dbdate = document.getData().get("postDate").toString();
                        String dbtime = document.getData().get("postTime").toString();

                        if(todayDate.equals(dbdate)){
                            dbdate = "Today";
                            tvDate.setText(dbdate + " " + dbtime);
                        }
                        else {
                            tvDate.setText(dbdate + " " + dbtime);
                        }

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

    public void displayOwner(String ownerUid){
        DocumentReference docRef = db.collection("users").document(ownerUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d("ProfileActivity", "DocumentSnapshot data: " + document.getData());

                        tvEmail.setText(document.getData().get("Email").toString());
                        tvName.setText(document.getData().get("FullName").toString());

                        tvEmail2.setText(document.getData().get("Email").toString());
                        tvName2.setText(document.getData().get("FullName").toString());
                        tvPhone.setText(document.getData().get("PhoneNum").toString());
                        tvAddress.setText(document.getData().get("Address").toString());
                        tvRegDate.setText(document.getData().get("RegisterDate").toString());

                        url = document.getData().get("PictureURL").toString();

                        if(url == ""){

                        }else{
                            Picasso.with(DisplayAdvertisement.this).load(url).into(ivProfilePic);
                            Picasso.with(DisplayAdvertisement.this).load(url).into(ivProfilePic2);
                        }

                        String phone = tvPhone.getText().toString();
                        openWhatsapp(phone);

                        String email = tvEmail2.getText().toString();
                        openEmail(email);

                        SpannableString content = new SpannableString(phone);
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        tvPhone.setText(content);

                        SpannableString content2 = new SpannableString(email);
                        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
                        tvEmail2.setText(content2);

                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
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

        Log.e("Log", "Report message: " + reportMessage);

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

        Log.e("Log", "Report message: " + reportMessage);

        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(11,16);

        Map<String,Object> report = new HashMap<>();
        report.put("reportRef",reportRef);
        report.put("adsID", adsID);
        report.put("reason", reportMessage);
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

    public void openWhatsapp(String phone){

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://api.whatsapp.com/send?phone=" + phone;
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void openEmail(String email){

        tvEmail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + email + "?");
                intent.setData(data);
                startActivity(intent);
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