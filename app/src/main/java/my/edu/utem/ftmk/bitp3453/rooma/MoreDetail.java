package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MoreDetail extends AppCompatActivity {

    String category, bathroom, propertySize, furnishing, parking, bedroom, resType, finishYear, monthlyRent, deposit;
    MaterialIconView mvBackBtn;
    Bundle bundle, facilities, convenience;
    Button btContinue;
    Uri houseUri, bedroomUri, bathroomUri, livingroomUri, kitchenUri;
    ImageView ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen;
    LinearLayout layoutAdsCover, layoutBedroom, layoutBathroom, layoutLivingRoom, layoutKitchen;
    int number;

    Spinner spState, spCity;
    String state, city;
    ArrayList<String> arrayListFacilities = new ArrayList<>();
    ArrayList<String> arrayListConvenience = new ArrayList<>();

    Map<String,String> adsProperty = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;
    StorageReference houseRef, bedroomRef, bathroomRef, livingroomRef,kitchenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        bundle = getIntent().getExtras();
        category = bundle.getString("category");
        bathroom =  bundle.getString("bathroom");
        propertySize =  bundle.getString("propertySize");
        furnishing =  bundle.getString("furnishing");
        parking =  bundle.getString("parking");
        bedroom =  bundle.getString("bedroom");
        resType =  bundle.getString("resType");
        finishYear =  bundle.getString("finishYear");
        monthlyRent =  bundle.getString("monthlyRent");
        deposit =  bundle.getString("deposit");

        arrayListFacilities = bundle.getStringArrayList("facilities");
        arrayListConvenience = bundle.getStringArrayList("convenience");

        Log.e("Bundle ", "MoreDetail: " + bundle);
        Log.e("Category ", "MoreDetail: " + category);
        Log.e("Facilities ", "MoreDetail: " + arrayListFacilities);
        Log.e("Convenience ", "MoreDetail: " + arrayListConvenience);

        adsProperty.put("category",category);
        adsProperty.put("bathroom",bathroom);
        adsProperty.put("propertySize",propertySize);
        adsProperty.put("furnishing",furnishing);
        adsProperty.put("parking",parking);
        adsProperty.put("bedroom",bedroom);
        adsProperty.put("resType",resType);
        adsProperty.put("finishYear",finishYear);
        adsProperty.put("monthlyRent",monthlyRent);
        adsProperty.put("deposit",deposit);
//        adsProperty.put("facilities",arrayListFacilities);


        mvBackBtn = findViewById(R.id.mvBackBtn);
        btContinue = findViewById(R.id.btContinue);

        layoutAdsCover = findViewById(R.id.layoutAdsCover);
        layoutBedroom = findViewById(R.id.layoutBedroom);
        layoutBathroom = findViewById(R.id.layoutBathroom);
        layoutLivingRoom = findViewById(R.id.layoutLivingRoom);
        layoutKitchen = findViewById(R.id.layoutKitchen);

        ivHouse = findViewById(R.id.ivHouse);
        ivBedroom = findViewById(R.id.ivBedroom);
        ivBathroom = findViewById(R.id.ivBathroom);
        ivLivingRoom = findViewById(R.id.ivLivingRoom);
        ivKitchen = findViewById(R.id.ivKitchen);

        Intent intent = getIntent();

        category = intent.getStringExtra("category");

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAvailableFacilities();
            }
        });

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

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //uploadImage();
                postAds();

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
    }

    public void toAvailableFacilities(){
        Intent intent = new Intent(getApplicationContext(),AvailableFacilities.class);
        bundle.remove("Swimming");
        bundle.remove("Gymnasium");
        bundle.remove("Tennis");
        bundle.remove("Squash");
        bundle.remove("Mini");
        bundle.remove("Playground");
        bundle.remove("CarPark");
        bundle.remove("Elevator");
        bundle.remove("Guard");
        bundle.remove("Jogging");
        bundle.remove("Balcony");
        bundle.remove("CableTv");
        bundle.remove("AirCond");
        bundle.remove("Cooking");
        bundle.remove("Gender");
        bundle.remove("PublicTrans");
        bundle.remove("Internet");
        bundle.remove("Washing");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    private void uploadImage() {

        if(houseUri == null || bedroomUri == null || bathroomUri == null || livingroomUri == null || kitchenUri == null)
        {
            Toast.makeText(getApplicationContext(),"Please make sure you have uploaded all the required picture for the ads before submit.",Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading File....");
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

                            Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            houseRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

            bedroomRef.putFile(bedroomUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            bedroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

            bathroomRef.putFile(bathroomUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            bathroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

            livingroomRef.putFile(livingroomUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            livingroomRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

            kitchenRef.putFile(kitchenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                            kitchenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.e("URL ", "onSuccess: " + uri);

                                }
                            });

                            //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();

                }
            });

        }
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

    public void postAds(){

        // Add a new document with a generated ID
        db.collection("advertisements")
                .add(adsProperty)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String text = parent.getItemAtPosition(position).toString();
//        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
//
//        state = spState.getSelectedItem().toString();
//        //city = spCity.getSelectedItem().toString();
//
//        Log.e("State ", "onCreate: " + state);
//        Log.e("City ", "onCreate: " + city);
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}