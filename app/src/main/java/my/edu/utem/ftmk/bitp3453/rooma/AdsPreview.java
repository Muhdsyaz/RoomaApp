package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class AdsPreview extends AppCompatActivity {

    Bundle bundle;
    Uri houseUri, bedroomUri, bathroomUri, livingroomUri, kitchenUri;

    ImageView ivAdsCover, ivHouse, ivBedroom, ivBathroom, ivLivingRoom, ivKitchen;
    LinearLayout layoutAdsPreview;
    HorizontalScrollView layoutHorizontal;
    TextView tvTitle, tvMonthlyRent, tvDate, tvCategory, tvLocation, tvResType, tvFloor, tvBedroom, tvBathroom, tvSize, tvFurnishing, tvFacilities, tvYear, tvDeposit, tvOther, tvDescription;
    String title, monthlyRent, category, location, resType, floor, bedroom, bathroom, size, furnishing, facilities, year, deposit, other, description;
    String adsID, ownerUid, state, city;

    ArrayList<String> arrayListFacilities;
    ArrayList<String> arrayListConvenience;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_preview);

        //getting the data bundle from other activity incoming
        bundle = getIntent().getExtras();

        //assigning uri value from previous activity
        houseUri = Uri.parse(bundle.getString("houseUri"));
        bedroomUri = Uri.parse(bundle.getString("bedroomUri"));
        bathroomUri = Uri.parse(bundle.getString("bathroomUri"));
        livingroomUri = Uri.parse(bundle.getString("livingroomUri"));
        kitchenUri = Uri.parse(bundle.getString("kitchenUri"));

        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(10,19);

        ownerUid = bundle.getString("ownerUid");
        adsID = bundle.getString("adsID");

        title = bundle.getString("title");
        monthlyRent = bundle.getString("monthlyRent");
        category = bundle.getString("category");
        location = bundle.getString("state") + " > " + bundle.getString("city");
        resType = bundle.getString("resType");
        floor = bundle.getString("floorRange");
        bedroom = bundle.getString("bedroom");
        bathroom = bundle.getString("bathroom");
        size = bundle.getString("propertySize");
        furnishing = bundle.getString("furnishing");
        year = bundle.getString("finishYear");
        deposit = bundle.getString("deposit");
        description = bundle.getString("description");

        arrayListFacilities = new ArrayList<>();
        arrayListConvenience = new ArrayList<>();

        arrayListFacilities = bundle.getStringArrayList("facilities");
        arrayListConvenience = bundle.getStringArrayList("convenience");

        //declare textview
        tvTitle = findViewById(R.id.tvTitle);
        tvMonthlyRent = findViewById(R.id.tvMonthlyRent);
        tvDate = findViewById(R.id.tvDate);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        tvResType = findViewById(R.id.tvResType);
        tvFloor = findViewById(R.id.tvFloor);
        tvBedroom = findViewById(R.id.tvBedroom);
        tvBathroom = findViewById(R.id.tvBathroom);
        tvSize = findViewById(R.id.tvSize);
        tvFurnishing = findViewById(R.id.tvFurnishing);
        tvFacilities = findViewById(R.id.tvFacilities);
        tvYear = findViewById(R.id.tvYear);
        tvDeposit = findViewById(R.id.tvDeposit);
        tvOther = findViewById(R.id.tvOther);
        tvDescription = findViewById(R.id.tvDescription);

        //assign value to textview
        tvTitle.setText(title);
        tvMonthlyRent.setText("RM " + monthlyRent);
        tvDate.setText(todayDate + " " + todayTime);
        tvCategory.setText(category);
        tvLocation.setText(location);
        tvResType.setText(resType);
        tvFloor.setText(floor);
        tvBedroom.setText(bedroom);
        tvBathroom.setText(bathroom);
        tvSize.setText(size);
        tvFurnishing.setText(furnishing);
        tvFacilities.setText(arrayListFacilities.toString());
        tvYear.setText(year);
        tvDeposit.setText("RM " + deposit);
        tvOther.setText(arrayListConvenience.toString());
        tvDescription.setText(description);

        //declare imageview
        ivAdsCover = findViewById(R.id.ivAdsCover);
        ivHouse = findViewById(R.id.ivHouse);
        ivBedroom = findViewById(R.id.ivBedroom);
        ivBathroom = findViewById(R.id.ivBathroom);
        ivLivingRoom = findViewById(R.id.ivLivingRoom);
        ivKitchen = findViewById(R.id.ivKitchen);

        //set uri to imageview
        ivAdsCover.setImageURI(houseUri);
        ivHouse.setImageURI(houseUri);
        ivBedroom.setImageURI(bedroomUri);
        ivBathroom.setImageURI(bathroomUri);
        ivLivingRoom.setImageURI(livingroomUri);
        ivKitchen.setImageURI(kitchenUri);

        //define layout
        layoutAdsPreview = findViewById(R.id.layoutAdsPreview);
        layoutHorizontal = findViewById(R.id.layoutHorizontal);

        //if image house is clicked, then show layouthorizontal
        ivAdsCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHorizontal.setVisibility(View.VISIBLE);
            }
        });

        layoutAdsPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutHorizontal.setVisibility(View.INVISIBLE);
            }
        });

        layoutHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}