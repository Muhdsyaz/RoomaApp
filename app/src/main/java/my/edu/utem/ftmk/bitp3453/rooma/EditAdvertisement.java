package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditAdvertisement extends AppCompatActivity {

    MaterialIconView mvBackBtn;
    Button btSubmit;

    Uri houseUri, bedroomUri, bathroomUri, livingroomUri, kitchenUri;
    String houseURL, bedroomURL, bathroomURL, livingroomURL, kitchenURL;
    String state, city;

    String adsID, activity, title, description, address, address1, address2, postCode, statedb, citydb;
    int number;

    ImageView ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen;
    LinearLayout layoutAdsCover, layoutBedroom, layoutBathroom, layoutLivingRoom, layoutKitchen;
    EditText etAdsTitle, etDescription, etAddress1, etAddress2, etPostCode;
    TextView tvState, tvCity;

    ProgressDialog progressDialog;
    StorageReference houseRef, bedroomRef, bathroomRef, livingroomRef,kitchenRef;

    Spinner spState, spCity;

    private double latitude, longitude;
    private Geocoder geocoder;
    List<Address> fullAddress;
    GeoPoint geoPoint;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advertisement);

        geocoder = new Geocoder(this, Locale.getDefault());

        Intent intent = getIntent();

        adsID = intent.getStringExtra("adsID");
        activity = intent.getStringExtra("activity");
        Log.e("GoogleMap,  ", "AdsID: " + adsID + ", From: " + activity);


        //declare text view
        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);

        //declare edit text
        etAdsTitle = findViewById(R.id.etAdsTitle);
        etDescription = findViewById(R.id.etDescription);
        etAddress1 = findViewById(R.id.etAddress1);
        etAddress2 = findViewById(R.id.etAddress2);
        etPostCode = findViewById(R.id.etPostCode);

        //declare button
        mvBackBtn = findViewById(R.id.mvBackBtn);
        btSubmit = findViewById(R.id.btSubmit);

        //declare layout
        layoutAdsCover = findViewById(R.id.layoutAdsCover);
        layoutBedroom = findViewById(R.id.layoutBedroom);
        layoutBathroom = findViewById(R.id.layoutBathroom);
        layoutLivingRoom = findViewById(R.id.layoutLivingRoom);
        layoutKitchen = findViewById(R.id.layoutKitchen);

        //declare imageview
        ivHouse = findViewById(R.id.ivHouse);
        ivBedroom = findViewById(R.id.ivBedroom);
        ivBathroom = findViewById(R.id.ivBathroom);
        ivLivingRoom = findViewById(R.id.ivLivingRoom);
        ivKitchen = findViewById(R.id.ivKitchen);

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDisplayAds();
            }
        });

        // <---- Spinner City and State

        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);

        MalaysiaStateAndCity malaysiaStateCity = new MalaysiaStateAndCity();
        ArrayList<ArrayList<String>> stateAndCity = malaysiaStateCity.getStateAndCity();

        final ArrayAdapter<String> adapterStateCity = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stateAndCity.get(0)){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterStateCity.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spState.setAdapter(adapterStateCity);
        spState.setAdapter(adapterStateCity);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter adapterCity = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, stateAndCity.get(i)) {

                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);

                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spCity.setAdapter(adapterCity);

                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if (i > 0) {
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    state = spState.getSelectedItem().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    String selectedcity = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected city : " + selectedcity, Toast.LENGTH_SHORT)
                            .show();

                    city = spCity.getSelectedItem().toString();

                    Log.e("State ", "onCreate: " + state);
                    Log.e("City ", "onCreate: " + city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // -----> Spinner City and State

        displayAdvertisement();

        layoutAdsCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 1;
                selectImage();
            }
        });

        layoutBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 2;
                selectImage();
            }
        });

        layoutBathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 3;
                selectImage();
            }
        });

        layoutLivingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 4;
                selectImage();
            }
        });

        layoutKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 5;
                selectImage();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                //updateAdsDetail();
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
                        Log.d("DisplayAdvertisementActivity", "DocumentSnapshot data: " + document.getData());

                        houseURL = document.getData().get("houseURL").toString();
                        bedroomURL = document.getData().get("bedroomURL").toString();
                        bathroomURL = document.getData().get("bathroomURL").toString();
                        livingroomURL = document.getData().get("livingroomURL").toString();
                        kitchenURL = document.getData().get("kitchenURL").toString();

                        // assign value to image view
                        Picasso.with(getApplicationContext()).load(houseURL).into(ivHouse);
                        Picasso.with(getApplicationContext()).load(bedroomURL).into(ivBedroom);
                        Picasso.with(getApplicationContext()).load(bathroomURL).into(ivBathroom);
                        Picasso.with(getApplicationContext()).load(livingroomURL).into(ivLivingRoom);
                        Picasso.with(getApplicationContext()).load(kitchenURL).into(ivKitchen);

                        //assign value to textview
                        tvState.setText(document.getData().get("state").toString());
                        tvCity.setText(document.getData().get("city").toString());

                        //assign value to edittext
                        etAdsTitle.setText(document.getData().get("title").toString());
                        etDescription.setText(document.getData().get("description").toString());
                        etAddress1.setText(document.getData().get("address1").toString());
                        etAddress2.setText(document.getData().get("address2").toString());
                        etPostCode.setText(document.getData().get("postCode").toString());



                    } else {
                        Log.d("DisplayAdvertisementActivity", "No such document");
                    }
                } else {
                    Log.d("DisplayAdvertisementActivity", "get failed with ", task.getException());
                }
            }
        });

    }

    private void uploadImage() {


        if(etAdsTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() ||
                etAddress1.getText().toString().isEmpty() || etAddress2.getText().toString().isEmpty() || etPostCode.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please make sure you fill all the form.",Toast.LENGTH_SHORT).show();
        }
        else if(houseUri == null || bedroomUri == null || bathroomUri == null || livingroomUri == null || kitchenUri == null){
            updateAdsDetail();
        }
        else{

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

                                                                                                                                    updateAdsDetail();

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


    public void updateAdsDetail(){

        title = etAdsTitle.getText().toString();
        description = etDescription.getText().toString();
        address1 = etAddress1.getText().toString();
        address2 = etAddress2.getText().toString();
        postCode = etPostCode.getText().toString();
        statedb = tvState.getText().toString();
        citydb = tvCity.getText().toString();

        if(state == null || city == null){

            state = statedb;
            city = citydb;

            address = address1 + " " + address2 + " " + city + " " + postCode + " " + state;
        }
        else{
            address = address1 + " " + address2 + " " + city + " " + postCode + " " + state;
        }

        try {

            fullAddress = geocoder.getFromLocationName(address, 5);

            Address location = fullAddress.get(0);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

        } catch (Exception e){
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Preparing your advertisement...");
        progressDialog.show();

        DocumentReference nameRef = db.collection("advertisements").document(adsID);

        nameRef
                .update("title", title, "description", description, "address", address, "address1", address1, "address2",
                        address2, "postCode",postCode, "latlng", geoPoint, "state", state, "city", city,
                        "houseURL", houseURL, "bedroomURL", bedroomURL, "bathroomURL", bathroomURL, "livingroomURL", livingroomURL, "kitchenURL", kitchenURL)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EditAdvertisement", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
                        startActivity(intent);

                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Advertisement details updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.w("EditName", "Error updating document", e);
                        Toast.makeText(getApplicationContext(), "Advertisement update failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            if(number == 1){
                houseUri = data.getData();
                ivHouse.setImageURI(houseUri);

            }

            if(number == 2){
                bedroomUri = data.getData();
                ivBedroom.setImageURI(bedroomUri);

            }

            if(number == 3){
                bathroomUri = data.getData();
                ivBathroom.setImageURI(bathroomUri);

            }

            if(number == 4){
                livingroomUri = data.getData();
                ivLivingRoom.setImageURI(livingroomUri);

            }

            if(number == 5){
                kitchenUri = data.getData();
                ivKitchen.setImageURI(kitchenUri);

            }
        }
    }

    public void toDisplayAds(){
        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", activity);
        startActivity(intent);
    }
}