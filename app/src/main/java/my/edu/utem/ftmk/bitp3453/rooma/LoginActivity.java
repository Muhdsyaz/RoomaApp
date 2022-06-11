package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity" ;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText etEmail, etPassword, etEmailReset;
    private Button btLogin, btSubmit, btCancel;
    private TextView tvForgotPassword;
    private LinearLayout layoutResetPassword;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        etEmailReset = findViewById(R.id.etEmailReset);

        layoutResetPassword = findViewById(R.id.layoutResetPassword);

        btSubmit = findViewById(R.id.btSubmit);
        btCancel = findViewById(R.id.btCancel);

        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutResetPassword.setVisibility(View.VISIBLE);
                tvForgotPassword.setVisibility(View.INVISIBLE);

                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkExistingUser();
                    }
                });

                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutResetPassword.setVisibility(View.INVISIBLE);
                        tvForgotPassword.setVisibility(View.VISIBLE);
                    }
                });


            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDisabledUser();
                //loginUser();
            }
        });
    }

    public void loginUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing you in...");
        progressDialog.show();

        if(!email.equals("") && !password.equals("")) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(email.equals("applicationrooma@gmail.com")){
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    toAdminMenu();
                                }
                                else {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    toHomeActivity();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();

                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

        }
    }

    public void checkExistingUser(){

        String email = etEmailReset.getText().toString().trim();

        if(!email.isEmpty()) {

            if(email.contains("@") && email.contains(".com")){

                Log.d(TAG, "Valid Email.");

                db.collection("users")
                        .whereEqualTo("Email", etEmailReset.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        String user = document.getString("Email");

                                        if(user.equals(etEmailReset.getText().toString())){
                                            Log.d(TAG, "User Exists");

                                            // if the user is exist in database, then password
                                            // reset will be send to the user's email
                                            sendPasswordReset();

                                        }
                                    }
                                }
                                if(task.getResult().size() == 0 ) {
                                    Log.d(TAG, "User not Exists");

                                    Toast.makeText(LoginActivity.this, "User does not exist!", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

            }
            else{

                Toast.makeText(LoginActivity.this, "Invalid email format.", Toast.LENGTH_LONG).show();

            }

        }
        else{

            Toast.makeText(LoginActivity.this, "Please enter your email first before submit.", Toast.LENGTH_LONG).show();

        }
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"applicationrooma@gmail.com"};
//        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My account has been disabled.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sorry to bother you, my account has been disabled by the system. " +
                "May I know the reason behind it?");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPasswordReset() {

        String emailAddress = etEmailReset.getText().toString().trim();

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(LoginActivity.this, "Email sent.", Toast.LENGTH_LONG).show();

                            layoutResetPassword.setVisibility(View.INVISIBLE);
                            tvForgotPassword.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void checkDisabledUser(){
        db.collection("users")
                .whereEqualTo("Email", etEmail.getText().toString())
                .whereEqualTo("Status","disabled")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

//                                status = document.getData().get("Status").toString();
//                                Log.e("Check", "status: " + status);

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                builder.setTitle("Your account has been disabled");
                                builder.setMessage("Pleas contact the admin to enable your account back.");
                                builder.setCancelable(false);

                                builder.setPositiveButton("Contact", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        sendEmail();

                                    }
                                });

                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }

                            if(task.getResult().size() == 0 ) {
                                Log.d(TAG, "User not Exists");

                                loginUser();

                            }
                        }
                    }
                });
    }

    public void toHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void toAdminMenu(){
        Intent intent = new Intent(LoginActivity.this, AdminMenu.class);
        startActivity(intent);
    }

    public void toStartActivity(View v){

        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}