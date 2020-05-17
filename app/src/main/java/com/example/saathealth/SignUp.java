package com.example.saathealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
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

public class SignUp extends AppCompatActivity {

    EditText name,phone,location;
    CheckBox tncAccept;
    Button register;
    TextView tnc,privacy;
    ProgressBar progressBar;
    TextView and,of;
    TextView nameTV,phoneTV,LocationTV,heading;
    SharedPreferences language;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.nameET);
        phone=findViewById(R.id.phoneET);
        tnc=findViewById(R.id.tncs);
        privacy=findViewById(R.id.privacy);
        location=findViewById(R.id.locationET);
        tncAccept=findViewById(R.id.checkBox);
        register=findViewById(R.id.gotoOTP);
        and=findViewById(R.id.su_and);
        of=findViewById(R.id.of);
        progressBar=findViewById(R.id.signup_progress);
        heading=findViewById(R.id.signupHeading);
        nameTV=findViewById(R.id.su_name);
        phoneTV=findViewById(R.id.su_phn);
        LocationTV=findViewById(R.id.su_loc);

        language=getSharedPreferences("language",MODE_PRIVATE);
        if(language.getString("lang","English").equals("Hindi")){
            tncAccept.setText("मैं साथहेल्थ की ");
            and.setText(" और");
            of.setText(" से सहमत हूँ");
            heading.setText("साइन अप करें");
            nameTV.setText("नाम");
            phoneTV.setText("फ़ोन नंबर");
            LocationTV.setText("स्थान");
            register.setText("रजिस्टर करें");
        }

        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);

                WebView wv = new WebView(SignUp.this);
                wv.loadUrl("https://www.saathealth.com/terms-and-conditions");

                ProgressDialog pd=new ProgressDialog(wv.getContext());
                pd.setMessage("Loading...");
                wv.setWebViewClient(new Mybrowser(pd,alert));
                wv.getSettings().setLoadsImagesAutomatically(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);

                WebView wv = new WebView(SignUp.this);
                wv.loadUrl("https://www.saathealth.com/privacy-policy");
                ProgressDialog pd=new ProgressDialog(wv.getContext());
                pd.setMessage("Loading...");
                wv.setWebViewClient(new Mybrowser(pd,alert));
                wv.getSettings().setLoadsImagesAutomatically(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || location.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this,"All fields are necessary",Toast.LENGTH_LONG).show();
                }
                else{
                    if(tncAccept.isChecked()){
                        if(phone.getText().toString().length()<10){
                            Toast.makeText(SignUp.this,"Please enter a valid mobile number",Toast.LENGTH_LONG).show();
                        }
                        else{
                            register.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            rootNode=FirebaseDatabase.getInstance();
                            reference=rootNode.getReference("Users");

                            String username=name.getText().toString();
                            String userPhone=phone.getText().toString();
                            String userLoc=location.getText().toString();

                            Query findNum=reference.orderByChild("phone").equalTo(userPhone);

                            findNum.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        register.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignUp.this,"Phone number already exists! Sign In please!",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        String username=name.getText().toString();
                                        String userPhone=phone.getText().toString();
                                        String userLoc=location.getText().toString();
                                        Intent i=new Intent(SignUp.this,confirmOTP.class);
                                        i.putExtra("phoneNumber",userPhone);
                                        i.putExtra("username",username);
                                        i.putExtra("userLoc",userLoc);
                                        i.putExtra("sign","up");
                                        startActivity(i);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    register.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                    else{
                        Toast.makeText(SignUp.this,"Please agree to the T&Cs and Private Statement",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public class Mybrowser extends WebViewClient {
        ProgressDialog pd;
        AlertDialog.Builder alert;
        public Mybrowser(ProgressDialog pd,AlertDialog.Builder alert) {
            this.pd = pd;
            this.alert=alert;
            pd.show();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (pd.isShowing()) {
                pd.dismiss();
                alert.show();
            }
        }
        @Override
        public void onReceivedError(WebView view,int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(getApplicationContext(),
                    "Error:" + description, Toast.LENGTH_SHORT).show();
        }
    }
}
