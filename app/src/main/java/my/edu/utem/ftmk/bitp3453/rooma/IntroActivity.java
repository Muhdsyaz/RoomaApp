package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    Intent intent;
    private FirebaseAuth mAuth;

    ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        splashImg = findViewById(R.id.splashImg);
        splashImg.animate().translationY(-1600).setDuration(1000).setStartDelay(2000);

        intent = new Intent(this, LoginActivity.class);

        Thread thread = new Thread(){
            public void run(){

                try{
                    Thread.sleep(3000);
                }
                catch(Exception e){
                }

                intent = new Intent(IntroActivity.this, StartActivity.class);
                startActivity(intent);

            }};
        thread.start();

    }

    public void toMapActivity(){
        Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}