package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void toEditName(View v){
        Intent intent = new Intent(getApplicationContext(),EditName.class);
        startActivity(intent);
    }

    public void toEditPhone(View v){
        Intent intent = new Intent(getApplicationContext(),EditPhone.class);
        startActivity(intent);
    }

    public void toEditAddress(View v){
        Intent intent = new Intent(getApplicationContext(),EditAddress.class);
        startActivity(intent);
    }

    public void toEditPassword(View v){
        Intent intent = new Intent(getApplicationContext(),EditPassword.class);
        startActivity(intent);
    }

    public void toProfile(View v){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}