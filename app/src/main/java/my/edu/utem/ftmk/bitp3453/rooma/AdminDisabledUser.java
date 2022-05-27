package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminDisabledUser extends AppCompatActivity implements  UserRVAdapter.ItemClickListener{

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView rvUser;
    private ArrayList<User> userArrayList;
    private UserRVAdapter userRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    TextView tvEmail, tvName, tvPhone, tvAddress, tvEmptyDb, tvSearch;
    Button btDelete, btBack, btEnable;
    CircleImageView ivProfilePic;
    ImageButton ibSearch;

    LinearLayout layoutUserInfo;

    String uid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_disabled_user);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        Log.e("onCreate ", " Name: " + name);

        // initializing textview
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);

        tvSearch= findViewById(R.id.tvSearch);
        tvEmptyDb = findViewById(R.id.tvEmptyDb);

        // initializing button
        btDelete = findViewById(R.id.btDelete);
        btBack = findViewById(R.id.btBack);
        btEnable = findViewById(R.id.btEnable);

        ibSearch = findViewById(R.id.ibSearch);

        // initializing layout
        layoutUserInfo = findViewById(R.id.layoutUserInfo);

        // initializing CircleImageView
        ivProfilePic = findViewById(R.id.ivProfilePic);

        // initializing our variables.
        rvUser = findViewById(R.id.rvUser);
        loadingPB = findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // creating our new array list
        userArrayList = new ArrayList<>();
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        userRVAdapter = new UserRVAdapter(userArrayList, this);

        // enable setclicklistener to recyclerview
        userRVAdapter.setClickListener(this);

        // setting adapter to our recycler view.
        rvUser.setAdapter(userRVAdapter);

        if(name == null || name.isEmpty()) {
            // below line is use to get the data from Firebase Firestore.
            // previously we were saving data on a reference of Courses
            // now we will be getting the data from the same reference.
            db.collection("users")
                    .whereEqualTo("UserType", "client")
                    .whereEqualTo("Status", "disabled")
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
                                userRVAdapter.notifyDataSetChanged();
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
        else{
            db.collection("users")
                    .whereEqualTo("UserType", "client")
                    .whereEqualTo("Status", "disabled")
                    .whereEqualTo("FullName", name)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                loadingPB.setVisibility(View.GONE);
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    User c = d.toObject(User.class);
                                    userArrayList.add(c);
                                }

                                userRVAdapter.notifyDataSetChanged();
                            } else {

                                loadingPB.setVisibility(View.GONE);
                                tvEmptyDb.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;

                float position = layoutUserInfo.getY();

                layoutUserInfo.animate().translationY(height).setDuration(500).start();
                layoutUserInfo.setY(position);

            }
        });

        btEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminDisabledUser.this);
                dialog.setCancelable(false);
                dialog.setTitle("Enable User");
                dialog.setMessage("Are you sure you want to enable this user?" );
                dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        enableUser();
                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                layoutUserInfo.setVisibility(View.VISIBLE);
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));


            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = tvSearch.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), AdminDisabledUser.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        // to filter recyclerview
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ArrayList<User> searchItems = new ArrayList<>();
                for(User documentSnapshot : userArrayList){
                    if(documentSnapshot.getFullName().toString().toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(documentSnapshot);
                    }
                    else if(documentSnapshot.getEmail().toString().toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(documentSnapshot);
                    }
                }
                userRVAdapter = new UserRVAdapter(searchItems,AdminDisabledUser.this);
                rvUser.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                userRVAdapter.setClickListener(AdminDisabledUser.this);
                rvUser.setAdapter(userRVAdapter);

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        uid = userRVAdapter.getItem(position).getUid();
        Log.e("ManageUser,  ", "Uid: " + uid);
        Toast.makeText(getApplicationContext()," Uid: " + uid, Toast.LENGTH_SHORT).show();

        readUser();

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

                                String pictureURL = document.getData().get("PictureURL").toString();

                                if(pictureURL == ""){

                                }
                                else{
                                    Picasso.with(getApplicationContext()).load(pictureURL).into(ivProfilePic);
                                }

                                tvEmail.setText(document.getData().get("Email").toString());
                                tvName.setText(document.getData().get("FullName").toString());
                                tvPhone.setText(document.getData().get("PhoneNum").toString());
                                tvAddress.setText(document.getData().get("Address").toString());

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float position = layoutUserInfo.getY();

        layoutUserInfo.setY(height);
        layoutUserInfo.animate().translationY(0).setDuration(500).start();

        layoutUserInfo.setVisibility(View.VISIBLE);

    }

    public void enableUser(){

        Log.e("Uid",uid);
        DocumentReference nameRef = db.collection("users").document(uid);

        nameRef
                .update("Status", "active")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("disableUser", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(),AdminDisabledUser.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Account has been enabled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("disableUser", "Error updating document", e);
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