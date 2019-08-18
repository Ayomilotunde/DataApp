package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailreg;
    private EditText passwordreg;
    private Button btnsigninreg;
    private Button btnsignupreg;


    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();

        mDialog=new ProgressDialog(this);

        emailreg=findViewById(R.id.emailreg);
        passwordreg=findViewById(R.id.passwordreg);

        btnsigninreg=findViewById(R.id.btnsigninreg);
        btnsignupreg=findViewById(R.id.btnsignupreg);

        //for Sign in
        btnsigninreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //for sign Up
        btnsignupreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail=emailreg.getText().toString().trim();
                String mPassword= passwordreg.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    emailreg.setError("Required field");
                    return;
                }

                if (TextUtils.isEmpty(mPassword)){
                    passwordreg.setError("Required field");
                    return;

                }

                mDialog.setMessage("Processing..");
                mDialog.setCancelable(false);
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"SignUp Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }

                    }
                });

            }
        });


    }
}
