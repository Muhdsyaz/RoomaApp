package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.HashMap;
import java.util.Map;

public class AvailableFacilities extends AppCompatActivity {

    MaterialIconView mvBackBtn;
    String category, resType, floorRange, bedroom, bathroom, furnishing, parking;
    Button btContinue;
    RadioButton rbSwim, rbGym, rbTennis, rbSquash, rbMiniMarket, rbPlayground, rbCarPark, rbElevator, rbGuard, rbJogging, rbBalcony, rbCableTv;
    RadioButton rbAirCond, rbCooking, rbGender, rbPublicTrans, rbInternet, rbWashing;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_facilities);

        //getting the data bundle from other activity incoming
        extras = getIntent().getExtras();
        category = extras.getString("category");
//        resType = extras.getString("resType");
//        floorRange = extras.getString("floorRange");
//        bedroom = extras.getString("bedroom");
//        bathroom = extras.getString("bathroom");
//        furnishing = extras.getString("furnishing");
//        parking = extras.getString("parking");
//
//        Log.e("Data ", "onCreate: " + category + " " + resType + " " + floorRange);

        mvBackBtn = findViewById(R.id.mvBackBtn);

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
                    extras.putString("Swimming", "Swimming pool");
                    Log.e("Swimming ", "onCreate: " + extras);
                } else {
                    rbSwim.setChecked(false);
                    rbSwim.setSelected(false);
                    extras.remove("Swimming");
                    Log.e("Swimming ", "onCreate: " + extras);
                }
            }
        });

        rbGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGym.isSelected()) {
                    rbGym.setChecked(true);
                    rbGym.setSelected(true);
                    extras.putString("Gymnasium", "Gymnasium");
                    Log.e("Gymnasium ", "onCreate: " + extras);
                } else {
                    rbGym.setChecked(false);
                    rbGym.setSelected(false);
                    extras.remove("Gymnasium");
                    Log.e("Gymnasium ", "onCreate: " + extras);
                }
            }
        });

        rbTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbTennis.isSelected()) {
                    rbTennis.setChecked(true);
                    rbTennis.setSelected(true);
                    extras.putString("Tennis", "Tennis court");
                    Log.e("Tennis ", "onCreate: " + extras);
                } else {
                    rbTennis.setChecked(false);
                    rbTennis.setSelected(false);
                    extras.remove("Tennis");
                    Log.e("Tennis ", "onCreate: " + extras);
                }
            }
        });

        rbSquash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbSquash.isSelected()) {
                    rbSquash.setChecked(true);
                    rbSquash.setSelected(true);
                    extras.putString("Squash", "Squash court");
                    Log.e("Squash ", "onCreate: " + extras);
                } else {
                    rbSquash.setChecked(false);
                    rbSquash.setSelected(false);
                    extras.remove("Squash");
                    Log.e("Squash ", "onCreate: " + extras);
                }
            }
        });

        rbMiniMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbMiniMarket.isSelected()) {
                    rbMiniMarket.setChecked(true);
                    rbMiniMarket.setSelected(true);
                    extras.putString("Mini", "Mini market");
                    Log.e("Mini ", "onCreate: " + extras);
                } else {
                    rbMiniMarket.setChecked(false);
                    rbMiniMarket.setSelected(false);
                    extras.remove("Mini");
                    Log.e("Mini ", "onCreate: " + extras);
                }
            }
        });

        rbPlayground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPlayground.isSelected()) {
                    rbPlayground.setChecked(true);
                    rbPlayground.setSelected(true);
                    extras.putString("Playground", "Playground");
                    Log.e("Playground ", "onCreate: " + extras);
                } else {
                    rbPlayground.setChecked(false);
                    rbPlayground.setSelected(false);
                    extras.remove("Playground");
                    Log.e("Playground ", "onCreate: " + extras);
                }
            }
        });

        rbCarPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCarPark.isSelected()) {
                    rbCarPark.setChecked(true);
                    rbCarPark.setSelected(true);
                    extras.putString("CarPark", "Car Park");
                    Log.e("CarPark ", "onCreate: " + extras);
                } else {
                    rbCarPark.setChecked(false);
                    rbCarPark.setSelected(false);
                    extras.remove("CarPark");
                    Log.e("CarPark ", "onCreate: " + extras);
                }
            }
        });

        rbElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbElevator.isSelected()) {
                    rbElevator.setChecked(true);
                    rbElevator.setSelected(true);
                    extras.putString("Elevator", "Elevator");
                    Log.e("Elevator ", "onCreate: " + extras);
                } else {
                    rbElevator.setChecked(false);
                    rbElevator.setSelected(false);
                    extras.remove("Elevator");
                    Log.e("Elevator ", "onCreate: " + extras);
                }
            }
        });

        rbGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGuard.isSelected()) {
                    rbGuard.setChecked(true);
                    rbGuard.setSelected(true);
                    extras.putString("Guard", "Guard");
                    Log.e("Guard ", "onCreate: " + extras);
                } else {
                    rbGuard.setChecked(false);
                    rbGuard.setSelected(false);
                    extras.remove("Guard");
                    Log.e("Guard ", "onCreate: " + extras);
                }
            }
        });

        rbJogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbJogging.isSelected()) {
                    rbJogging.setChecked(true);
                    rbJogging.setSelected(true);
                    extras.putString("Jogging", "Jogging");
                    Log.e("Jogging ", "onCreate: " + extras);
                } else {
                    rbJogging.setChecked(false);
                    rbJogging.setSelected(false);
                    extras.remove("Jogging");
                    Log.e("Jogging ", "onCreate: " + extras);
                }
            }
        });

        rbBalcony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbBalcony.isSelected()) {
                    rbBalcony.setChecked(true);
                    rbBalcony.setSelected(true);
                    extras.putString("Balcony", "Balcony");
                    Log.e("Balcony ", "onCreate: " + extras);
                } else {
                    rbBalcony.setChecked(false);
                    rbBalcony.setSelected(false);
                    extras.remove("Balcony");
                    Log.e("Balcony ", "onCreate: " + extras);
                }
            }
        });

        rbCableTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCableTv.isSelected()) {
                    rbCableTv.setChecked(true);
                    rbCableTv.setSelected(true);
                    extras.putString("CableTv", "CableTv");
                    Log.e("CableTv ", "onCreate: " + extras);
                } else {
                    rbCableTv.setChecked(false);
                    rbCableTv.setSelected(false);
                    extras.remove("CableTv");
                    Log.e("CableTv ", "onCreate: " + extras);
                }
            }
        });

        rbAirCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbAirCond.isSelected()) {
                    rbAirCond.setChecked(true);
                    rbAirCond.setSelected(true);
                    extras.putString("AirCond", "Air-cond");
                    Log.e("AirCond ", "onCreate: " + extras);
                } else {
                    rbAirCond.setChecked(false);
                    rbAirCond.setSelected(false);
                    extras.remove("AirCond");
                    Log.e("AirCond ", "onCreate: " + extras);
                }
            }
        });

        rbCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCooking.isSelected()) {
                    rbCooking.setChecked(true);
                    rbCooking.setSelected(true);
                    extras.putString("Cooking", "Cooking");
                    Log.e("Cooking ", "onCreate: " + extras);
                } else {
                    rbCooking.setChecked(false);
                    rbCooking.setSelected(false);
                    extras.remove("Cooking");
                    Log.e("Cooking ", "onCreate: " + extras);
                }
            }
        });

        rbGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGender.isSelected()) {
                    rbGender.setChecked(true);
                    rbGender.setSelected(true);
                    extras.putString("Gender", "Mix-gender");
                    Log.e("Gender ", "onCreate: " + extras);
                } else {
                    rbGender.setChecked(false);
                    rbGender.setSelected(false);
                    extras.remove("Gender");
                    Log.e("Gender ", "onCreate: " + extras);
                }
            }
        });

        rbPublicTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPublicTrans.isSelected()) {
                    rbPublicTrans.setChecked(true);
                    rbPublicTrans.setSelected(true);
                    extras.putString("PublicTrans", "Public transport");
                    Log.e("PublicTrans ", "onCreate: " + extras);
                } else {
                    rbPublicTrans.setChecked(false);
                    rbPublicTrans.setSelected(false);
                    extras.remove("PublicTrans");
                    Log.e("PublicTrans ", "onCreate: " + extras);
                }
            }
        });

        rbInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbInternet.isSelected()) {
                    rbInternet.setChecked(true);
                    rbInternet.setSelected(true);
                    extras.putString("Internet", "Internet");
                    Log.e("Internet ", "onCreate: " + extras);
                } else {
                    rbInternet.setChecked(false);
                    rbInternet.setSelected(false);
                    extras.remove("Internet");
                    Log.e("Internet ", "onCreate: " + extras);
                }
            }
        });

        rbWashing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbWashing.isSelected()) {
                    rbWashing.setChecked(true);
                    rbWashing.setSelected(true);
                    extras.putString("Washing", "Washing machine");
                    Log.e("Washing ", "onCreate: " + extras);
                } else {
                    rbWashing.setChecked(false);
                    rbWashing.setSelected(false);
                    extras.remove("Washing");
                    Log.e("Washing ", "onCreate: " + extras);
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

    public void toMoreDetail(){
        Intent intent = new Intent(getApplicationContext(),MoreDetail.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}