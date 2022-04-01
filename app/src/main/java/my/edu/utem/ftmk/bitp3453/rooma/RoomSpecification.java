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

public class RoomSpecification extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String category;
    Button btContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_specification);

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

        Log.e("Room ", "onCreate: " + category);

        Spinner spResType = findViewById(R.id.spResType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.restype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spResType.setAdapter(adapter);
        spResType.setOnItemSelectedListener(this);

        Spinner spFloorRange = findViewById(R.id.spFloorRange);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.floor, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFloorRange.setAdapter(adapter2);
        spFloorRange.setOnItemSelectedListener(this);

        Spinner spFurnishing = findViewById(R.id.spFurnishing);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.furnishing, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFurnishing.setAdapter(adapter5);
        spFurnishing.setOnItemSelectedListener(this);

        Spinner spParking = findViewById(R.id.spParking);
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.parking, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParking.setAdapter(adapter6);
        spParking.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
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
        intent.putExtra("category",category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}