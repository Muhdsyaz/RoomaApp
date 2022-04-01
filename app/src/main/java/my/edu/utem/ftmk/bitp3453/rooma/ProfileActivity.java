package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout layoutProfile, layoutProfileMenu;
    TextView tvEdit, tvLogout;
    MaterialIconView mvProfileMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //set variable for textview
        tvEdit = findViewById(R.id.tvEdit);
        tvLogout = findViewById(R.id.tvLogout);

        //set variable for layout
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutProfileMenu = findViewById(R.id.layoutProfileMenu);

        //set variable for material icon
        mvProfileMenu = findViewById(R.id.mvProfileMenu);

        mvProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.VISIBLE);
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProfileMenu.setVisibility(View.INVISIBLE);
            }
        });

        layoutProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    public void toHome(View v){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void toFavorite(View v){
        Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
        startActivity(intent);
    }

    public void toPostAds(View v){
        Intent intent = new Intent(getApplicationContext(),PostAdsActivity.class);
        startActivity(intent);
    }

    public void toEditProfile(View v){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        startActivity(intent);
    }

    public void toAdsHistory(View v){
        Intent intent = new Intent(getApplicationContext(),AdsHistory.class);
        startActivity(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}