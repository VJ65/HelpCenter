package com.rp.finalprojectandroid.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.rp.finalprojectandroid.R;

public class StatusAdapterClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterClass.ViewHolder> {
    public StatusAdapterClass (@NonNull FirestoreRecyclerOptions<StatusModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull StatusModelClass statusModelClass) {
        viewHolder.Comments.setText(statusModelClass.getComments());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row, parent, false
        );
        return new ViewHolder(singlerow);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView Comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Comments = itemView.findViewById(R.id.nameTV);
        }

    }

}
