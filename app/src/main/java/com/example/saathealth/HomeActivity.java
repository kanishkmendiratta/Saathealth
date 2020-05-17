package com.example.saathealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    String userPhone,userName,userLoc,userLanguage;
    TextView name,phone,loc;
    TextView nameTV,phoneTV,locTV;
    ProgressBar progressBar;
    ConstraintLayout layout;
    Button logout;
    SharedPreferences language;
    Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bundle=getIntent().getExtras();
        userPhone=bundle.getString("phoneNum");
        name=findViewById(R.id.home_nameTV);
        loc=findViewById(R.id.home_locationTV);
        phone=findViewById(R.id.home_phoneTV);
        nameTV=findViewById(R.id.home_name);
        phoneTV=findViewById(R.id.home_phone);
        locTV=findViewById(R.id.home_location);
        progressBar=findViewById(R.id.home_progress);
        layout=findViewById(R.id.homelayout);
        logout=findViewById(R.id.logOut);

        language=getSharedPreferences("language",MODE_PRIVATE);

        if(language.getString("lang","English").equals("Hindi")){
            nameTV.setText("नाम:");
            phoneTV.setText("फ़ोन नंबर:");
            locTV.setText("स्थान:");
            logout.setText("लॉग आउट");
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences user=getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor ed1=user.edit();
                ed1.putBoolean("isLogged",false);
                ed1.putString("userPhone",null);
                ed1.apply();
                finishAffinity();
                startActivity(new Intent(HomeActivity.this,SignIn.class));
            }
        });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        Query findNum=reference.orderByChild("phone").equalTo(userPhone);

        findNum.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    userName=dataSnapshot.child(userPhone).child("name").getValue(String.class);
                    userLoc=dataSnapshot.child(userPhone).child("location").getValue(String.class);

                    name.setText(userName);
                    phone.setText(userPhone);
                    loc.setText(userLoc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
