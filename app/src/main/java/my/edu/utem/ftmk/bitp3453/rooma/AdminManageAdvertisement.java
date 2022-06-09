package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

public class AdminManageAdvertisement extends AppCompatActivity implements ReportedRVAdapter.ItemClickListener{

    private RecyclerView rvDisabled;
    private ArrayList<Advertisement> advertisementArrayList;
    private ReportedRVAdapter reportedRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private TextView tvEmptyDb, tvReportRef, tvTitle, tvAdsID, tvOwnerName, tvDate, tvTime, tvReportDate, tvReportTime, tvReason;

    Button btRemove, btView;
    MaterialIconView mvBackBtn2;

    LinearLayout layoutReportInfo;
    String adsID, reportRef, ownerID, ownerName, date, time, reportDate, reportTime , reason, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_advertisement);

        btRemove = findViewById(R.id.btRemove);
        btView = findViewById(R.id.btView);
        mvBackBtn2 = findViewById(R.id.mvBackBtn2);

        layoutReportInfo = findViewById(R.id.layoutReportInfo);

        // initializing text view
        tvReportRef = findViewById(R.id.tvReportRef);
        tvTitle = findViewById(R.id.tvTitle);
        tvAdsID = findViewById(R.id.tvAdsID);
        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvReportDate = findViewById(R.id.tvReportDate);
        tvReportTime = findViewById(R.id.tvReportTime);
        tvReason = findViewById(R.id.tvReason);

        tvEmptyDb = findViewById(R.id.tvEmptyDb);

        // initializing our variables.
        rvDisabled = findViewById(R.id.rvDisabled);
        loadingPB = findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        advertisementArrayList = new ArrayList<>();
        rvDisabled.setHasFixedSize(true);
        rvDisabled.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        reportedRVAdapter = new ReportedRVAdapter(advertisementArrayList, this);

        reportedRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        rvDisabled.setAdapter(reportedRVAdapter);

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("reports")
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
                            reportedRVAdapter.notifyDataSetChanged();
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
                Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

        mvBackBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutReportInfo.setVisibility(View.INVISIBLE);
            }
        });

        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
                intent.putExtra("adsID", adsID);
                intent.putExtra("activity", "admin");
                startActivity(intent);
            }
        });

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromList();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        String title = reportedRVAdapter.getItem(position).getTitle();
        adsID = reportedRVAdapter.getItem(position).getAdsID();
        reportRef = reportedRVAdapter.getItem(position).getReportRef();
        Log.e("DisplayAds,  ", "AdsID: " + adsID);
        Toast.makeText(getApplicationContext()," AdsID: " + adsID, Toast.LENGTH_SHORT).show();

        layoutReportInfo.setVisibility(View.VISIBLE);

        displayReport();

    }

    public void displayReport(){
        db.collection("reports")
                .whereEqualTo("adsID", adsID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                reportDate = document.getData().get("reportDate").toString();
                                reportTime = document.getData().get("reportTime").toString();
                                reason = document.getData().get("reason").toString();

                                tvReportRef.setText("Report Ref: " + reportRef);
                                tvAdsID.setText("Ads ID: " +adsID);
                                tvReportDate.setText("Report Date: " +reportDate);
                                tvReportTime.setText("Report Time: " +reportTime);
                                tvReason.setText("Reason: " +reason);

                                db.collection("advertisements")
                                        .whereEqualTo("adsID", adsID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        ownerID = document.getData().get("ownerUid").toString();
                                                        title = document.getData().get("title").toString();
                                                        date = document.getData().get("postDate").toString();
                                                        time = document.getData().get("postTime").toString();

                                                        tvTitle.setText("Title: " + title);
                                                        tvDate.setText("Post Date: " + date);
                                                        tvTime.setText("Post Time: " +time);

                                                        db.collection("users")
                                                                .whereEqualTo("Uid", ownerID)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                ownerName = document.getData().get("FullName").toString();
                                                                                tvOwnerName.setText("Posted by: " + ownerName);

                                                                            }
                                                                        } else {
                                                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                } else {
                                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void removeFromList(){

        db.collection("reports").document(reportRef)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");

                        Intent intent = new Intent(getApplicationContext(), AdminManageAdvertisement.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    public void toAdminMenu(View v){
        Intent intent = new Intent(getApplicationContext(), AdminMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}