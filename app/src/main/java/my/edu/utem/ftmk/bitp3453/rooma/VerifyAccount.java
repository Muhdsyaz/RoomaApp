package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class VerifyAccount extends AppCompatActivity {

    TextView tvIC, tvSelfie;
    LinearLayout layoutIC, layoutSelfie;
    Button btSubmit;

    ImageView ivIC, ivSelfie;
    Uri icUri, selfieUri;
    String icURL, selfieURL;

    int number;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime;

    Map<String,Object> verify = new HashMap<>();

    String url;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    StorageReference icRef, selfieRef;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        db = FirebaseFirestore.getInstance();

        // declare textview
        tvIC = findViewById(R.id.tvIC);
        tvSelfie = findViewById(R.id.tvSelfie);

        btSubmit = findViewById(R.id.btSubmit);

        // declare layout
        layoutIC = findViewById(R.id.layoutIC);
        layoutSelfie = findViewById(R.id.layoutSelfie);

        ivIC = findViewById(R.id.ivIC);
        ivSelfie = findViewById(R.id.ivSelfie);

        layoutIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = 1;
                boolean pick = true;
                if(pick == true){

                    if(!checkCameraPermission()){
                        requestCameraPermissions();
                    }
                    else
                        PickImage();

                }
                else {

                    if (!checkStoragePermission()) {
                        requestStoragePermissions();
                    } else
                        PickImage();
                }

            }
        });

        layoutSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = 2;
                boolean pick = true;
                if(pick == true){

                    if(!checkCameraPermission()){
                        requestCameraPermissions();
                    }
                    else
                        PickImage();

                }
                else {

                    if (!checkStoragePermission()) {
                        requestStoragePermissions();
                    } else
                        PickImage();
                }

            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistingRef();
            }
        });

    }

    private void PickImage() {
        CropImage.activity().start(this);
    }

    private void requestStoragePermissions() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private void requestCameraPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return  res1 && res2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            if(number == 1){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        ivIC.setImageBitmap(bitmap);
                        icUri = getImageUri(getApplicationContext(),bitmap);

                        tvIC.setText("File uploaded.");
                        tvIC.setTextColor(Color.parseColor("#4CAF50"));


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }

            if(number == 2){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(resultUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        ivSelfie.setImageBitmap(bitmap);
                        selfieUri = getImageUri(getApplicationContext(),bitmap);

                        tvSelfie.setText("File uploaded.");
                        tvSelfie.setTextColor(Color.parseColor("#4CAF50"));


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }


        }
    }

    private void uploadImage() {
        if(icUri == null && selfieUri == null)
        {
            Toast.makeText(getApplicationContext(),"Please upload both required pictures for verification purpose.",Toast.LENGTH_LONG).show();

            tvIC.setText("No file detected.");
            tvIC.setTextColor(Color.parseColor("#D0342C"));

            tvSelfie.setText("No file detected.");
            tvSelfie.setTextColor(Color.parseColor("#D0342C"));
        }
        else if(icUri == null)
        {
            Toast.makeText(getApplicationContext(),"Please upload your Identity Card.",Toast.LENGTH_LONG).show();

            tvIC.setText("No file detected.");
            tvIC.setTextColor(Color.parseColor("#D0342C"));

        }
        else if(selfieUri == null)
        {
            Toast.makeText(getApplicationContext(),"Please upload a selfie with your Identity Card.",Toast.LENGTH_LONG).show();

            tvSelfie.setText("No file detected.");
            tvSelfie.setTextColor(Color.parseColor("#D0342C"));
        }
        else{

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Preparing your request...");
            progressDialog.show();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);

            icRef = FirebaseStorage.getInstance().getReference("ic_images/"+ " identity_card " +fileName);
            selfieRef = FirebaseStorage.getInstance().getReference("selfie_images/"+ " selfie_with_ic " +fileName);

            icRef.putFile(icUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            icRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    icURL = uri.toString();
                                    Log.e("URL ", "onSuccess: " + uri);

                                    selfieRef.putFile(selfieUri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    icRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            selfieURL = uri.toString();
                                                            Log.e("URL ", "onSuccess: " + uri);

                                                            // send data to collection verification
                                                            verifyUser();

                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();


                                        }
                                    });

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();


                }
            });

        }
    }

    //method to convert bitmap to Uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void verifyUser(){

        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(10,16);

        DocumentReference nameRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        nameRef
                .update("IcURL", icURL, "SelfieURL", selfieURL, "Verify", "Pending", "RequestDate", todayDate, "RequestTime", todayTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EditName", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Your account verification has been submitted.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });
    }

    public void checkExistingRef(){


        db.collection("users")
                .whereEqualTo("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if(task.getResult().size() == 0 ) {

                                //if no ads, call method upload image which will store data in database
                                uploadImage();

                            }
                            else{

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());

                                    String Uid = document.getString("Uid");

                                    if(Uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        Log.d("TAG", "User Exists");

                                        checkRequestStatus();

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

    public void checkRequestStatus(){
        db.collection("users")
                .whereEqualTo("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                String status = document.getString("Verify");

                                if(status.equals("Pending")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyAccount.this);

                                    builder.setMessage("However, your account verification request is still \"" + status + "\".");
                                    builder.setTitle("You already have request for account verification.");
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
                                else if(status.equals("Verified")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyAccount.this);

                                    builder.setMessage("You are able to post your advertisements now.");
                                    builder.setTitle("Congratulation! Your account has been verified.");
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
                                else{
                                    uploadImage();
                                }
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void toProfile(View v){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void toProfile(){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}