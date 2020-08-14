package com.rp.finalprojectandroid.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rp.finalprojectandroid.R;


public class HomeFragment extends Fragment {
    private RecyclerView rcv;
    private TextView aboutDeveloper;
    private ImageView logo,lottie;
    private Animation fadeIn;
    private FirebaseFirestore objectFirebaseAuth;

    private StatusAdapterClass objectStatusAdapter;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        aboutDeveloper=view.findViewById (R.id.developedByShaka);
        logo=view.findViewById (R.id.logoAbout);

        lottie=view.findViewById (R.id.editTextAnimationHome);
        fadeIn= AnimationUtils.loadAnimation (getActivity (),R.anim.fade_in);
        aboutDeveloper.setAnimation (fadeIn);
        logo.startAnimation (fadeIn);
        aboutDeveloper.startAnimation (fadeIn);
        objectFirebaseAuth=FirebaseFirestore.getInstance();
        rcv=view.findViewById(R.id.RV);



        addValuestoRV();



        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        objectStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectStatusAdapter.stopListening();
    }
    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Reports");
            FirestoreRecyclerOptions<StatusModelClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelClass>().setQuery(objectQuery, StatusModelClass.class).build();
            objectStatusAdapter = new StatusAdapterClass(options);
            //Toast.makeText (getActivity (), "Values Are Here", Toast.LENGTH_SHORT).show ();
            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcv.setAdapter(objectStatusAdapter);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
