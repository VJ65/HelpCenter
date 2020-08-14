package com.rp.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginWithNumber extends AppCompatActivity {
            private EditText emailET,pwET;
            private Button getNumberBtn,verifyBtn;
            private ImageView backToHome;

    private ImageView doneanimation,loadingAnimationNumber;


    private String mVerificationId;
    //firebase auth object
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_with_number);
        loadingAnimationNumber=findViewById (R.id.loadTextAnimationNumber);
        doneanimation=findViewById(R.id.doneAnim);
        emailET=findViewById(R.id.edit_username);
        backToHome=findViewById(R.id.backToLogin1);
        pwET=findViewById(R.id.edit_password);
        getNumberBtn=findViewById(R.id.getVerificationCodeBtn);
        verifyBtn=findViewById(R.id.signInVerificationCode);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginWithNumber.this, LogInScreen.class));
            }
        });

getNumberBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String vcode=emailET.getText().toString();
        sendVerificationCode(vcode);
        loadingAnimationNumber.setVisibility (View.VISIBLE);
        Toast.makeText(LoginWithNumber.this, "Code Sent ", Toast.LENGTH_SHORT).show();
    }
});
verifyBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String code = emailET.getText().toString().trim();
        if (code.isEmpty() || code.length() < 6) {
            pwET.setError("Enter valid code");
            pwET.requestFocus();
            return;
        }

        //verifying the code entered manually
        verifyVerificationCode(code);
    }
});



    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                pwET.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginWithNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        Toast.makeText(this, "Code Verified", Toast.LENGTH_LONG).show();

        loadingAnimationNumber.setVisibility (View.INVISIBLE);
        doneanimation.setVisibility(View.VISIBLE);
        finish ();
        startActivity(new Intent(LoginWithNumber.this, SetUpUserProfile.class));
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginWithNumber.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity

                            startActivity(new Intent(LoginWithNumber.this, SetUpUserProfile.class));
                            Toast.makeText(LoginWithNumber.this, "LogInSuccessFull", Toast.LENGTH_SHORT).show();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}
