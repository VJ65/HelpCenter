package com.rp.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rp.finalprojectandroid.Fragments.HomeFragment;
import com.rp.finalprojectandroid.Fragments.ProfileFragment;
import com.rp.finalprojectandroid.Fragments.ReportFragment;

import static com.rp.finalprojectandroid.R.color.colorPrimary;

public class NagivationView extends AppCompatActivity {
            BottomNavigationView objectBottomNavigationView;
            ProfileFragment objectProfileFragment;
            ReportFragment objectReportFragment;
            HomeFragment objectHomeFragment;
    public static Context contextOfApplication;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_nagivation_view);
        contextOfApplication = getApplicationContext();

        try {

    objectHomeFragment=new HomeFragment ();
    objectProfileFragment=new ProfileFragment ();
    objectReportFragment=new ReportFragment ();
    changeFragment (objectReportFragment);

    objectBottomNavigationView=findViewById (R.id.BNV);
    objectBottomNavigationView.setItemIconTintList(null);
    objectBottomNavigationView.setBackgroundColor (Color.WHITE);
    objectBottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId ()){

                case  R.id.homeitem:
                  //  objectBottomNavigationView.setBackgroundColor (Color.BLUE);
                    changeFragment (objectHomeFragment);
                    return  true;
                case  R.id.profileItem:
                    changeFragment (objectProfileFragment);
                //    objectBottomNavigationView.setBackgroundColor (Color.GRAY);
                    return  true;
                case  R.id.issueItem:
                    changeFragment (objectReportFragment);
                //    objectBottomNavigationView.setBackgroundColor (Color.GREEN);
                    return  true;
                default:
                    return  false;



            }

        }
    });

}catch (Exception e ){
    Toast.makeText (this, e.getMessage (), Toast.LENGTH_SHORT).show ();
}

    }

    private void changeFragment(Fragment object){
        try {

            FragmentTransaction objectFragmentTransaction = getSupportFragmentManager ().beginTransaction ();
            objectFragmentTransaction.replace (R.id.FL,object);
            objectFragmentTransaction.commit ();


        }catch (Exception e ){
            Toast.makeText (this, e.getMessage (), Toast.LENGTH_SHORT).show ();
        }



    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
