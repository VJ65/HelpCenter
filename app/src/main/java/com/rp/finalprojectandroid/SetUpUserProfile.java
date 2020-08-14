package com.rp.finalprojectandroid;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rp.finalprojectandroid.Fragments.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetUpUserProfile extends AppCompatActivity {
    private Button backtologin, registrationBtn,skipq1;
    private ImageView imageToUploadIV, signoutAnimation, progressBar;
    FirebaseAuth mAuth;
    private Uri imageDataInUriForm;
    private StorageReference objectStorageReference;
    private static final int REQUEST_CODE = 124;
    private boolean yesImageIsHere=false;
    private FirebaseFirestore objectFirebaseFirestore;
    private boolean isImageSelected = false;
    private TextView waitingText;
    private RelativeLayout mainLayout;
    private Animation slideDownAnimation,fadeOutAnim;
    ProgressBar bar;
    private EditText userNameET, fullNameET, userBioET, userPassword;

    @Override

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_set_up_user_profile);
        ConnectXML (); //ismy sary variable button wagyra initialization hain
        OnclickRunning (); //onClick methods hain ismy

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                imageDataInUriForm = data.getData ();
                Bitmap objectBitmap;

                objectBitmap = MediaStore.Images.Media.getBitmap (getContentResolver (), imageDataInUriForm);
                imageToUploadIV.setImageBitmap (objectBitmap);

                isImageSelected = true;

            } else if (requestCode != REQUEST_CODE) {
                Toast.makeText (this, "Request code doesn't match", Toast.LENGTH_SHORT).show ();
            } else if (resultCode != RESULT_OK) {
                Toast.makeText (this, "Fails to get image", Toast.LENGTH_SHORT).show ();
            } else if (data == null) {
                Toast.makeText (this, "No Image Was Selected", Toast.LENGTH_SHORT).show ();
            }


        } catch (Exception e) {
            Toast.makeText (this, "onActivityResult:" + e.getMessage (), Toast.LENGTH_SHORT).show ();
        }
    }

    private void ConnectXML () {
        objectFirebaseFirestore = FirebaseFirestore.getInstance ();

        objectStorageReference = FirebaseStorage.getInstance ().getReference ("Users");
        skipq1=findViewById (R.id.skip);
        mainLayout = findViewById (R.id.setUpUserProfileLayout);
        backtologin = findViewById (R.id.backToLogin2);
        registrationBtn = findViewById (R.id.doneSetup);
        signoutAnimation = findViewById (R.id.signOutAnim);
        userPassword = findViewById (R.id.passwordETV);
        userNameET = findViewById (R.id.etUsername);
        fullNameET = findViewById (R.id.etFullName);
        bar=findViewById (R.id.ProgressBar);
        userBioET = findViewById (R.id.userBio);
        imageToUploadIV = findViewById (R.id.userIV);
        slideDownAnimation= AnimationUtils.loadAnimation (getApplicationContext (),R.anim.slide_up);
        fadeOutAnim= AnimationUtils.loadAnimation (getApplicationContext (),R.anim.fade_out);



/*       User ka name lyraha hai Authentication say   */
        FirebaseUser user = mAuth.getInstance ().getCurrentUser ();
        if (user != null) {

            String email = user.getEmail ();

            if (email != null) {
                userNameET.setText (email);
                Snackbar snackbar = Snackbar
                        .make (mainLayout, "Welcome " + email, Snackbar.LENGTH_LONG);

                snackbar.show ();
                //Toast.makeText(SetUpUserProfile.this, "User is "+email, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText (SetUpUserProfile.this, "Anonymous User", Toast.LENGTH_SHORT).show ();
        }
        /*       User ka name lyraha hai Authentication say END  */

    }
    private void OnclickRunning(){
        skipq1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                startActivity (new Intent (SetUpUserProfile.this, NagivationView.class));
            }
        });
        backtologin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                mAuth.getInstance ().signOut ();
                signoutAnimation.setVisibility (View.VISIBLE);
                finish ();
                Toast.makeText (SetUpUserProfile.this, "Signed Out", Toast.LENGTH_SHORT).show ();
                startActivity (new Intent (SetUpUserProfile.this, LogInScreen.class));

            }
        });
        imageToUploadIV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                openGallery ();

            }
        });
        registrationBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {




                new AlertDialog.Builder(SetUpUserProfile.this)
                        .setTitle("Update Profile")
                        .setMessage("Are you sure you to update?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton (android.R.string.yes, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                uploadOurImage ();
                            }

                        }).create().show();
            }
        });


    }
    private void uploadOurImage () {
        try {
            if (imageDataInUriForm != null && !userNameET.getText ().toString ().isEmpty ()
                    && isImageSelected) {
                bar.setVisibility (View.VISIBLE);
                //yourName.jpeg
                String imageName = userNameET.getText ().toString () + "." + getExtension (imageDataInUriForm);
                final String Caption = userBioET.getText ().toString ();

                //FirebaseStorage/BSCSAImagesFolder/yourName.jpeg
                final StorageReference actualImageRef = objectStorageReference.child (imageName);

                UploadTask uploadTask = actualImageRef.putFile (imageDataInUriForm);
                uploadTask.continueWithTask (new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                    @Override
                    public Task<Uri> then (@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful ()) {
                            throw task.getException ();
                        }
                        return actualImageRef.getDownloadUrl ();
                    }
                }).addOnCompleteListener (new OnCompleteListener<Uri> () {
                    @Override
                    public void onComplete (@NonNull Task<Uri> task) {
                        if (task.isSuccessful ()) {
                            String url = task.getResult ().toString ();
                            Map<String, Object> objectMap = new HashMap<> ();
                            objectMap.put ("URL", url);
                            objectMap.put ("NAME",fullNameET.getText ().toString ());
                            objectMap.put ("BIO", Caption);
                            objectMap.put ("EMAIL",userNameET.getText ().toString ());
                            objectMap.put ("Server Time Stamp", FieldValue.serverTimestamp ());
                            objectFirebaseFirestore.collection ("User")
                                    .document (userNameET.getText ().toString ())
                                    .set (objectMap)
                                    .addOnFailureListener (new OnFailureListener () {
                                        @Override
                                        public void onFailure (@NonNull Exception e) {

                                            Toast.makeText (SetUpUserProfile.this, "User Profile Setup Failed  " + e.getMessage (), Toast.LENGTH_SHORT).show ();
                                        }
                                    })
                                    .addOnSuccessListener (new OnSuccessListener<Void> () {
                                        @Override
                                        public void onSuccess (Void aVoid) {
                                            userNameET.setText ("");
                                            userBioET.setText ("");
                                            bar.setAnimation (fadeOutAnim);
                                            bar.setVisibility (View.INVISIBLE);
                                            signoutAnimation.setVisibility (View.VISIBLE);

                                            imageToUploadIV.setAnimation (slideDownAnimation);
                                            startActivity (new Intent (SetUpUserProfile.this, NagivationView.class));
                                      /*      Snackbar snackbar = Snackbar
                                                    .make (mainLayout, "Profile Setup Done" , Snackbar.LENGTH_LONG);

                                            snackbar.show ();
                                        */
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener (new OnFailureListener () {
                    @Override
                    public void onFailure (@NonNull Exception e) {

                        Toast.makeText (getApplicationContext (), "Fails To Upload Image: " + e.getMessage (), Toast.LENGTH_SHORT).show ();
                    }
                });
            } else if (imageDataInUriForm == null) {

                Toast.makeText (getApplicationContext (), "No Image Is Selected", Toast.LENGTH_SHORT).show ();
            } else if (userNameET.getText ().toString ().isEmpty ()) {
                Toast.makeText (getApplicationContext (), "Please First You Need To Enter An Image Name", Toast.LENGTH_SHORT).show ();
                userNameET.requestFocus ();
            } else if (!isImageSelected) {

                Toast.makeText (getApplicationContext (), "Please Select An Image", Toast.LENGTH_SHORT).show ();
            }
        } catch (Exception e) {

            Toast.makeText (getApplicationContext (), "uploadOurImage:" + e.getMessage (), Toast.LENGTH_SHORT).show ();
        }
    }

    private String getExtension (Uri imageDataInUriForm) {
        try {
            ContentResolver objectContentResolver = getContentResolver (); //allows the UI to continue to be available to the user while the query is running
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton (); //some MIME types map to multiple extensions

            String extension = objectMimeTypeMap.getExtensionFromMimeType (objectContentResolver.getType (imageDataInUriForm));
            return extension;
        } catch (Exception e) {
            Toast.makeText (this, "getExtension:" + e.getMessage (), Toast.LENGTH_SHORT).show ();
        }

        return "";
    }


    private void openGallery () {
            try {
                Intent objectIntent = new Intent (); //Step 1:create the object of intent
                objectIntent.setAction (Intent.ACTION_GET_CONTENT); //Step 2: You want to get some data


                objectIntent.setType ("image/*");//Step 3: Images of all type
                startActivityForResult (objectIntent, REQUEST_CODE);

            } catch (Exception e) {
                Toast.makeText (getApplicationContext (), "openGallery:" + e.getMessage (), Toast.LENGTH_SHORT).show ();
            }
    }




}

