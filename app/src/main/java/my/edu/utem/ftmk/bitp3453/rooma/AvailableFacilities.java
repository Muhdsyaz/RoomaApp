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
    String category;
    Button btContinue;
    RadioButton rbSwim, rbGym, rbTennis, rbSquash, rbMiniMarket, rbPlayground, rbCarPark, rbElevator, rbGuard, rbJogging, rbBalcony, rbCableTv;
    RadioButton rbAirCond, rbCooking, rbGender, rbPublicTrans, rbInternet, rbWashing;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_facilities);

        //getting the data bundle from other activity incoming
        bundle = getIntent().getExtras();
        category = bundle.getString("category");

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
                    bundle.putString("Swimming", "Swimming pool");
                    Log.e("Swimming ", "onCreate: " + bundle);
                } else {
                    rbSwim.setChecked(false);
                    rbSwim.setSelected(false);
                    bundle.remove("Swimming");
                    Log.e("Swimming ", "onCreate: " + bundle);
                }
            }
        });

        rbGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGym.isSelected()) {
                    rbGym.setChecked(true);
                    rbGym.setSelected(true);
                    bundle.putString("Gymnasium", "Gymnasium");
                    Log.e("Gymnasium ", "onCreate: " + bundle);
                } else {
                    rbGym.setChecked(false);
                    rbGym.setSelected(false);
                    bundle.remove("Gymnasium");
                    Log.e("Gymnasium ", "onCreate: " + bundle);
                }
            }
        });

        rbTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbTennis.isSelected()) {
                    rbTennis.setChecked(true);
                    rbTennis.setSelected(true);
                    bundle.putString("Tennis", "Tennis court");
                    Log.e("Tennis ", "onCreate: " + bundle);
                } else {
                    rbTennis.setChecked(false);
                    rbTennis.setSelected(false);
                    bundle.remove("Tennis");
                    Log.e("Tennis ", "onCreate: " + bundle);
                }
            }
        });

        rbSquash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbSquash.isSelected()) {
                    rbSquash.setChecked(true);
                    rbSquash.setSelected(true);
                    bundle.putString("Squash", "Squash court");
                    Log.e("Squash ", "onCreate: " + bundle);
                } else {
                    rbSquash.setChecked(false);
                    rbSquash.setSelected(false);
                    bundle.remove("Squash");
                    Log.e("Squash ", "onCreate: " + bundle);
                }
            }
        });

        rbMiniMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbMiniMarket.isSelected()) {
                    rbMiniMarket.setChecked(true);
                    rbMiniMarket.setSelected(true);
                    bundle.putString("Mini", "Mini market");
                    Log.e("Mini ", "onCreate: " + bundle);
                } else {
                    rbMiniMarket.setChecked(false);
                    rbMiniMarket.setSelected(false);
                    bundle.remove("Mini");
                    Log.e("Mini ", "onCreate: " + bundle);
                }
            }
        });

        rbPlayground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPlayground.isSelected()) {
                    rbPlayground.setChecked(true);
                    rbPlayground.setSelected(true);
                    bundle.putString("Playground", "Playground");
                    Log.e("Playground ", "onCreate: " + bundle);
                } else {
                    rbPlayground.setChecked(false);
                    rbPlayground.setSelected(false);
                    bundle.remove("Playground");
                    Log.e("Playground ", "onCreate: " + bundle);
                }
            }
        });

        rbCarPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCarPark.isSelected()) {
                    rbCarPark.setChecked(true);
                    rbCarPark.setSelected(true);
                    bundle.putString("CarPark", "Car Park");
                    Log.e("CarPark ", "onCreate: " + bundle);
                } else {
                    rbCarPark.setChecked(false);
                    rbCarPark.setSelected(false);
                    bundle.remove("CarPark");
                    Log.e("CarPark ", "onCreate: " + bundle);
                }
            }
        });

        rbElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbElevator.isSelected()) {
                    rbElevator.setChecked(true);
                    rbElevator.setSelected(true);
                    bundle.putString("Elevator", "Elevator");
                    Log.e("Elevator ", "onCreate: " + bundle);
                } else {
                    rbElevator.setChecked(false);
                    rbElevator.setSelected(false);
                    bundle.remove("Elevator");
                    Log.e("Elevator ", "onCreate: " + bundle);
                }
            }
        });

        rbGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGuard.isSelected()) {
                    rbGuard.setChecked(true);
                    rbGuard.setSelected(true);
                    bundle.putString("Guard", "Guard");
                    Log.e("Guard ", "onCreate: " + bundle);
                } else {
                    rbGuard.setChecked(false);
                    rbGuard.setSelected(false);
                    bundle.remove("Guard");
                    Log.e("Guard ", "onCreate: " + bundle);
                }
            }
        });

        rbJogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbJogging.isSelected()) {
                    rbJogging.setChecked(true);
                    rbJogging.setSelected(true);
                    bundle.putString("Jogging", "Jogging");
                    Log.e("Jogging ", "onCreate: " + bundle);
                } else {
                    rbJogging.setChecked(false);
                    rbJogging.setSelected(false);
                    bundle.remove("Jogging");
                    Log.e("Jogging ", "onCreate: " + bundle);
                }
            }
        });

        rbBalcony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbBalcony.isSelected()) {
                    rbBalcony.setChecked(true);
                    rbBalcony.setSelected(true);
                    bundle.putString("Balcony", "Balcony");
                    Log.e("Balcony ", "onCreate: " + bundle);
                } else {
                    rbBalcony.setChecked(false);
                    rbBalcony.setSelected(false);
                    bundle.remove("Balcony");
                    Log.e("Balcony ", "onCreate: " + bundle);
                }
            }
        });

        rbCableTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCableTv.isSelected()) {
                    rbCableTv.setChecked(true);
                    rbCableTv.setSelected(true);
                    bundle.putString("CableTv", "CableTv");
                    Log.e("CableTv ", "onCreate: " + bundle);
                } else {
                    rbCableTv.setChecked(false);
                    rbCableTv.setSelected(false);
                    bundle.remove("CableTv");
                    Log.e("CableTv ", "onCreate: " + bundle);
                }
            }
        });

        rbAirCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbAirCond.isSelected()) {
                    rbAirCond.setChecked(true);
                    rbAirCond.setSelected(true);
                    bundle.putString("AirCond", "Air-cond");
                    Log.e("AirCond ", "onCreate: " + bundle);
                } else {
                    rbAirCond.setChecked(false);
                    rbAirCond.setSelected(false);
                    bundle.remove("AirCond");
                    Log.e("AirCond ", "onCreate: " + bundle);
                }
            }
        });

        rbCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbCooking.isSelected()) {
                    rbCooking.setChecked(true);
                    rbCooking.setSelected(true);
                    bundle.putString("Cooking", "Cooking");
                    Log.e("Cooking ", "onCreate: " + bundle);
                } else {
                    rbCooking.setChecked(false);
                    rbCooking.setSelected(false);
                    bundle.remove("Cooking");
                    Log.e("Cooking ", "onCreate: " + bundle);
                }
            }
        });

        rbGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbGender.isSelected()) {
                    rbGender.setChecked(true);
                    rbGender.setSelected(true);
                    bundle.putString("Gender", "Mix-gender");
                    Log.e("Gender ", "onCreate: " + bundle);
                } else {
                    rbGender.setChecked(false);
                    rbGender.setSelected(false);
                    bundle.remove("Gender");
                    Log.e("Gender ", "onCreate: " + bundle);
                }
            }
        });

        rbPublicTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbPublicTrans.isSelected()) {
                    rbPublicTrans.setChecked(true);
                    rbPublicTrans.setSelected(true);
                    bundle.putString("PublicTrans", "Public transport");
                    Log.e("PublicTrans ", "onCreate: " + bundle);
                } else {
                    rbPublicTrans.setChecked(false);
                    rbPublicTrans.setSelected(false);
                    bundle.remove("PublicTrans");
                    Log.e("PublicTrans ", "onCreate: " + bundle);
                }
            }
        });

        rbInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbInternet.isSelected()) {
                    rbInternet.setChecked(true);
                    rbInternet.setSelected(true);
                    bundle.putString("Internet", "Internet");
                    Log.e("Internet ", "onCreate: " + bundle);
                } else {
                    rbInternet.setChecked(false);
                    rbInternet.setSelected(false);
                    bundle.remove("Internet");
                    Log.e("Internet ", "onCreate: " + bundle);
                }
            }
        });

        rbWashing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rbWashing.isSelected()) {
                    rbWashing.setChecked(true);
                    rbWashing.setSelected(true);
                    bundle.putString("Washing", "Washing machine");
                    Log.e("Washing ", "onCreate: " + bundle);
                } else {
                    rbWashing.setChecked(false);
                    rbWashing.setSelected(false);
                    bundle.remove("Washing");
                    Log.e("Washing ", "onCreate: " + bundle);
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
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}