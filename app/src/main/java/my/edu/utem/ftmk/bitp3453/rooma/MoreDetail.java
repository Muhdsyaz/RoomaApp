package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class MoreDetail extends AppCompatActivity {

    String category;
    MaterialIconView mvBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);

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
        intent.putExtra("category",category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}