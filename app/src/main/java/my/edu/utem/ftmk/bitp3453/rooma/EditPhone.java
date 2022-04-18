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

public class EditPhone extends AppCompatActivity {

    String phone;
    EditText etPhone;
    TextView tvDone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        Intent intent = getIntent();

        phone = intent.getStringExtra("phone");

        etPhone = findViewById(R.id.etPhone);
        etPhone.setText(phone);

        tvDone = findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePhone();

            }
        });

    }

    public void updatePhone(){

        if(!etPhone.getText().toString().isEmpty()){
            DocumentReference phoneRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            phoneRef
                    .update("PhoneNum", etPhone.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("EditPhone", "DocumentSnapshot successfully updated!");

                            Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                            startActivity(intent);

                            Toast.makeText(EditPhone.this, "Phone updated successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditPhone", "Error updating document", e);
                        }
                    });
        }
        else {
            Toast.makeText(EditPhone.this, "This field cannot be blank.", Toast.LENGTH_SHORT).show();
        }
    }

    public void toEditProfile(View v){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}