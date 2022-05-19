package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements AdvertisementRVAdapter.ItemClickListener {

    private RecyclerView rvAdvertisement;
    private ArrayList<Advertisement> advertisementArrayList;
    private AdvertisementRVAdapter advertisementRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    BottomNavigationView bottomNav;

    LinearLayout layoutNoData;

    String adsID;

    private TextView tvEmptyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // initializing our variables.
        rvAdvertisement = findViewById(R.id.rvAdvertisement);
        loadingPB = findViewById(R.id.idProgressBar);

        layoutNoData = findViewById(R.id.layoutNoData);

        //declare bottom navigation
        bottomNav = findViewById(R.id.bottomNav);

        //set home selected
        bottomNav.setSelectedItemId(R.id.favorite);

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
                        return true;

                    case R.id.postAds:
                        startActivity(new Intent(getApplicationContext(),PostAdsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        advertisementArrayList = new ArrayList<>();
        rvAdvertisement.setHasFixedSize(true);
        rvAdvertisement.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        advertisementRVAdapter = new AdvertisementRVAdapter(advertisementArrayList, this);

        advertisementRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        rvAdvertisement.setAdapter(advertisementRVAdapter);

        db.collection("favorites")
                .whereEqualTo("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                adsID = document.getData().get("adsID").toString();

                                // below line is use to get the data from Firebase Firestore.
                                // previously we were saving data on a reference of Courses
                                // now we will be getting the data from the same reference.
                                db.collection("advertisements")
                                        .whereEqualTo("adsID", adsID)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                // after getting the data we are calling on success method
                                                // and inside this method we are checking if the received
                                                // query snapshot is empty or not.
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    // if the snapshot is not empty we are
                                                    // hiding our progress bar and adding
                                                    // our data in a list.
                                                    loadingPB.setVisibility(View.GONE);
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        // after getting this list we are passing
                                                        // that list to our object class.
                                                        Advertisement c = d.toObject(Advertisement.class);

                                                        // and we will pass this object class
                                                        // inside our arraylist which we have
                                                        // created for recycler view.
                                                        advertisementArrayList.add(c);
                                                    }
                                                    // after adding the data to recycler view.
                                                    // we are calling recycler view notifuDataSetChanged
                                                    // method to notify that data has been changed in recycler view.
                                                    advertisementRVAdapter.notifyDataSetChanged();
                                                } else {
                                                    // if the snapshot is empty we are displaying a toast message.
                                                    loadingPB.setVisibility(View.GONE);
                                                    layoutNoData.setVisibility(View.VISIBLE);
                                                    //Toast.makeText(TransactionHistoryActivity.this, "You have not made any transactions yet.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // if we do not get any data or any error we are displaying
                                        // a toast message that we do not get any data
                                        Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                        if(task.getResult().size() == 0 ) {
                            //if no ads, display illustration
                            loadingPB.setVisibility(View.GONE);
                            layoutNoData.setVisibility(View.VISIBLE);

                        }
                    }
                });



    }

    @Override
    public void onItemClick(View view, int position) {
        String title = advertisementRVAdapter.getItem(position).getTitle();
        adsID = advertisementRVAdapter.getItem(position).getAdsID();
        Log.e("DisplayAds,  ", "AdsID: " + adsID);
        Toast.makeText(getApplicationContext(),"Title: " + title + " AdsID: " + adsID, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        startActivity(intent);

        toDisplayAds(adsID);
    }

    public void toDisplayAds(String adsID){
        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", "favorite");
        startActivity(intent);
    }

    public void toHome(View v){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void toPostAds(View v){
        Intent intent = new Intent(getApplicationContext(),PostAdsActivity.class);
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