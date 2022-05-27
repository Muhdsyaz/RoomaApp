package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvailableFacilities extends AppCompatActivity {

    MaterialIconView mvBackBtn;
    String category;
    Button btContinue;
    RadioButton rbSwim, rbGym, rbTennis, rbSquash, rbMiniMarket, rbPlayground, rbCarPark, rbElevator, rbGuard, rbJogging, rbBalcony, rbCableTv;
    RadioButton rbAirCond, rbCooking, rbGender, rbPublicTrans, rbInternet, rbWashing;

    Bundle bundle;

    ArrayList<String> arrayListFacilities = new ArrayList<>();
    ArrayList<String> arrayListConvenience = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_facilities);

        //getting the data bundle from other activity incoming
        bundle = getIntent().getExtras();
        category = bundle.getString("category");

        mvBackBtn = findViewById(R.id.mvBackBtn);

        //declaration for radio button
        rbSwim = findViewById(R.id.rbSwim);
        rbGym = findViewById(R.id.rbGym);
        rbTennis = findViewById(R.id.rbTennis);
        rbSquash = findViewById(R.id.rbSquash);
        rbMiniMarket = findViewById(R.id.rbMiniMarket);
        rbPlayground = findViewById(R.id.rbPlayground);
        rbCarPark = findViewById(R.id.rbCarPark);
        rbElevator = findViewById(R.id.rbElevator);
        rbGuard = findViewById(R.id.rbGuard);
        rbJogging = findViewById(R.id.rbJogging);
        rbBalcony = findViewById(R.id.rbBalcony);
        rbCableTv = findViewById(R.id.rbCableTv);
        rbAirCond = findViewById(R.id.rbAirCond);
        rbCooking = findViewById(R.id.rbCooking);
        rbGender = findViewById(R.id.rbGender);
        rbPublicTrans = findViewById(R.id.rbPublicTrans);
        rbInternet = findViewById(R.id.rbInternet);
        rbWashing = findViewById(R.id.rbWashing);

        // declare variable for button
        btContinue = findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMoreDetail();
            }
        });

        rbSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbSwim.isSelected()) {
                    rbSwim.setChecked(true);
                    rbSwim.setSelected(true);
//                    facilities.putString("Swimming", "Swimming pool");
//                    Log.e("Swimming ", "onCreate: " + facilities);
                    arrayListFacilities.add("Swimming pool");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbSwim.setChecked(false);
                    rbSwim.setSelected(false);
//                    facilities.remove("Swimming");
//                    Log.e("Swimming ", "onCreate: " + facilities);
                    arrayListFacilities.remove("Swimming pool");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGym.isSelected()) {
                    rbGym.setChecked(true);
                    rbGym.setSelected(true);
//                    facilities.putString("Gymnasium", "Gymnasium");
//                    Log.e("Gymnasium ", "onCreate: " + facilities);

                    arrayListFacilities.add("Gymnasium");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbGym.setChecked(false);
                    rbGym.setSelected(false);
//                    facilities.remove("Gymnasium");
//                    Log.e("Gymnasium ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Gymnasium");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbTennis.isSelected()) {
                    rbTennis.setChecked(true);
                    rbTennis.setSelected(true);
//                    facilities.putString("Tennis", "Tennis court");
//                    Log.e("Tennis ", "onCreate: " + facilities);

                    arrayListFacilities.add("Tennis court");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbTennis.setChecked(false);
                    rbTennis.setSelected(false);
//                    facilities.remove("Tennis");
//                    Log.e("Tennis ", "onCreate: " + facilities);
                    arrayListFacilities.remove("Tennis court");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbSquash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbSquash.isSelected()) {
                    rbSquash.setChecked(true);
                    rbSquash.setSelected(true);
//                    facilities.putString("Squash", "Squash court");
//                    Log.e("Squash ", "onCreate: " + facilities);

                    arrayListFacilities.add("Squash court");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbSquash.setChecked(false);
                    rbSquash.setSelected(false);
//                    facilities.remove("Squash");
//                    Log.e("Squash ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Squash court");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbMiniMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbMiniMarket.isSelected()) {
                    rbMiniMarket.setChecked(true);
                    rbMiniMarket.setSelected(true);
//                    facilities.putString("Mini", "Mini market");
//                    Log.e("Mini ", "onCreate: " + facilities);

                    arrayListFacilities.add("Mini market");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbMiniMarket.setChecked(false);
                    rbMiniMarket.setSelected(false);
//                    facilities.remove("Mini");
//                    Log.e("Mini ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Mini market");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbPlayground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPlayground.isSelected()) {
                    rbPlayground.setChecked(true);
                    rbPlayground.setSelected(true);
//                    facilities.putString("Playground", "Playground");
//                    Log.e("Playground ", "onCreate: " + facilities);

                    arrayListFacilities.add("Playground");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbPlayground.setChecked(false);
                    rbPlayground.setSelected(false);
//                    facilities.remove("Playground");
//                    Log.e("Playground ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Playground");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbCarPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCarPark.isSelected()) {
                    rbCarPark.setChecked(true);
                    rbCarPark.setSelected(true);
//                    facilities.putString("CarPark", "Car Park");
//                    Log.e("CarPark ", "onCreate: " + facilities);

                    arrayListFacilities.add("Car Park");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbCarPark.setChecked(false);
                    rbCarPark.setSelected(false);
//                    facilities.remove("CarPark");
//                    Log.e("CarPark ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Car Park");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbElevator.isSelected()) {
                    rbElevator.setChecked(true);
                    rbElevator.setSelected(true);
//                    facilities.putString("Elevator", "Elevator");
//                    Log.e("Elevator ", "onCreate: " + facilities);

                    arrayListFacilities.add("Elevator");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbElevator.setChecked(false);
                    rbElevator.setSelected(false);
//                    facilities.remove("Elevator");
//                    Log.e("Elevator ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Elevator");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                }
            }
        });

        rbGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGuard.isSelected()) {
                    rbGuard.setChecked(true);
                    rbGuard.setSelected(true);
//                    facilities.putString("Guard", "Guard");
//                    Log.e("Guard ", "onCreate: " + facilities);

                    arrayListFacilities.add("Security guard");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbGuard.setChecked(false);
                    rbGuard.setSelected(false);
//                    facilities.remove("Guard");
//                    Log.e("Guard ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Security guard");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                }
            }
        });

        rbJogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbJogging.isSelected()) {
                    rbJogging.setChecked(true);
                    rbJogging.setSelected(true);
//                    facilities.putString("Jogging", "Jogging");
//                    Log.e("Jogging ", "onCreate: " + facilities);

                    arrayListFacilities.add("Jogging park");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbJogging.setChecked(false);
                    rbJogging.setSelected(false);
//                    facilities.remove("Jogging");
//                    Log.e("Jogging ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Jogging park");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbBalcony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbBalcony.isSelected()) {
                    rbBalcony.setChecked(true);
                    rbBalcony.setSelected(true);
//                    facilities.putString("Balcony", "Balcony");
//                    Log.e("Balcony ", "onCreate: " + facilities);

                    arrayListFacilities.add("Balcony");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);

                } else {
                    rbBalcony.setChecked(false);
                    rbBalcony.setSelected(false);
//                    facilities.remove("Balcony");
//                    Log.e("Balcony ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Balcony");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbCableTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCableTv.isSelected()) {
                    rbCableTv.setChecked(true);
                    rbCableTv.setSelected(true);
//                    facilities.putString("CableTv", "CableTv");
//                    Log.e("CableTv ", "onCreate: " + facilities);
                    arrayListFacilities.add("Cable Tv");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);


                } else {
                    rbCableTv.setChecked(false);
                    rbCableTv.setSelected(false);
//                    facilities.remove("CableTv");
//                    Log.e("CableTv ", "onCreate: " + facilities);

                    arrayListFacilities.remove("Cable Tv");
                    Log.e("Swimming ", "onCreate: " + arrayListFacilities);
                }
            }
        });

        rbAirCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbAirCond.isSelected()) {
                    rbAirCond.setChecked(true);
                    rbAirCond.setSelected(true);
//                    convenience.putString("AirCond", "Air-cond");
//                    Log.e("AirCond ", "onCreate: " + convenience);

                    arrayListConvenience.add("Air-cond");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbAirCond.setChecked(false);
                    rbAirCond.setSelected(false);
//                    convenience.remove("AirCond");
//                    Log.e("AirCond ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Air-cond");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);
                }
            }
        });

        rbCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCooking.isSelected()) {
                    rbCooking.setChecked(true);
                    rbCooking.setSelected(true);
//                    convenience.putString("Cooking", "Cooking");
//                    Log.e("Cooking ", "onCreate: " + convenience);

                    arrayListConvenience.add("Cooking allowed");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbCooking.setChecked(false);
                    rbCooking.setSelected(false);
//                    convenience.remove("Cooking");
//                    Log.e("Cooking ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Cooking allowed");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);
                }
            }
        });

        rbGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGender.isSelected()) {
                    rbGender.setChecked(true);
                    rbGender.setSelected(true);
//                    convenience.putString("Gender", "Mix-gender");
//                    Log.e("Gender ", "onCreate: " + convenience);

                    arrayListConvenience.add("Mix-gender");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbGender.setChecked(false);
                    rbGender.setSelected(false);
//                    convenience.remove("Gender");
//                    Log.e("Gender ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Mix-gender");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);
                }
            }
        });

        rbPublicTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPublicTrans.isSelected()) {
                    rbPublicTrans.setChecked(true);
                    rbPublicTrans.setSelected(true);
//                    convenience.putString("PublicTrans", "Public transport");
//                    Log.e("PublicTrans ", "onCreate: " + convenience);

                    arrayListConvenience.add("Public transport");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbPublicTrans.setChecked(false);
                    rbPublicTrans.setSelected(false);
//                    convenience.remove("PublicTrans");
//                    Log.e("PublicTrans ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Public transport");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                }
            }
        });

        rbInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbInternet.isSelected()) {
                    rbInternet.setChecked(true);
                    rbInternet.setSelected(true);
//                    convenience.putString("Internet", "Internet");
//                    Log.e("Internet ", "onCreate: " + convenience);

                    arrayListConvenience.add("Internet connection");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbInternet.setChecked(false);
                    rbInternet.setSelected(false);
//                    convenience.remove("Internet");
//                    Log.e("Internet ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Internet connection");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);
                }
            }
        });

        rbWashing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbWashing.isSelected()) {
                    rbWashing.setChecked(true);
                    rbWashing.setSelected(true);
//                    convenience.putString("Washing", "Washing machine");
//                    Log.e("Washing ", "onCreate: " + convenience);

                    arrayListConvenience.add("Washing machine");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);

                } else {
                    rbWashing.setChecked(false);
                    rbWashing.setSelected(false);
//                    convenience.remove("Washing");
//                    Log.e("Washing ", "onCreate: " + convenience);

                    arrayListConvenience.remove("Washing machine");
                    Log.e("AirCond ", "onCreate: " + arrayListConvenience);
                }
            }
        });


        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.equals("House")){
                    toHouseSpecification();
                }
                else if(category.equals("Apartment")){
                    toApartmentSpecification();
                }
                else{
                    toRoomSpecification();
                }
            }
        });

    }

    public void toApartmentSpecification(){
        Intent intent = new Intent(getApplicationContext(),ApartmentSpecification.class);
        intent.putExtra("category",category);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toHouseSpecification(){
        Intent intent = new Intent(getApplicationContext(),HouseSpecification.class);
        intent.putExtra("category",category);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toRoomSpecification(){
        Intent intent = new Intent(getApplicationContext(),RoomSpecification.class);
        intent.putExtra("category",category);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toMoreDetail(){

        bundle.putStringArrayList("facilities",arrayListFacilities);
        bundle.putStringArrayList("convenience",arrayListConvenience);

        Intent intent = new Intent(getApplicationContext(),MoreDetail.class);
        intent.putExtras(bundle);
        Log.e("Bundle ", "onCreate: " + bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}