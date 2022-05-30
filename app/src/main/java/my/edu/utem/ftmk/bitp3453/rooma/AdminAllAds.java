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

public class AdminAllAds extends AppCompatActivity {

    TextView tvApartment, tvHouse, tvRoom;
    int totApartment, totHouse, totRoom;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_ads);

        db = FirebaseFirestore.getInstance();

        tvApartment = findViewById(R.id.tvApartment);
        tvHouse = findViewById(R.id.tvHouse);
        tvRoom = findViewById(R.id.tvRoom);

        totalAdvertisement();

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

                            tvApartment.setText(String.valueOf(totApartment));
                            tvHouse.setText(String.valueOf(totHouse));
                            tvRoom.setText(String.valueOf(totRoom));

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