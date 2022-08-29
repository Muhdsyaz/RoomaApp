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
import android.widget.Toast;

public class RoomSpecification extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String category;
    Button btContinue;
    Spinner spRoomType, spFloorRange, spFurnishing, spParking;
    String bedroom, bathroom, roomType, floorRange, furnishing, parking, propertySize, finishYear, deposit, monthlyRent;
    EditText etPropertySize, etFinishYear, etDeposit, etMonthlyRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_specification);

        // declare variable for edit text
        etPropertySize = findViewById(R.id.etRoomSize);
        etFinishYear = findViewById(R.id.etFinishYear);
        etDeposit = findViewById(R.id.etDeposit);
        etMonthlyRent = findViewById(R.id.etMonthlyRent);

        Intent intent = getIntent();

        category = intent.getStringExtra("category");

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

        Log.e("Room ", "onCreate: " + category);

        spRoomType = findViewById(R.id.spRoomType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roomtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoomType.setAdapter(adapter);
        spRoomType.setOnItemSelectedListener(this);
        roomType = spRoomType.getSelectedItem().toString();

        spFloorRange = findViewById(R.id.spFloorRange);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.floor, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFloorRange.setAdapter(adapter2);
        spFloorRange.setOnItemSelectedListener(this);
        floorRange = spFloorRange.getSelectedItem().toString();

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
//        String text = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        roomType = spRoomType.getSelectedItem().toString();
        floorRange = spFloorRange.getSelectedItem().toString();
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

        bedroom = "1";
        bathroom = "1";

        Intent intent = new Intent(getApplicationContext(),AvailableFacilities.class);
        Bundle bundle = new Bundle();
        bundle.putString("resType",roomType);
        bundle.putString("bedroom",bedroom);
        bundle.putString("bathroom",bathroom);
        bundle.putString("floorRange",floorRange);
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