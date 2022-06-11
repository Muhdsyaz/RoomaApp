package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LinearLayout llLogin, llRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        llLogin = findViewById(R.id.llLogin);
        llRegister = findViewById(R.id.llRegister);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });

        llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            if(currentUser.getEmail().equals("applicationrooma@gmail.com")){
                toAdminMenu();
            }
            else{
                toHomeActivity();
            }
        }
    }

    public void toHomeActivity(){
        Intent intent = new Intent(StartActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void toAdminMenu(){
        Intent intent = new Intent(StartActivity.this, AdminMenu.class);
        startActivity(intent);
    }

    public void toLoginActivity(){
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void toRegisterActivity(){
        Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}