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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminVerificationRequest extends AppCompatActivity implements VerificationRVAdapter.ItemClickListener{

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView rvVerify;
    private ArrayList<User> userArrayList;
    private VerificationRVAdapter verificationRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    TextView tvEmail, tvName, tvPhone, tvAddress, tvEmptyDb, tvDate, tvTime;
    Button btDelete, btBack, btDisable;
    CircleImageView ivProfilePic;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification_request);

        // initializing textview
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        tvEmptyDb = findViewById(R.id.tvEmptyDb);

        // initializing CircleImageView
        ivProfilePic = findViewById(R.id.ivProfilePic);

        // initializing our variables.
        rvVerify = findViewById(R.id.rvVerify);
        loadingPB = findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        userArrayList = new ArrayList<>();
        rvVerify.setHasFixedSize(true);
        rvVerify.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        verificationRVAdapter = new VerificationRVAdapter(userArrayList, this);

        // enable setclicklistener to recyclerview
        verificationRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        rvVerify.setAdapter(verificationRVAdapter);


        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("users")
                .whereEqualTo("UserType", "client")
                .whereEqualTo("Verify", "Pending")
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
                                User c = d.toObject(User.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                userArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            verificationRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            loadingPB.setVisibility(View.GONE);
                            tvEmptyDb.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemClick(View view, int position) {
        uid = verificationRVAdapter.getItem(position).getUid();
        Log.e("Check  ", "Uid: " + uid);
        Toast.makeText(getApplicationContext()," Uid: " + uid, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),AdminVerificationDetail.class);
        intent.putExtra("Uid", uid);
        startActivity(intent);

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