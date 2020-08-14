package com.rp.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoFromAdmin extends AppCompatActivity {
    public TextView nameTVINFO,emailTVINFO,issueTVINFO;
    private Animation fadeIn,slideDown;
    private Button sent1,back1;
    private EditText emailAdmin,subjectAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_from_admin);

//        nameTVINFO=findViewById (R.id.userInfoStNameTextViewTV);
        nameTVINFO=findViewById (R.id.userInfoStNameTV);
        back1=findViewById (R.id.backFromFeedbackScreen);
        emailTVINFO=findViewById (R.id.userInfoStEmailTV);
        issueTVINFO=findViewById (R.id.userInfoStIssueTV);
        emailAdmin=findViewById (R.id.issueEmailAdminClick);
      //  searchBar=findViewById(R.id.searchView);
        subjectAdmin=findViewById (R.id.issueSubjectAdminClick);
        sent1=findViewById (R.id.oksendMainClick);
        fadeIn = AnimationUtils.loadAnimation (UserInfoFromAdmin.this,R.anim.fade_in);
        slideDown = AnimationUtils.loadAnimation (UserInfoFromAdmin.this,R.anim.slide_down);
        nameTVINFO.startAnimation (fadeIn);
        emailTVINFO.startAnimation (fadeIn);
        issueTVINFO.startAnimation (fadeIn);


        back1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                startActivity (new Intent (UserInfoFromAdmin.this, AdminActivity.class));

            }
        });

        sent1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                sendMail ();
                NotificationCompat.Builder builder=new NotificationCompat.Builder (UserInfoFromAdmin.this,"Feedback")
                        .setContentText ("Help Desk")
                        .setSmallIcon (R.drawable.lolo)
                        .setAutoCancel (true)
                        .setContentText ("Feedback Sent");

                NotificationManagerCompat managerCompat=NotificationManagerCompat.from (UserInfoFromAdmin.this);
                managerCompat.notify (999,builder.build ());

            }
        });
        //Reading data from activity
        Intent intent = getIntent();

        String name1 = intent.getStringExtra("name");
        nameTVINFO.setText (name1);;

        String email1 = intent.getStringExtra("email");
        emailTVINFO.setText (email1);
        emailAdmin.setText (email1);

//        random_recycle_name= intent.getStringExtra("recycle");
        String issue1 = intent.getStringExtra("issue");


        issueTVINFO.setText (issue1);


        //Toast.makeText(this, "------------\n"+name+"\n"+issue+"\n"+email+"\n------------", Toast.LENGTH_LONG).show();

    }

    private void sendMail() {
        String recipientList = emailAdmin.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject2 = "Issue FeedBack";
        String message = subjectAdmin.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject2);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

}
