package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAddress extends AppCompatActivity {

    String address;
    EditText etAddress;
    TextView tvDone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        Intent intent = getIntent();

        address = intent.getStringExtra("address");

        etAddress = findViewById(R.id.etAddress);
        etAddress.setText(address);

        tvDone = findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAddress();

            }
        });

    }

    public void updateAddress(){

        if(!etAddress.getText().toString().isEmpty()){
            DocumentReference addressRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            addressRef
                    .update("Address", etAddress.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("EditAddress", "DocumentSnapshot successfully updated!");

                            Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                            startActivity(intent);

                            Toast.makeText(EditAddress.this, "Address updated successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditAddress", "Error updating document", e);
                        }
                    });
        }
        else {
            Toast.makeText(EditAddress.this, "This field cannot be blank.", Toast.LENGTH_SHORT).show();
        }
    }

    public void toEditProfile(View v){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}