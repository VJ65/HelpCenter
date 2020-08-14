package com.rp.finalprojectandroid.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.$Gson$Preconditions;
import com.rp.finalprojectandroid.LogInScreen;
import com.rp.finalprojectandroid.MainActivity;
import com.rp.finalprojectandroid.NagivationView;
import com.rp.finalprojectandroid.R;
import com.rp.finalprojectandroid.SetUpUserProfile;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ReportFragment extends Fragment  {

    private Button sendIssue,signOut;
    private ImageView imageToUploadIV;
    private EditText studentName, studentEmail, studentIssue;
    private ImageView loading;
    private Animation fadeIn,fadeOut,swipeUp;

     FirebaseFirestore objectFirebaseFirestore;  private static final String Student_ID="";
    private static final String Student_Name="Student_name";
    private static final String CollectionName="Reports";
    private static final String Student_Rollno="student_email";
    private String emailCheck,nameCheck;

    private static final String Student_Issue="student_issue";
    FirebaseAuth mAuth;
    private Animation slideDownAnimation, fadeOutAnim;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_report, container, false);


      sendIssue=view.findViewById (R.id.reportIssueBtn);
        imageToUploadIV = view.findViewById (R.id.addImage);
        signOut=view.findViewById (R.id.backToLoginScreenButton);
        studentName = view.findViewById (R.id.reportIssueNameEV);
        studentEmail = view.findViewById (R.id.reportIssueEmailEV);
        fadeIn= AnimationUtils.loadAnimation (getActivity (),R.anim.fade_in);
        fadeOut=AnimationUtils.loadAnimation (getActivity (),R.anim.fade_out);
        swipeUp=AnimationUtils.loadAnimation (getActivity (),R.anim.slide_up);
        studentIssue = view.findViewById (R.id.reportIssueEV);

        loading = view.findViewById (R.id.loadTextAnimation);
        objectFirebaseFirestore = FirebaseFirestore.getInstance ();

        FirebaseUser user = mAuth.getInstance ().getCurrentUser ();

        if (user != null) {

            emailCheck = user.getEmail ();

            if (emailCheck != null) {
                studentEmail.setText (emailCheck);

                //Toast.makeText(SetUpUserProfile.this, "User is "+email, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText (getActivity (), "No User", Toast.LENGTH_SHORT).show ();
        }
        try {


            objectFirebaseFirestore.collection("User")
                    .document(studentEmail.getText ().toString ())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot> () {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() ) {

                                 nameCheck = documentSnapshot.getString ("NAME");

                                studentName.setText (nameCheck);

                                //  Toast.makeText(getActivity (), "Image Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                 Toast.makeText(getActivity (), "No Such profile Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Toast.makeText(getActivity (), "Failed To Retrieve Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ex) {

            //Toast.makeText(getActivity (), "DownloadError: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
            sendIssue.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    addValues ();

                    loading.setVisibility (View.VISIBLE);
                }
            });

        signOut.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                mAuth.getInstance ().signOut ();
                Toast.makeText (getActivity (), "Signed Out", Toast.LENGTH_SHORT).show ();
                getActivity ().finish ();
                startActivity (new Intent (getActivity (), LogInScreen.class));
            }
        });

        return view;

    }

    public void addValues() {

        try {
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseFirestore.collection("CollectionName").document(studentName.getText().toString()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if (task.getResult().exists()) {
                                Toast.makeText(getActivity (), "You Have Already Created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(!studentEmail.getText().toString().isEmpty() && !studentName.getText().toString().isEmpty() && !studentIssue.getText().toString().isEmpty()
                               && studentName.getText ().toString ().equals (nameCheck)) {


                                    Map<String,Object> objMap=new HashMap<>();
                                    objMap.put(Student_Name, studentName.getText().toString());
                                    objMap.put(Student_Rollno, studentEmail.getText().toString());
                                    objMap.put(Student_Issue, studentIssue.getText().toString());
                                        objMap.put("TimeofIssue", FieldValue.serverTimestamp ());

                                    objectFirebaseFirestore.collection(CollectionName)
                                            .document(studentName.getText().toString()).set(objMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    studentName.startAnimation (swipeUp);
                                                    studentEmail.startAnimation (swipeUp);
                                                    studentIssue.startAnimation (swipeUp);
                                                    imageToUploadIV.setImageResource (R.drawable.electronics);
                                                    loading.setVisibility (View.INVISIBLE);

                                                    Toast.makeText(getActivity (), "ISSUE REPORTED", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(getActivity (), "Fails to add data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    loading.setVisibility (View.INVISIBLE);
                                    Toast.makeText(getActivity (), "Please enter valid details", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

        }
        catch (Exception e)
        {

            Toast.makeText(getActivity (), "Add Values"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }



}