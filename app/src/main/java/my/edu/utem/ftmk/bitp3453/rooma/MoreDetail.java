package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoreDetail extends AppCompatActivity {

    String category;
    MaterialIconView mvBackBtn;
    Bundle bundle;
    Button btContinue;
    Uri houseUri, bedroomUri, bathroomUri, livingroomUri, kitchenUri;
    ImageView ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen;
    LinearLayout layoutAdsCover, layoutBedroom, layoutBathroom, layoutLivingRoom, layoutKitchen;
    int number;

    ProgressDialog progressDialog;
    StorageReference houseRef, bedroomRef, bathroomRef, livingroomRef,kitchenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        bundle = getIntent().getExtras();
        category = bundle.getString("category");
        Log.e("Data ", "MoreDetail: " + bundle);

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
                uploadImage();
            }
        });
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

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}