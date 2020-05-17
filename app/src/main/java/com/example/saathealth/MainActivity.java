package com.example.saathealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        user=getSharedPreferences("user",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user.getBoolean("isLogged",false)){
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    home.putExtra("phoneNum",user.getString("userPhone",null));
                    startActivity(home);
                    finish();
                }
                else {
                    Intent home = new Intent(MainActivity.this, SignIn.class);
                    startActivity(home);
                    finish();
                }

            }
        }, 3500);

    }
}
