package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class HouseSpecification extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String category;
    Button btContinue;
    String text, houseType, bedroom, bathroom, furnishing, parking, propertySize, year, deposit, monthlyRent;
    Spinner spHouseType, spBedroom, spBathroom, spFurnishing, spParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_specification);

        Intent intent = getIntent();

        category = intent.getStringExtra("category");

        // declare variable for button
        btContinue = findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAvailableFacilities();
            }
        });

        Log.e("House ", "onCreate: " + category);

        spHouseType = findViewById(R.id.spHouseType);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.housetype, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHouseType.setAdapter(adapter1);
        spHouseType.setOnItemSelectedListener(this);
        houseType = spHouseType.getSelectedItem().toString();

        spBedroom = findViewById(R.id.spBedroom);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.bedroom, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBedroom.setAdapter(adapter3);
        spBedroom.setOnItemSelectedListener(this);
        bedroom = spBedroom.getSelectedItem().toString();

        spBathroom = findViewById(R.id.spBathroom);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.bathroom, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBathroom.setAdapter(adapter4);
        spBathroom.setOnItemSelectedListener(this);
        bathroom = spBathroom.getSelectedItem().toString();

        spFurnishing = findViewById(R.id.spFurnishing);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.furnishing, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFurnishing.setAdapter(adapter5);
        spFurnishing.setOnItemSelectedListener(this);
        furnishing = spFurnishing.getSelectedItem().toString();

        spParking = findViewById(R.id.spParking);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.parking, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParking.setAdapter(adapter6);
        spParking.setOnItemSelectedListener(this);
        parking = spParking.getSelectedItem().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        houseType = spHouseType.getSelectedItem().toString();
        bedroom = spBedroom.getSelectedItem().toString();
        bathroom = spBathroom.getSelectedItem().toString();
        furnishing = spFurnishing.getSelectedItem().toString();
        parking = spParking.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void toPostAds(View v){
        Intent intent = new Intent(getApplicationContext(),PostAdsActivity.class);
        startActivity(intent);
    }

    public void toAvailableFacilities(){
        Intent intent = new Intent(getApplicationContext(),AvailableFacilities.class);
        Bundle bundle = new Bundle();
        bundle.putString("resType",houseType);
        bundle.putString("bedroom",bedroom);
        bundle.putString("bathroom",bathroom);
        bundle.putString("furnishing",furnishing);
        bundle.putString("parking",parking);
        bundle.putString("category",category);
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("Data ", "onCreate: " + bundle);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}