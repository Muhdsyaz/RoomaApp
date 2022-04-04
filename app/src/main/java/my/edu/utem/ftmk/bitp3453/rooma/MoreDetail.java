package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class MoreDetail extends AppCompatActivity {

    String category;
    MaterialIconView mvBackBtn;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

        bundle = getIntent().getExtras();
        category = bundle.getString("category");
        Log.e("Data ", "MoreDetail: " + bundle);

        mvBackBtn = findViewById(R.id.mvBackBtn);

        Intent intent = getIntent();

        category = intent.getStringExtra("category");

        mvBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAvailableFacilities();
            }
        });
    }

    public void toAvailableFacilities(){
        Intent intent = new Intent(getApplicationContext(),AvailableFacilities.class);
        bundle.remove("Swimming");
        bundle.remove("Gymnasium");
        bundle.remove("Tennis");
        bundle.remove("Squash");
        bundle.remove("Mini");
        bundle.remove("Playground");
        bundle.remove("CarPark");
        bundle.remove("Elevator");
        bundle.remove("Guard");
        bundle.remove("Jogging");
        bundle.remove("Balcony");
        bundle.remove("CableTv");
        bundle.remove("AirCond");
        bundle.remove("Cooking");
        bundle.remove("Gender");
        bundle.remove("PublicTrans");
        bundle.remove("Internet");
        bundle.remove("Washing");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}