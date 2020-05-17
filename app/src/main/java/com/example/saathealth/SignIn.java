package com.example.saathealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity implements SelectLangDialog.langSelectListener{

    TextView gotoSignup;
    Button reqOTp;
    EditText phone;
    ProgressBar progressBar;
    TextView heading,phoneHeading,newTo;
    SharedPreferences language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gotoSignup=findViewById(R.id.gotoSignUp);
        reqOTp=findViewById(R.id.reqOTP);
        phone=findViewById(R.id.phoneNumET);
        heading=findViewById(R.id.signinHeading);
        progressBar=findViewById(R.id.signin_progress);
        phoneHeading=findViewById(R.id.signin_phoneNum);
        newTo=findViewById(R.id.newTO);

        language=getSharedPreferences("language",MODE_PRIVATE);
        if(language.getString("lang","English").equals("Hindi")){
            heading.setText("लॉग इन करें");
            phoneHeading.setText("फ़ोन नंबर");
            newTo.setText("साथहेल्थ में नए?");
            reqOTp.setText("अनुरोध OTP");
            gotoSignup.setText("साइन अप करें!");
        }

        reqOTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().length()<10){
                    Toast.makeText(SignIn.this,"Enter a valid mobile number",Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    reqOTp.setVisibility(View.INVISIBLE);
                    final String userPhone=phone.getText().toString();

                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

                    Query findNum=reference.orderByChild("phone").equalTo(userPhone);

                    findNum.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent i=new Intent(SignIn.this,confirmOTP.class);
                                i.putExtra("phoneNumber",userPhone);
                                i.putExtra("sign","in");
                                startActivity(i);
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                reqOTp.setVisibility(View.VISIBLE);
                                Toast.makeText(SignIn.this,"User does not exists! Sign Up!",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });

        SelectLangDialog exampleDialog = new SelectLangDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    @Override
    public void applyTexts(String lang) {

        if(lang.equals("Hindi")){
            gotoSignup=findViewById(R.id.gotoSignUp);
            reqOTp=findViewById(R.id.reqOTP);
            heading=findViewById(R.id.signinHeading);
            phoneHeading=findViewById(R.id.signin_phoneNum);
            newTo=findViewById(R.id.newTO);
            heading.setText("लॉग इन करें");
            phoneHeading.setText("फ़ोन नंबर");
            newTo.setText("साथहेल्थ में नए?");
            reqOTp.setText("अनुरोध OTP");
            gotoSignup.setText("साइन अप करें!");
        }
        else{
            gotoSignup=findViewById(R.id.gotoSignUp);
            reqOTp=findViewById(R.id.reqOTP);
            heading=findViewById(R.id.signinHeading);
            phoneHeading=findViewById(R.id.signin_phoneNum);
            newTo=findViewById(R.id.newTO);
            heading.setText("LOGIN");
            phoneHeading.setText("Phome Number");
            newTo.setText("New to Saathealth?");
            reqOTp.setText("Request OTP");
            gotoSignup.setText("SignUP Here!");
        }
        language=getSharedPreferences("language",MODE_PRIVATE);
        SharedPreferences.Editor ed=language.edit();
        ed.putString("lang",lang);
        ed.apply();
    }
}
