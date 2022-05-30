package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ApartmentSpecification extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String category;
    Button btContinue;
    String text, resType, floorRange, bedroom, bathroom, furnishing, parking, propertySize, finishYear, deposit, monthlyRent;
    EditText etPropertySize, etFinishYear, etDeposit, etMonthlyRent;
    Spinner spResType, spFloorRange, spBedroom, spBathroom, spFurnishing, spParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_specification);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        // declare variable for edit text
        etPropertySize = findViewById(R.id.etPropertySize);
        etFinishYear = findViewById(R.id.etFinishYear);
        etDeposit = findViewById(R.id.etDeposit);
        etMonthlyRent = findViewById(R.id.etMonthlyRent);

        // declare variable for button
        btContinue = findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPropertySize.getText().toString().isEmpty() || etFinishYear.getText().toString().isEmpty() ||
                        etDeposit.getText().toString().isEmpty() || etMonthlyRent.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), "Please fill all the form before continue.", Toast.LENGTH_LONG).show();

                }
                else{
                    toAvailableFacilities();
                }

            }
        });

        Log.e("Aparment ", "onCreate: " + category);

        spResType = findViewById(R.id.spResType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.restype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spResType.setAdapter(adapter);
        spResType.setOnItemSelectedListener(this);
        resType = spResType.getSelectedItem().toString();

        spFloorRange = findViewById(R.id.spFloorRange);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.floor, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFloorRange.setAdapter(adapter2);
        spFloorRange.setOnItemSelectedListener(this);
        floorRange = spFloorRange.getSelectedItem().toString();

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
//        text = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        resType = spResType.getSelectedItem().toString();
        floorRange = spFloorRange.getSelectedItem().toString();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void toAvailableFacilities(){

        propertySize = etPropertySize.getText().toString();
        finishYear = etFinishYear.getText().toString();
        deposit = etDeposit.getText().toString();
        monthlyRent = etMonthlyRent.getText().toString();

        Intent intent = new Intent(getApplicationContext(),AvailableFacilities.class);
        Bundle bundle = new Bundle();
        bundle.putString("resType",resType);
        bundle.putString("floorRange",floorRange);
        bundle.putString("bedroom",bedroom);
        bundle.putString("bathroom",bathroom);
        bundle.putString("furnishing",furnishing);
        bundle.putString("parking",parking);
        bundle.putString("propertySize",propertySize);
        bundle.putString("finishYear", finishYear);
        bundle.putString("deposit",deposit);
        bundle.putString("monthlyRent",monthlyRent);
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