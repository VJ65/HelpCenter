package com.rp.finalprojectandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rp.finalprojectandroid.DeleteUser;
import com.rp.finalprojectandroid.HomeActivity;
import com.rp.finalprojectandroid.LogInScreen;
import com.rp.finalprojectandroid.MainActivity;
import com.rp.finalprojectandroid.R;
import com.rp.finalprojectandroid.SetUpUserProfile;


public class ProfileFragment extends Fragment {

    private Button back,updateSection,deleteProfile,gettingmydata,movingToReport;
    FirebaseAuth mAuth;
     private ImageView imageToDownloadIV;
     private TextView userBIOTV,userMailTV,userNameTV,reportContentProfile,reportTimeProfile;
     FirebaseFirestore objectFirebaseFireStore;
     FirebaseFirestore obj;
     private String bio,name,email;
     private Animation fadeIn;
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        gettingmydata=view.findViewById (R.id.getData);
        movingToReport=view.findViewById (R.id.moveToReport);

        reportContentProfile=view.findViewById (R.id.lastReportContent);
        reportTimeProfile=view.findViewById (R.id.lastReportTime);
        fadeIn = AnimationUtils.loadAnimation (getActivity (),R.anim.fade_in);
        deleteProfile=view.findViewById (R.id.deleteProfileFragment);
        back=view.findViewById (R.id.doneWithFragmentProfile);
        updateSection=view.findViewById (R.id.updateProfileFragment);
        userNameTV=view.findViewById (R.id.userNameProfile);
        imageToDownloadIV=view.findViewById (R.id.userIVProfile);
        userMailTV=view.findViewById (R.id.userEmailProfile);
        userBIOTV=view.findViewById (R.id.userBioProfile);
        imageToDownloadIV.startAnimation(fadeIn);
        userMailTV.startAnimation(fadeIn);
        userNameTV.startAnimation(fadeIn);
        userBIOTV.startAnimation(fadeIn);
        reportContentProfile.setAnimation (fadeIn);
        reportTimeProfile.setAnimation (fadeIn);
        objectFirebaseFireStore = FirebaseFirestore.getInstance();
        obj=FirebaseFirestore.getInstance();
        /*GETTING PROFILE INFORMATION START*/
        try {
            FirebaseUser user = mAuth.getInstance ().getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                userMailTV.setText (email);
             //   Toast.makeText (getActivity (), "Yes "+email, Toast.LENGTH_SHORT).show ();

            }
            imageToDownloadIV.setImageResource(R.drawable.loading);
            objectFirebaseFireStore.collection("User")
                    .document(userMailTV.getText ().toString ())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot> () {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() ) {

                                String url = documentSnapshot.getString("URL");
                                 bio = documentSnapshot.getString ("BIO");
                                 name = documentSnapshot.getString ("NAME");
                                 email = documentSnapshot.getString ("EMAIL");
                                userBIOTV.setText (bio);
                                userNameTV.setText (name);
                                userMailTV.setText (email);
                                Glide.with(getActivity ())
                                        .load(url)
                                        .into(imageToDownloadIV);
 /*  NotificationCompat.Builder builder= new NotificationCompat.Builder (getActivity (),"welcome");
   builder.setSmallIcon (R.drawable.lolo);
   builder.setContentTitle ("Welcome To Help Desk");
   builder.setContentText (name);
   builder.setPriority (NotificationCompat.PRIORITY_DEFAULT);

   NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from (getActivity ());
   notificationManagerCompat.notify (001,builder.build ());*/

                                //  Toast.makeText(getActivity (), "Image Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                //  Toast.makeText(getActivity (), "No Such File Exists", Toast.LENGTH_SHORT).show();
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
        /*GETTING PROFILE INFORMATION END */


        /* GETTING LAST SENT ISSUE INFORMATION START */
        try {

            obj.collection("Reports")
                    .document(name)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot> () {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() ) {

                                String content = documentSnapshot.getString("student_issue");
                                //String time = documentSnapshot.getTimestamp ("TimeofIssue");
                                //String name = documentSnapshot.getString ("Student_name");

                                reportContentProfile.setText (content);
                                //   reportTimeProfile.setText (time);

                                //  Toast.makeText(getActivity (), "Image Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                //  Toast.makeText(getActivity (), "No Such File Exists", Toast.LENGTH_SHORT).show();
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




        /* GETTING LAST SENT ISSUE INFORMATION START */
        /*---------------------------------------------------------------*/


        /*ON CLICK LISTENERS  START*/
        movingToReport.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                FragmentTransaction fr = getFragmentManager ().beginTransaction ();
                fr.replace (R.id.FL,new ReportFragment ());
                fr.commit ();
            }
        });

        gettingmydata.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                try {
                    GetData ();
                }catch (Exception e ){
                    Toast.makeText (getActivity (), e.getMessage (), Toast.LENGTH_SHORT).show ();
                }

            }
        });

        deleteProfile.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (getActivity (), DeleteUser.class));
            }
        });
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                mAuth.getInstance ().signOut ();
                Toast.makeText (getActivity (), "Signed Out", Toast.LENGTH_SHORT).show ();
                startActivity (new Intent (getActivity (), LogInScreen.class));
            }


        });
        updateSection.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (getActivity (), SetUpUserProfile.class));
            }
        });
        /*ON CLICK LISTENERS  END*/
        return view;
    }
    public void GetData(){
        try {

            obj.collection("Reports")
                    .document(name)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot> () {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() ) {

                                String content = documentSnapshot.getString("student_issue");
                                //String time = documentSnapshot.getTimestamp ("TimeofIssue");
                                //String name = documentSnapshot.getString ("Student_name");

                                reportContentProfile.setText (content);
                             //   reportTimeProfile.setText (time);

                                //  Toast.makeText(getActivity (), "Image Downloaded", Toast.LENGTH_SHORT).show();

                            } else {
                                //  Toast.makeText(getActivity (), "No Such File Exists", Toast.LENGTH_SHORT).show();
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
    }


}
