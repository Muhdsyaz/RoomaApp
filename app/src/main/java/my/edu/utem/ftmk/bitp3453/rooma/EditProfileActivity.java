package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView ivProfilePic;
    String url;

    TextView tvEmail, tvName, tvPhone, tvAddress, tvPassword;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    StorageReference storageReference;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();

        //set variable for textview
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvPassword = findViewById(R.id.tvPassword);

        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        tvName.setText(document.getData().get("FullName").toString());
                        tvPhone.setText(document.getData().get("PhoneNum").toString());
                        tvAddress.setText(document.getData().get("Address").toString());
                        tvPassword.setText(document.getData().get("Password").toString());

                        url = document.getData().get("Picture URL").toString();
                        new EditProfileActivity.FetchImage(url).start();

                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
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
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivProfilePic.setImageBitmap(bitmap);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                    Date now = new Date();
                    String fileName = formatter.format(now);
                    storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

                    //upload the photo uploaded from camera to storage
                    storageReference.putFile(getImageUri(getApplicationContext(),bitmap))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    ivProfilePic.setImageURI(null);
                                    Toast.makeText(EditProfileActivity.this,"Successfully Uploaded" + storageReference.getDownloadUrl(),Toast.LENGTH_SHORT).show();

                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Log.e("URL ", "onSuccess: " + uri);
                                            url = uri.toString();
                                            updatePictureUrl();
                                        }
                                    });

                                    //Log.e("URL ", "onSuccess: " + storageReference.getDownloadUrl());
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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

    //method to convert bitmap to Uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void updatePictureUrl(){

        DocumentReference nameRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        nameRef
                .update("Picture URL", url)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EditName", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                        startActivity(intent);

                        Toast.makeText(EditProfileActivity.this, "Name updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });
    }


    public void toEditName(View v){
        Intent intent = new Intent(getApplicationContext(),EditName.class);
        intent.putExtra("name",tvName.getText().toString());
        startActivity(intent);
    }

    public void toEditPhone(View v){
        Intent intent = new Intent(getApplicationContext(),EditPhone.class);
        intent.putExtra("phone",tvPhone.getText().toString());
        startActivity(intent);
    }

    public void toEditAddress(View v){
        Intent intent = new Intent(getApplicationContext(),EditAddress.class);
        intent.putExtra("address",tvAddress.getText().toString());
        startActivity(intent);
    }

    public void toEditPassword(View v){
        Intent intent = new Intent(getApplicationContext(),EditPassword.class);
        intent.putExtra("password",tvPassword.getText().toString());
        startActivity(intent);
    }

    public void toProfile(View v){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}