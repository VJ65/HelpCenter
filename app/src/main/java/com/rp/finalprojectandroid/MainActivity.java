package com.rp.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;
    private Animation animtoogather;
    private ImageView mainLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLogo=findViewById (R.id.logoMain);
        animtoogather = AnimationUtils.loadAnimation (getApplicationContext (),R.anim.fade_in);
        mainLogo.startAnimation(animtoogather);


        //After X time new activity start-------
        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //--------if no internet go on no net activity
                        if (!isOnline(MainActivity.this)) {
                            Intent intent = new Intent(MainActivity.this, NoInternet.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        //------if Internet Found Than Welcome----
                        else {
                            Intent intent = new Intent(MainActivity.this, LogInScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        //--------------------//
                    }
                }, 2200);//-----------Time Foe next Screen
    }

        private boolean isOnline (MainActivity mainActivity){
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

}
