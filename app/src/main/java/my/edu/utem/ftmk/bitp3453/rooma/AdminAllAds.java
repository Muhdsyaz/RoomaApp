package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class AdminAllAds extends AppCompatActivity {

    TextView tvApartment, tvHouse, tvRoom, tvPercentageApartment, tvPercentageHouse, tvPercentageRoom, tvActiveUser, tvDisabledUser, tvPercentageVerified;
    int totApartment, totHouse, totRoom, totActive, totDisabled, totVerified;
    double totalAds, percentApartment, percentHouse, percentRoom, totalUser, percentVerified;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_ads);

        db = FirebaseFirestore.getInstance();

        tvApartment = findViewById(R.id.tvApartment);
        tvHouse = findViewById(R.id.tvHouse);
        tvRoom = findViewById(R.id.tvRoom);
        tvActiveUser = findViewById(R.id.tvActiveUser);
        tvDisabledUser = findViewById(R.id.tvDisabledUser);

        tvPercentageApartment = findViewById(R.id.tvPercentageApartment);
        tvPercentageHouse = findViewById(R.id.tvPercentageHouse);
        tvPercentageRoom = findViewById(R.id.tvPercentageRoom);
        tvPercentageVerified = findViewById(R.id.tvPercentageVerified);

        totalAdvertisement();
        totalUser();

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

                                String category = document.getData().get("category").toString();

                                if(category.equals("Apartment")){
                                    totApartment++;
                                }

                                if(category.equals("House")){
                                    totHouse++;
                                }

                                if(category.equals("Room")){
                                    totRoom++;
                                }

                            }

                            totalAds = task.getResult().size();

                            DecimalFormat df = new DecimalFormat("0.00");

                            percentApartment = (totApartment/totalAds) * 100;
                            percentHouse = (totHouse/totalAds) * 100;
                            percentRoom = (totRoom/totalAds) * 100;

                            tvApartment.setText(String.valueOf(totApartment));
                            tvHouse.setText(String.valueOf(totHouse));
                            tvRoom.setText(String.valueOf(totRoom));
                            tvPercentageApartment.setText(df.format(percentApartment) + "%");
                            tvPercentageHouse.setText(df.format(percentHouse) + "%");
                            tvPercentageRoom.setText(df.format(percentRoom) + "%");

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void totalUser(){


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("TAG", document.getId() + " => " + document.getData());

                                String status = document.getData().get("Status").toString();
                                String verify = document.getData().get("Verify").toString();

                                if(status.equals("active")){
                                    totActive++;
                                }

                                if(status.equals("disabled")){
                                    totDisabled++;
                                }

                                if(verify.equals("Verified")){
                                    totVerified++;
                                }

                            }

                            totalUser = task.getResult().size();

                            DecimalFormat df = new DecimalFormat("0.00");
                            percentVerified = (totVerified/totalUser) * 100;

                            tvActiveUser.setText(String.valueOf(totActive));
                            tvDisabledUser.setText(String.valueOf(totDisabled));
                            tvPercentageVerified.setText(df.format(percentVerified) + "%");

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void toAdminMenu(View v){
        Intent intent = new Intent(getApplicationContext(), AdminMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}