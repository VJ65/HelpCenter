package com.rp.finalprojectandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterClass extends FirestoreRecyclerAdapter<com.rp.finalprojectandroid.ModelClass, com.rp.finalprojectandroid.AdapterClass.ViewHolder1> {
   String val="";
   Context ctx;

    public AdapterClass(@NonNull FirestoreRecyclerOptions<ModelClass> options, String s,Context ct) {
        super(options);
        val=s;
        ctx=ct;

    }
//you will get new unused view holders and you have to fill them with data you want to display new data dal deta hai in viewHolders main
    @Override
    protected void onBindViewHolder (@NonNull ViewHolder1 viewHolder1, int i, @NonNull final ModelClass modelClass) {

        if (modelClass.getStudent_name().contains(val)){

        viewHolder1.stName.setText(modelClass.getStudent_name ());
        viewHolder1.stName.setVisibility(View.VISIBLE);
        viewHolder1.stEmail.setText(modelClass.getStudent_email ());
            viewHolder1.stEmail.setVisibility(View.VISIBLE);
        viewHolder1.stissue.setText(modelClass.getStudent_issue ());
            viewHolder1.stissue.setVisibility(View.VISIBLE);
            viewHolder1.line.setVisibility(View.VISIBLE);
        }

        viewHolder1.relativeLayout.setOnClickListener (new View.OnClickListener () {
            @Override
                public void onClick (View v) {

                Intent intent = new Intent(ctx, UserInfoFromAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", modelClass.getStudent_name ());
                intent.putExtra("email", modelClass.getStudent_email ());
                intent.putExtra("issue", modelClass.getStudent_issue ());
                ctx.startActivity(intent);



            }
        });



    }

// parent main cell hota h jo hm create kerny lgy hain -- viewType main check kerty k layout file correct wali infate ho
    @NonNull
    @Override
    public com.rp.finalprojectandroid.AdapterClass.ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singleRow1 = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_admin, parent, false
                //inflate ka mtlb hota hai k view XMl s jo lyrahy usko change kerty java View main parse kerty
        );
        return new ViewHolder1(singleRow1,"");
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {


        TextView stName;
        TextView stEmail;
        TextView stissue;
        TextView line;
        RelativeLayout relativeLayout;


        public ViewHolder1(@NonNull View itemView,String v) {
            super(itemView);

            stName = itemView.findViewById(R.id.studentName1);
            stEmail = itemView.findViewById(R.id.studentEmail1);
            stissue = itemView.findViewById(R.id.studentIssue1);
            line=itemView.findViewById(R.id.line);
            relativeLayout=itemView.findViewById(R.id.reeee);

        }

    }

}
