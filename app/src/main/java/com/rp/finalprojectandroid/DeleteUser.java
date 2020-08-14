package com.rp.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.auth.FirebaseAuth.*;

public class DeleteUser extends AppCompatActivity {
    private EditText email, password;
    private Button delete, backtoLoginBtn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_delete_user);
        connectXML ();

    }



    private void connectXML () {
        email = findViewById (R.id.deleteETV);
        password = findViewById (R.id.deletepasswordETV);
        backtoLoginBtn = findViewById (R.id.backToLoginDelete);
        delete = findViewById (R.id.deleteUser);
        backtoLoginBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                startActivity (new Intent (DeleteUser.this, NagivationView.class));
            }
        });
        delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final FirebaseUser user = getInstance ().getCurrentUser ();
                String emailUser = user.getEmail ();
                email.setText (emailUser);
                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential (email.getText ().toString (), password.getText ().toString ());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate (credential)
                        .addOnCompleteListener (new OnCompleteListener<Void> () {
                            @Override
                            public void onComplete (@NonNull Task<Void> task) {
                                user.delete ()
                                        .addOnCompleteListener (new OnCompleteListener<Void> () {
                                            @Override
                                            public void onComplete (@NonNull Task<Void> task) {
                                                if (task.isSuccessful ()) {
                                                    Toast.makeText (DeleteUser.this, " User Deactivated", Toast.LENGTH_SHORT).show ();
                                                    startActivity (new Intent (DeleteUser.this, LogInScreen.class));
                                                    finish ();
                                                }
                                            }
                                        }).addOnFailureListener (new OnFailureListener () {
                                    @Override
                                    public void onFailure (@NonNull Exception e) {
                                        Toast.makeText (DeleteUser.this, "Failed To Delete ", Toast.LENGTH_SHORT).show ();
                                    }
                                });
                            }
                        });
            }
        });

    }


}
