package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostAdsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String category;
    Button btContinue;

    BottomNavigationView bottomNav;

    Spinner spinner;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ads);

        db = FirebaseFirestore.getInstance();

        btContinue = findViewById(R.id.btContinue);

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.postAds);

        //perform ItemSelectedListener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.postAds:
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkVerification();

            }
        });

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        category = spinner.getSelectedItem().toString();
    }

    public void checkVerification(){
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String verify = document.getData().get("Verify").toString();

                        if(verify.equals("Pending")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostAdsActivity.this);

                            builder.setMessage("However, your account verification request is still \"" + verify + "\".");
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
                        else if(verify.equals("Verified")){

                            if(category.equals("Category")){
                                Toast.makeText(getApplicationContext(), "Please choose a category before continue.", Toast.LENGTH_SHORT).show();
                            }
                            else if(category.equals("House")){
                                toHouseSpecification();
                                Log.e("House ", "onCreate: " + category);
                            }
                            else if(category.equals("Apartment")){
                                toApartmentSpecification();
                                Log.e("Aparment ", "onCreate: " + category);
                            }
                            else{
                                toRoomSpecification();
                                Log.e("Room ", "onCreate: " + category);
                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostAdsActivity.this);

                            builder.setMessage("Please verify your account to enable you to post advertisements.");
                            builder.setTitle("Your account currently is not verified.");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(PostAdsActivity.this, VerifyAccount.class);
                                    startActivity(intent);

                                }
                            });

                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                        }

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void toHome(View v){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void toFavorite(View v){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
        startActivity(intent);
    }

    public void toProfile(View v){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

    public void toApartmentSpecification(){
        Intent intent = new Intent(getApplicationContext(),ApartmentSpecification.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    public void toHouseSpecification(){
        Intent intent = new Intent(getApplicationContext(),HouseSpecification.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    public void toRoomSpecification(){
        Intent intent = new Intent(getApplicationContext(),RoomSpecification.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        category = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), category, Toast.LENGTH_SHORT).show();
        category = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}