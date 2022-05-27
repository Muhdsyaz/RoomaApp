package my.edu.utem.ftmk.bitp3453.rooma;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LiveAdsFragment extends Fragment implements AdvertisementRVAdapter.ItemClickListener{

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView rvAdvertisement;
    private ArrayList<Advertisement> advertisementArrayList;
    private AdvertisementRVAdapter advertisementRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private TextView tvEmptyDb;

    String adsID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_all_ads, container, false);

        //Assign variable
        tvEmptyDb = view.findViewById(R.id.tvEmptyDb);

        // initializing our variables.
        rvAdvertisement = view.findViewById(R.id.rvAdvertisement);
        loadingPB = view.findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        advertisementArrayList = new ArrayList<>();
        rvAdvertisement.setHasFixedSize(true);
        rvAdvertisement.setLayoutManager(new LinearLayoutManager(getContext()));

        // adding our array list to our recycler view adapter class.
        advertisementRVAdapter = new AdvertisementRVAdapter(advertisementArrayList, getContext());

        advertisementRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        rvAdvertisement.setAdapter(advertisementRVAdapter);

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("advertisements")
                .whereEqualTo("ownerUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("status","live")
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
                            tvEmptyDb.setVisibility(View.VISIBLE);
                            //Toast.makeText(TransactionHistoryActivity.this, "You have not made any transactions yet.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                //Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

    @Override
    public void onItemClick(View view, int position) {
        String title = advertisementRVAdapter.getItem(position).getTitle();
        adsID = advertisementRVAdapter.getItem(position).getAdsID();
        Log.e("DisplayAds,  ", "AdsID: " + adsID);
        Toast.makeText(getContext(),"Title: " + title + " AdsID: " + adsID, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        startActivity(intent);

        toDisplayAds(adsID);
    }

    public void toDisplayAds(String adsID){
        Intent intent = new Intent(getContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        intent.putExtra("activity", "liveAds");
        startActivity(intent);
    }
}