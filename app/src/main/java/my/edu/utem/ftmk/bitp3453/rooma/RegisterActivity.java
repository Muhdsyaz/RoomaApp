package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etFullName, etPassword, etConfirmPassword,etAddress, etPhoneNum;
    Button btSignUp;

    boolean check = true;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Map<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declare EditText
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        //declare Button
        btSignUp = findViewById(R.id.btSignUp);

        // call this method to check the condition of all
        // required field
        checkFieldCondition();

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });
    }

    public void registerUser()
    {

        Log.e("Test email: ", etEmail.getText().toString());

        user.put("Email", etEmail.getText().toString().trim());
        user.put("FullName", etFullName.getText().toString());
        user.put("PhoneNum", "+6" + etPhoneNum.getText().toString());
        user.put("Address", etAddress.getText().toString());
        user.put("Password", etConfirmPassword.getText().toString());
        user.put("Picture URL", "");

        if(etEmail.getText().toString().equals("") && etFullName.getText().toString().equals("") && etPhoneNum.getText().toString().equals("")
                && etPassword.getText().toString().equals("") && etAddress.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {

            Toast.makeText(RegisterActivity.this, "Please make sure all field are filled before submit.", Toast.LENGTH_LONG).show();
        }
        else if(check == false) {
            Log.e("Check: ", "" + check);
            Toast.makeText(RegisterActivity.this, "Please make sure to fill your information as required.", Toast.LENGTH_SHORT).show();
        }
        else {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(@NonNull AuthResult authResult) {

                            user.put("Uid", authResult.getUser().getUid());

                            // Register User add to Firebase
                            firebaseFirestore.collection("users")
                                    .document(authResult.getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void unused) {

                                            sendEmailVerification();

                                            Toast.makeText(RegisterActivity.this, "Sign Up Succesfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });


                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

    public void sendEmailVerification() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    public void checkFieldCondition(){

        etEmail.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please fill in your email.");

                    check = false;
                }else if(!etEmail.getText().toString().trim().contains("@") || !etEmail.getText().toString().trim().contains(".com")){
                    etEmail.setError("Invalid email format.");

                    check = false;

                } else {
                    etEmail.setError(null);

                    check = true;
                }
            }
        });

        etFullName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etFullName.getText().toString().isEmpty()) {
                    etFullName.setError("Please fill in your name.");

                    check = false;
                }else if(!etFullName.getText().toString().matches("^[a-zA-Z\\s]+$")){
                    etFullName.setError("Name cannot contain number or symbol.");

                    check = false;

                } else {
                    etFullName.setError(null);

                    check = true;
                }
            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etPhoneNum.getText().toString().isEmpty()) {
                    etPhoneNum.setError("Please fill in your phone number.");

                    check = false;
                }else if(etPhoneNum.getText().toString().length()<10 || etPhoneNum.getText().length()>11) {

                    etPhoneNum.setError("Invalid phone number range.");

                    check = false;

                }else if(!etPhoneNum.getText().toString().substring(0,1).equals("0")) {

                    etPhoneNum.setError("Invalid phone number.");

                    check = false;

                } else {
                    etPhoneNum.setError(null);

                    check = true;
                }
            }
        });

        etAddress.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etAddress.getText().toString().isEmpty()) {
                    etAddress.setError("Please fill in your address.");

                    check = false;
                }
                else {
                    etAddress.setError(null);

                    check = true;
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please fill in your password.");

                    check = false;
                }else if(etPassword.getText().toString().length() < 6){
                    etPassword.setError("Password too short, minimum characters at least 6.");

                    check = false;

                } else {
                    etPassword.setError(null);

                    check = true;
                }
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setError("Please fill in your confirm password.");

                    check = false;
                }else if(!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
                    etConfirmPassword.setError("Password is not match.");

                    check = false;

                } else {
                    etConfirmPassword.setError(null);

                    check = true;
                }
            }
        });

    }

    public void toStartActivity(View v){

        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}