package com.example.personal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.personal.Model.Data;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton fabadd;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;

    private String post_key;
    private String name;
    private String matricno;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mDatabase=FirebaseDatabase.getInstance().getReference().child("All Data").child(uid);


        recyclerView=findViewById(R.id.recyclerid);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fabadd=findViewById(R.id.fabadd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData();
            }
        });

    }

    private void AddData() {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View myview=inflater.inflate(R.layout.inputdata, null);

        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);

        final EditText name=myview.findViewById(R.id.name);
        final EditText matricno=myview.findViewById(R.id.matricno);
        final EditText description=myview.findViewById(R.id.description);

        Button cancel=myview.findViewById(R.id.cancel);
        Button submit=myview.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mName=name.getText().toString().trim();
                String mMatricno=matricno.getText().toString().trim();
                String mDescription=description.getText().toString().trim();

                if (TextUtils.isEmpty(mName)) {
                    name.setError("Required Field...");
                }
                if (TextUtils.isEmpty(mMatricno)) {
                    matricno.setError("Required Field...");
                }
                if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Required Field...");
                }

                String id=mDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data=new Data(mName,mMatricno,mDescription,id,mDate);

                mDatabase.child(id).setValue(data);
                Toast.makeText(getApplicationContext(),"Data Uploaded",Toast.LENGTH_SHORT).show();


                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.itemdesign,
                        MyViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setName(model.getName());
                viewHolder.setMatricno(model.getMatricno());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDate(model.getDate());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();
                        name=model.getName();
                        matricno=model.getMatricno();
                        description=model.getDescription();

                        updateData();

                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name) {
            TextView Mname=mView.findViewById(R.id.name_item);
            Mname.setText(name);
        }

        public void setMatricno(String matricno) {
            TextView Mmatricno=mView.findViewById(R.id.matricno_item);
            Mmatricno.setText(matricno);
        }

        public void setDescription(String description) {
            TextView Mdescription=mView.findViewById(R.id.description_item);
            Mdescription.setText(description);
        }
        public void setDate(String date) {
            TextView Mdate=mView.findViewById(R.id.date_item);
            Mdate.setText(date);
        }


    }

    public void updateData() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View myview=inflater.inflate(R.layout.update_data, null);

        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();
        //dialog.setCancelable(false);

        final EditText mName=myview.findViewById(R.id.name);
        final EditText mMatricno=myview.findViewById(R.id.matricno);
        final EditText mDescription=myview.findViewById(R.id.description);

        mName.setText(name);
        mName.setSelection(name.length());

        mMatricno.setText(matricno);
        mMatricno.setSelection(matricno.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button update=myview.findViewById(R.id.update);
        Button delete=myview.findViewById(R.id.delete);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=mName.getText().toString().trim();
                matricno=mMatricno.getText().toString().trim();
                description=mDescription.getText().toString().trim();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(name,matricno,description,post_key,mDate);
                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();

            }
        });


        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
