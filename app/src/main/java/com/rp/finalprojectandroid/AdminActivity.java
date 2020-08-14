package com.rp.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rp.finalprojectandroid.Fragments.StatusAdapterClass;
import com.rp.finalprojectandroid.Fragments.StatusModelClass;

public class AdminActivity extends AppCompatActivity {
    private FirebaseFirestore objectFirebaseAuth;
    SearchView searchBar;
     RecyclerView rcv;
     AdapterClass objectAdapter;
     private Button back;

    private EditText email,subject;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin);

        //--------------//

        searchBar=findViewById(R.id.searchView);
        objectFirebaseAuth=FirebaseFirestore.getInstance();
        rcv=findViewById(R.id.RVA);
        back=findViewById (R.id.donewithAdmin);
        addValuestoRV1 ("");
        //--------------------------//


        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (AdminActivity.this, LogInScreen.class));
                finish ();

            }
        });










        //---------------------//
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                objectAdapter.stopListening();
                addValuestoRV1(query);
                objectAdapter.startListening();


//                serachIt=query.toLowerCase();
//                adNo=0;
//                resultLoad=0;
//                onStart();
//                if (beta.equals("on")){
//                    Toast.makeText(Item_Detail.this, "Searching Beta Version\nMay Contain Error", Toast.LENGTH_SHORT).show();}
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    objectAdapter.stopListening();
                    addValuestoRV1("");
                    objectAdapter.startListening();

                }

                return false;
            }
        });
        //----------------------------//




    }

    @Override
    protected void onStart () {
        super.onStart ();
        objectAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectAdapter.stopListening();
    }
    private void addValuestoRV1(String q) {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Reports");
            FirestoreRecyclerOptions<ModelClass> options =
                    new FirestoreRecyclerOptions.Builder<ModelClass>().setQuery(objectQuery, ModelClass.class).build();

            objectAdapter = new AdapterClass (options,q,getApplicationContext());
           rcv.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
            rcv.setAdapter(objectAdapter);
        } catch (Exception ex) {
            Toast.makeText(AdminActivity.this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}
