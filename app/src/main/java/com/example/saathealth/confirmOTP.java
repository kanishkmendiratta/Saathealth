package com.example.saathealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class confirmOTP extends AppCompatActivity {

    Bundle bundle=new Bundle();
    String verificationCodeBySystem;
    Button confirm;
    TextView heading;
    ProgressBar progressBar;
    String userPhone,userName,userLoc,signMethod;
    EditText otpET;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    SharedPreferences language,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_o_t_p);
        confirm=findViewById(R.id.confirm);
        heading=findViewById(R.id.conf_heading);
        otpET=findViewById(R.id.otpET);
        progressBar=findViewById(R.id.otp_progress);

        bundle=getIntent().getExtras();
        userPhone=bundle.getString("phoneNumber");
        signMethod=bundle.getString("sign");
        if(signMethod.equals("up")){
            userName=bundle.getString("username");
            userLoc=bundle.getString("userLoc");
        }

        sendOtpToUser(userPhone);

        language=getSharedPreferences("language",MODE_PRIVATE);
        if(language.getString("lang","English").equals("Hindi")){
            confirm.setText("पुष्टि करें");
            heading.setText("आपको भेजा गया ओटीपी दर्ज करें");
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpET.getText().toString().equals(null) || otpET.getText().toString().length()!=6){
                    Toast.makeText(confirmOTP.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                    verifyCode(otpET.getText().toString());
                }
                //startActivity(new Intent(confirmOTP.this,HomeActivity.class));
            }
        });
    }

    public void sendOtpToUser(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(confirmOTP.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    public void verifyCode(String codeByUser){
        PhoneAuthCredential  credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInByCredentials(credential);
    }

    public void signInByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(confirmOTP.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    rootNode=FirebaseDatabase.getInstance();
                    reference=rootNode.getReference("Users");
                    user=getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor ed=user.edit();
                    ed.putBoolean("isLogged",true);
                    ed.putString("userPhone",userPhone);
                    ed.apply();
                    if(signMethod.equals("in")){
                        reference.child(userPhone).child("language").setValue(language.getString("lang","English"));
                    }
                    else{
                        UserDatabase ud=new UserDatabase(userName,userPhone,language.getString("lang","English"),userLoc);
                        reference.child(userPhone).setValue(ud);
                    }
                    Intent i=new Intent(confirmOTP.this,HomeActivity.class);
                    i.putExtra("phoneNum",userPhone);
                    finishAffinity();
                    startActivity(i);
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    Toast.makeText(confirmOTP.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
