package com.rp.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private Button back,updateSection;

    private FirebaseAuth mAuth;
    private ImageView imageToDownloadIV;
    private TextView userBIOTV,userMailTV,userNameTV;

    private FirebaseFirestore objectFirebaseFirestore;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);
        connectXML ();
        Download ();
        OnclickFucntion ();
    }



    public void connectXML(){



        back=findViewById (R.id.done);
        updateSection=findViewById (R.id.updateProfile);
        userNameTV=findViewById (R.id.userNameHome);
        imageToDownloadIV=findViewById (R.id.userIVHome);
        userMailTV=findViewById (R.id.userNameHome);
        userBIOTV=findViewById (R.id.userBioHome);
        userMailTV=findViewById (R.id.userEmailHome);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();


        FirebaseUser user = mAuth.getInstance ().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
                userMailTV.setText (email);

        }
    }
    private void OnclickFucntion(){

        updateSection.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (HomeActivity.this, SetUpUserProfile.class));
            }
        });
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                mAuth.getInstance ().signOut ();
                Toast.makeText (HomeActivity.this, "Signed Out", Toast.LENGTH_SHORT).show ();
                startActivity (new Intent (HomeActivity.this, LogInScreen.class));

            }
        });


    }
    private void Download() {
        try {

            imageToDownloadIV.setImageResource(R.drawable.loading);
                objectFirebaseFirestore.collection("User")
                        .document(userMailTV.getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot> () {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {

                                    String url = documentSnapshot.getString("URL");
                                    String bio = documentSnapshot.getString ("BIO");
                                    String name = documentSnapshot.getString ("NAME");
                                    userBIOTV.setText (bio);
                                    userNameTV.setText (name);
                                    Glide.with(getApplicationContext ())
                                            .load(url)
                                            .into(imageToDownloadIV);

                                    Toast.makeText(getApplicationContext (), "Image Downloaded", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext (), "No Such File Exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext (), "Failed To Retrieve Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        } catch (Exception ex) {

            Toast.makeText(getApplicationContext (), "DownloadError: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
