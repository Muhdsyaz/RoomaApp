package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminMenu extends AppCompatActivity {

    Button btLogout;
    TextView tvRequest, tvTotalAds;
    int request, totalAds;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        db = FirebaseFirestore.getInstance();

        // declare button
        btLogout = findViewById(R.id.btLogout);

        // declare textview
        tvRequest = findViewById(R.id.tvRequest);
        tvTotalAds = findViewById(R.id.tvTotalAds);

        totalRequest();
        totalAdvertisement();

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminMenu.this);
                dialog.setCancelable(false);
                dialog.setTitle("Logout Confirmation");
                dialog.setMessage("Are you sure you want to logout?" );
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        signOut();
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

    }

    public void totalRequest(){


        db.collection("users")
                .whereEqualTo("UserType", "client")
                .whereEqualTo("Verify", "Pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TAG", document.getId() + " => " + document.getData());

                                request = task.getResult().size();

                            }

                            tvRequest.setText(String.valueOf(request));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void totalAdvertisement(){


        db.collection("advertisements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TAG", document.getId() + " => " + document.getData());

                                totalAds = task.getResult().size();

                            }

                            tvTotalAds.setText(String.valueOf(totalAds));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void toAdminVerificationRequest(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminVerificationRequest.class);
        startActivity(intent);
    }

    public void toAdminAllAds(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminAllAds.class);
        startActivity(intent);
    }


    public void toAdminManageAdvertisement(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminManageAdvertisement.class);
        startActivity(intent);
    }

    public void toAdminDisabledAdvertisement(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminDisabledAdvertisement.class);
        startActivity(intent);
    }

    public void toAdminManageUser(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminManageUser.class);
        startActivity(intent);
    }

    public void toAdminDisabledUser(View v) {
        Intent intent = new Intent(getApplicationContext(), AdminDisabledUser.class);
        startActivity(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}