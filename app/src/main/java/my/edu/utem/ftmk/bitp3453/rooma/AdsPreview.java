package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdsPreview extends AppCompatActivity {

    Bundle bundle;
    Uri houseUri, bedroomUri, bathroomUri, livingroomUri, kitchenUri;

    ImageView ivAdsCover, ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen;
    LinearLayout layoutAdsPreview;
    HorizontalScrollView layoutHorizontal;
    TextView tvTitle, tvMonthlyRent, tvDate, tvCategory, tvLocation, tvResType, tvFloor, tvBedroom, tvBathroom, tvSize, tvFurnishing, tvFacilities, tvYear, tvDeposit, tvOther, tvDescription;
    String title, monthlyRent, category, location, resType, floor, bedroom, bathroom, size, furnishing, parking, facilities, year, deposit, other, description;
    String adsID, ownerUid, state, city, address;

    ArrayList<String> arrayListFacilities;
    ArrayList<String> arrayListConvenience;

    StorageReference houseRef, bedroomRef, bathroomRef, livingroomRef,kitchenRef;
    String houseURL, bedroomURL, bathroomURL, livingroomURL, kitchenURL;

    MaterialIconView mvBackBtn;
    Button btSubmit;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime;

    ProgressDialog progressDialog;

    private double latitude, longitude;
    private Geocoder geocoder;
    List<Address> fullAddress;
    GeoPoint geoPoint;

    Map<String,Object> adsProperty = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_preview);

        geocoder = new Geocoder(this, Locale.getDefault());

        //getting the data bundle from other activity incoming
        bundle = getIntent().getExtras();

        //assigning uri value from previous activity
        houseUri = Uri.parse(bundle.getString("houseUri"));
        bedroomUri = Uri.parse(bundle.getString("bedroomUri"));
        bathroomUri = Uri.parse(bundle.getString("bathroomUri"));
        livingroomUri = Uri.parse(bundle.getString("livingroomUri"));
        kitchenUri = Uri.parse(bundle.getString("kitchenUri"));

        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(10,16);

        ownerUid = bundle.getString("ownerUid");
        adsID = bundle.getString("adsID");

        title = bundle.getString("title");
        monthlyRent = bundle.getString("monthlyRent");
        category = bundle.getString("category");
        state = bundle.getString("state");
        city = bundle.getString("city");
        address = bundle.getString("address");
        resType = bundle.getString("resType");
        floor = bundle.getString("floorRange");
        bedroom = bundle.getString("bedroom");
        bathroom = bundle.getString("bathroom");
        size = bundle.getString("propertySize");
        furnishing = bundle.getString("furnishing");
        parking = bundle.getString("parking");
        year = bundle.getString("finishYear");
        deposit = bundle.getString("deposit");
        description = bundle.getString("description");

        arrayListFacilities = new ArrayList<>();
        arrayListConvenience = new ArrayList<>();

        arrayListFacilities = bundle.getStringArrayList("facilities");
        arrayListConvenience = bundle.getStringArrayList("convenience");

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

        //assign value to textview
        tvTitle.setText(title);
        tvMonthlyRent.setText("RM " + monthlyRent);
        tvDate.setText(todayDate + " " + todayTime);
        tvCategory.setText(category);
        tvLocation.setText(state + " > " + city);
        tvResType.setText(resType);
        tvFloor.setText(floor);
        tvBedroom.setText(bedroom);
        tvBathroom.setText(bathroom);
        tvSize.setText(size);
        tvFurnishing.setText(furnishing);
        tvFacilities.setText(arrayListFacilities.toString());
        tvYear.setText(year);
        tvDeposit.setText("RM " + deposit);
        tvOther.setText(arrayListConvenience.toString());
        tvDescription.setText(description);

        //declare imageview
        ivAdsCover = findViewById(R.id.ivAdsCover);
        ivHouse = findViewById(R.id.ivHouse);
        ivBedroom = findViewById(R.id.ivBedroom);
        ivBathroom = findViewById(R.id.ivBathroom);
        ivLivingRoom = findViewById(R.id.ivLivingRoom);
        ivKitchen = findViewById(R.id.ivKitchen);

        //set uri to imageview
        ivAdsCover.setImageURI(houseUri);
        ivHouse.setImageURI(houseUri);
        ivBedroom.setImageURI(bedroomUri);
        ivBathroom.setImageURI(bathroomUri);
        ivLivingRoom.setImageURI(livingroomUri);
        ivKitchen.setImageURI(kitchenUri);

        //define button
        mvBackBtn = findViewById(R.id.mvBackBtn);
        btSubmit = findViewById(R.id.btSubmit);

        //define layout
        layoutAdsPreview = findViewById(R.id.layoutAdsPreview);
        layoutHorizontal = findViewById(R.id.layoutHorizontal);

        //if image house is clicked, then show layouthorizontal
        ivAdsCover.setOnClickListener(new View.OnClickListener() {
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

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMoreDetail();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistingAds();
            }
        });

    }

    private void uploadImage() {

        if(houseUri == null || bedroomUri == null || bathroomUri == null || livingroomUri == null || kitchenUri == null)
        {
            Toast.makeText(getApplicationContext(),"Please make sure you have uploaded all the required picture for the ads before submit.",Toast.LENGTH_SHORT).show();
        }
        else{

//            progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading File....");
//            progressDialog.show();

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Preparing your advertisement...");
            progressDialog.show();


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);
            houseRef = FirebaseStorage.getInstance().getReference("images/"+"house_"+fileName);
            bedroomRef = FirebaseStorage.getInstance().getReference("images/"+"bedroom_"+fileName);
            bathroomRef = FirebaseStorage.getInstance().getReference("images/"+"bathroom_"+fileName);
            livingroomRef = FirebaseStorage.getInstance().getReference("images/"+"livingroom_"+fileName);
            kitchenRef = FirebaseStorage.getInstance().getReference("images/"+"kitchen_"+fileName);


            houseRef.putFile(houseUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            houseRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    houseURL = uri.toString();
                                    Log.e("URL ", "onSuccess: " + uri);

                                    bedroomRef.putFile(bedroomUri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    //Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                                                    bedroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            bedroomURL= uri.toString();
                                                            Log.e("URL ", "onSuccess: " + uri);

                                                            bathroomRef.putFile(bathroomUri)
                                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                            //Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                                                                            bathroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri) {

                                                                                    bathroomURL = uri.toString();
                                                                                    Log.e("URL ", "onSuccess: " + uri);

                                                                                    livingroomRef.putFile(livingroomUri)
                                                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                @Override
                                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                                                    //Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                                                                                                    livingroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Uri uri) {

                                                                                                            livingroomURL = uri.toString();
                                                                                                            Log.e("URL ", "onSuccess: " + uri);

                                                                                                            kitchenRef.putFile(kitchenUri)
                                                                                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                                                                            //Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                                                                                                                            kitchenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Uri uri) {

                                                                                                                                    kitchenURL = uri.toString();
                                                                                                                                    Log.e("URL ", "onSuccess: " + uri);

                                                                                                                                    postAds();

                                                                                                                                }
                                                                                                                            });

                                                                                                                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                                                                                                                            if (progressDialog.isShowing())
//                                                                                                                                progressDialog.dismiss();

                                                                                                                        }
                                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {


//                                                                                                                    if (progressDialog.isShowing())
//                                                                                                                        progressDialog.dismiss();
                                                                                                                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                                                                                                                }
                                                                                                            });

                                                                                                        }
                                                                                                    });

                                                                                                    //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                                                                                                    if (progressDialog.isShowing())
//                                                                                                        progressDialog.dismiss();

                                                                                                }
                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {


//                                                                                            if (progressDialog.isShowing())
//                                                                                                progressDialog.dismiss();
                                                                                            Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    });

                                                                                }
                                                                            });

                                                                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                                                                            if (progressDialog.isShowing())
//                                                                                progressDialog.dismiss();

                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {


//                                                                    if (progressDialog.isShowing())
//                                                                        progressDialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                        }
                                                    });

                                                    //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                                                    if (progressDialog.isShowing())
//                                                        progressDialog.dismiss();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {


//                                            if (progressDialog.isShowing())
//                                                progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    public void postAds(){

        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(10,16);

        adsProperty.put("ownerUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        adsProperty.put("adsID", adsID);
        adsProperty.put("postDate", todayDate);
        adsProperty.put("postTime", todayTime);

        adsProperty.put("category",category);
        adsProperty.put("bathroom",bathroom);
        adsProperty.put("propertySize",size);
        adsProperty.put("furnishing",furnishing);
        adsProperty.put("parking",parking);
        adsProperty.put("bedroom",bedroom);
        adsProperty.put("resType",resType);
        adsProperty.put("finishYear",year);
        adsProperty.put("monthlyRent",monthlyRent);
        adsProperty.put("deposit",deposit);

        adsProperty.put("facilities", arrayListFacilities.toString());
        adsProperty.put("convenience", arrayListConvenience.toString());

        adsProperty.put("title", title);
        adsProperty.put("description", description);
        adsProperty.put("state",state);
        adsProperty.put("city",city);
        adsProperty.put("address",address);

        try {

            fullAddress = geocoder.getFromLocationName(adsProperty.get("address").toString(), 5);

            Address location = fullAddress.get(0);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

            //Toast.makeText(getApplicationContext(), "latitude: " + latitude +", longitude: " + longitude, Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
        }

        adsProperty.put("latlng", geoPoint);

        adsProperty.put("houseURL", houseURL);
        adsProperty.put("bedroomURL", bedroomURL);
        adsProperty.put("bathroomURL", bathroomURL);
        adsProperty.put("livingroomURL", livingroomURL);
        adsProperty.put("kitchenURL", kitchenURL);

        db.collection("advertisements").document(adsID)
                .set(adsProperty).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Your advertisement has been uploaded.",Toast.LENGTH_LONG).show();
                toPostAds();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to upload your advertisement.",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void checkExistingAds(){

        db.collection("users")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                String ads = document.getString("adsID");

                                if(ads.equals(adsID)){
                                    Log.d("TAG", "Ads Exists");

                                }
                            }
                        }
                        if(task.getResult().size() == 0 ) {
                            //if no ads, call method upload image which will store data in database
                            uploadImage();

                        }
                    }
                });
    }

    public void toPostAds(){
        Intent intent = new Intent(getApplicationContext(),PostAdsActivity.class);
        startActivity(intent);
    }

    public void toMoreDetail(){

        bundle.remove("houseUri");
        bundle.remove("bedroomUri");
        bundle.remove("bathroomUri");
        bundle.remove("livingroomUri");
        bundle.remove("kitchenUri");

        Intent intent = new Intent(getApplicationContext(),MoreDetail.class);
        intent.putExtras(bundle);
        Log.e("Bundle ", "onCreate: " + bundle);
        startActivity(intent);
    }
}