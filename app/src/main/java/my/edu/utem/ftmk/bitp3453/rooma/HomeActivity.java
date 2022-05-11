package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdvertisementRVAdapter.ItemClickListener{

    TextView tvClear;
    Spinner spCategory, spMinPrice, spMaxPrice, spState, spCity, spSort;
    Button btSearch;

    String adsID, category, minPrice, maxPrice, state, city, sort;

    ConstraintLayout layoutAdvertisement;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView rvAdvertisement;
    private ArrayList<Advertisement> advertisementArrayList;
    private AdvertisementRVAdapter advertisementRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    private TextView tvEmptyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //declare spinner
        spCategory = findViewById(R.id.spCategory);
        spMinPrice = findViewById(R.id.spMinPrice);
        spMaxPrice = findViewById(R.id.spMaxPrice);
        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        spSort = findViewById(R.id.spSort);

        //declare textview
        tvClear = findViewById(R.id.tvClear);

        //declare button
        btSearch = findViewById(R.id.btSearch);

        layoutAdvertisement = findViewById(R.id.layoutAdvertisement);



        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter1);
        spCategory.setOnItemSelectedListener(this);
        category = spCategory.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.minprice, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMinPrice.setAdapter(adapter2);
        spMinPrice.setOnItemSelectedListener(this);
        minPrice = spMinPrice.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.maxprice, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaxPrice.setAdapter(adapter3);
        spMaxPrice.setOnItemSelectedListener(this);
        maxPrice = spMaxPrice.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.sort, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(adapter4);
        spSort.setOnItemSelectedListener(this);
        sort = spSort.getSelectedItem().toString();


        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);

            }
        });

        // <---- Spinner City and State

        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);

        MalaysiaStateAndCity malaysiaStateCity = new MalaysiaStateAndCity();
        ArrayList<ArrayList<String>> stateAndCity = malaysiaStateCity.getStateAndCity();

        final ArrayAdapter<String> adapterStateCity = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stateAndCity.get(0)){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterStateCity.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spState.setAdapter(adapterStateCity);
        spState.setAdapter(adapterStateCity);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter adapterCity = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, stateAndCity.get(i)) {

                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);

                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spCity.setAdapter(adapterCity);

                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if (i > 0) {
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    state = spState.getSelectedItem().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    String selectedcity = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected city : " + selectedcity, Toast.LENGTH_SHORT)
                            .show();

                    city = spCity.getSelectedItem().toString();

                    Log.e("State ", "onCreate: " + state);
                    Log.e("City ", "onCreate: " + city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // -----> Spinner City and State

        // initializing our variables.
        rvAdvertisement = findViewById(R.id.rvAdvertisement);
        loadingPB = findViewById(R.id.idProgressBar);

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

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Courses
        // now we will be getting the data from the same reference.
        db.collection("advertisements").get()
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
                Toast.makeText(getApplicationContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        String title = advertisementRVAdapter.getItem(position).getTitle();
        adsID = advertisementRVAdapter.getItem(position).getAdsID();
        Toast.makeText(getApplicationContext(),"Title: " + title + " AdsID: " + adsID, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        startActivity(intent);

        toDisplayAds(adsID);
    }

    public void toFavorite(View v){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
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

    public void toDisplayAds(String adsID){
        Intent intent = new Intent(getApplicationContext(),DisplayAdvertisement.class);
        intent.putExtra("adsID", adsID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = spCategory.getSelectedItem().toString();
        minPrice = spMinPrice.getSelectedItem().toString();
        maxPrice = spMaxPrice.getSelectedItem().toString();
        Log.e("Property ", " Category: " + category);
        Log.e("Min ", " Price: " + minPrice);
        Log.e("Max ", " Price: " + maxPrice);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}