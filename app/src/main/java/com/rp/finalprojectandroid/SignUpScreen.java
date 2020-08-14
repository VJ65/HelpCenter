package com.rp.finalprojectandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpScreen extends AppCompatActivity {
        private Button registerUserBtn,backBtn;
        private TextView resetPassword1;
        private EditText userEmail,userPassword;
        private ProgressBar progressBar1;
        private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        connectXML();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this, LogInScreen.class));
            }
        });
        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1.setVisibility (View.VISIBLE);
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar1.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpScreen.this, "User Registered" , Toast.LENGTH_SHORT).show();

                                 progressBar1.setVisibility(View.INVISIBLE);
                                 finish();
                                startActivity(new Intent(SignUpScreen.this, LogInScreen.class));

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpScreen.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignUpScreen.this, LogInScreen.class));
                                    finish();
                                }
                            }
                        });

            }
        });

    }
  private void connectXML(){
      registerUserBtn=findViewById(R.id.registerUser);
      backBtn=findViewById(R.id.backToLogin);
      userEmail=findViewById(R.id.emailETV);
      userPassword=findViewById(R.id.passwordETV);
      progressBar1=findViewById(R.id.progressBarSignUp);
      auth = FirebaseAuth.getInstance();
  }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar1.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton (android.R.string.yes, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        SignUpScreen.super.onBackPressed();
                    }

    }).create().show();
    }
}
