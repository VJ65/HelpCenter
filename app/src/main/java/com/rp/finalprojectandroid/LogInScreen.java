package com.rp.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogInScreen extends AppCompatActivity {
    private Button normalUserLoginBtn, intentSignUpActivity, loginp;
    private EditText emailET, pwET;
    private TextView resetPwTVBtn;
    private FirebaseAuth objectFirebaseAuth;
    private ImageView doneanimation,logo;
    private Animation slideDownAnimation,slideAnimation,fadeOutAnim,fadeInAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        ConnectXML();


        loginp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginp.setAnimation (slideAnimation);
                loginp.setAnimation (fadeOutAnim);
                startActivity(new Intent(LogInScreen.this, LoginWithNumber.class));
            }
        });


    }

    private void ConnectXML() {



        doneanimation = findViewById(R.id.doneAnim);
        loginp = findViewById(R.id.logInPhn);
        logo=findViewById (R.id.logoIMG);
        normalUserLoginBtn = findViewById(R.id.loginUserBtn);
        intentSignUpActivity = findViewById(R.id.goToSignUpActivity);
        emailET = findViewById(R.id.emailETV);
        fadeOutAnim=AnimationUtils.loadAnimation (getApplicationContext (),R.anim.fade_out);
        slideDownAnimation= AnimationUtils.loadAnimation (getApplicationContext (),R.anim.slide_down);
        fadeInAnim=AnimationUtils.loadAnimation (getApplicationContext (),R.anim.fade_in);
        loginp.setAnimation (fadeInAnim);
        normalUserLoginBtn.setAnimation (fadeInAnim);
        slideAnimation=AnimationUtils.loadAnimation (getApplicationContext (),R.anim.move);
        logo.setAnimation (slideDownAnimation);
        pwET = findViewById(R.id.passwordETV);
        resetPwTVBtn = findViewById(R.id.resetPasswordButton);
        normalUserLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigIn();


            }
        });
        objectFirebaseAuth = FirebaseAuth.getInstance();
        /* ONCLICK RESET PASSWORD EMAIL BUTTON  RESET PASSWORD WITH FIREBASE */
        resetPwTVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInScreen.this, ResetPasswodActivity.class));
                finish();
            }
        });
        intentSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LogInScreen.this, SignUpScreen.class));

            }
        });

    }
    private  void sigIn(){

        if(emailET.getText ().toString ().isEmpty () && pwET.getText ().toString ().isEmpty ())
        {
            Toast.makeText (this, "Please Fill All Fields First", Toast.LENGTH_SHORT).show ();
        }else if(emailET.getText ().toString ().equals ("ADMIN") && pwET.getText ().toString ().equals ("18941")){

            startActivity(new Intent(LogInScreen.this, AdminActivity.class));

        }else if(pwET.getText ().toString ().isEmpty ()){
            Toast.makeText (this, "Please Fill your Password Field", Toast.LENGTH_SHORT).show ();
            
        }
        else if(emailET.getText ().toString ().isEmpty ()){
            Toast.makeText (this, "Please Fill your Email Field", Toast.LENGTH_SHORT).show ();

        }

        else {
            objectFirebaseAuth.signInWithEmailAndPassword(emailET.getText().toString(), pwET.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                emailET.setVisibility(View.INVISIBLE);
                                pwET.setVisibility(View.INVISIBLE);
                                doneanimation.setVisibility(View.VISIBLE);


                                Intent intent = new Intent(getApplicationContext(), SetUpUserProfile.class);
                                startActivity(intent);


                            } else {
                                emailET.setVisibility(View.VISIBLE);
                                pwET.setVisibility(View.VISIBLE);
                                Toast.makeText(LogInScreen.this, "LOGIN FAILED", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

        }
     }
}
