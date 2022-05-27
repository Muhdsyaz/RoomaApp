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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPassword extends AppCompatActivity {

    String password;
    EditText etPassword, etConfirmPassword;
    TextView tvDone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        Intent intent = getIntent();

        password = intent.getStringExtra("password");

        etPassword = findViewById(R.id.etPassword);
        etPassword.setText(password);

        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etConfirmPassword.setText(password);

        tvDone = findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePassword();

            }
        });

    }

    public boolean validatePassword(){
        if(etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
            return true;
        }
        else
            return false;
    }

    public void updatePassword(){

        if(etPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty())
        {
            Toast.makeText(EditPassword.this, "Password cannot be blank!", Toast.LENGTH_SHORT).show();
        }
        else if(!validatePassword()){
            Toast.makeText(EditPassword.this, "Password is not match!", Toast.LENGTH_SHORT).show();
        }
        else {

            DocumentReference passwordRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            passwordRef
                    .update("Password", etConfirmPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("EditPassword", "DocumentSnapshot successfully updated!");

                            Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                            startActivity(intent);

                            updateUserPassword();

                            Toast.makeText(EditPassword.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditPassword", "Error updating document", e);
                        }
                    });
        }
    }

    public void updateUserPassword() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = etConfirmPassword.getText().toString();
        Log.e("Check password: ", newPassword);

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("EditPassword", "User password updated.");
                        }
                    }
                });
    }

    public void toEditProfile(View v){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}