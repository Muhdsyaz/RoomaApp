package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminVerificationDetail extends AppCompatActivity {

    private FirebaseFirestore db;

    TextView tvEmail, tvName, tvPhone, tvDate, tvTime;
    Button btReject, btAccept;
    CircleImageView ivProfilePic;
    ImageView ivIC, ivSelfie;

    String uid, icURL, selfieURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification_detail);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        uid = intent.getStringExtra("Uid");

        // initializing textview
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        // initializing imageview
        ivIC = findViewById(R.id.ivIC);
        ivSelfie = findViewById(R.id.ivSelfie);

        // initializing button
        btReject = findViewById(R.id.btReject);
        btAccept = findViewById(R.id.btAccept);

        readUser();

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptUserRequest();
            }
        });

        btReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectUserRequest();
            }
        });
    }

    public void readUser() {

        db.collection("users")
                .whereEqualTo("Uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                icURL = document.getData().get("IcURL").toString();
                                selfieURL = document.getData().get("SelfieURL").toString();

                                Picasso.with(getApplicationContext()).load(icURL).into(ivIC);
                                Picasso.with(getApplicationContext()).load(selfieURL).into(ivSelfie);


                                tvEmail.setText("Email: " + document.getData().get("Email").toString());
                                tvName.setText("Full Name: " +document.getData().get("FullName").toString());
                                tvPhone.setText("Phone Number: " +document.getData().get("PhoneNum").toString());
                                tvDate.setText("Request Date: " +document.getData().get("RequestDate").toString());
                                tvTime.setText("Request Time: " +document.getData().get("RequestTime").toString());

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void acceptUserRequest(){

            DocumentReference docRef = db.collection("users").document(uid);

            docRef
                    .update("Verify", "Verified")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent intent = new Intent(getApplicationContext(), AdminVerificationRequest.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "The account has been verified.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditName", "Error updating document", e);
                        }
                    });

    }

    public void rejectUserRequest(){

        DocumentReference docRef = db.collection("users").document(uid);

        docRef
                .update("Verify", "Rejected")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(getApplicationContext(), AdminVerificationRequest.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "The account verification request has been rejected.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EditName", "Error updating document", e);
                    }
                });

    }

    public void toAdminVerificationRequest(View v){
        Intent intent = new Intent(getApplicationContext(), AdminVerificationRequest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}