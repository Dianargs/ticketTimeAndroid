package com.example.movie_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    EditText mEmail,mPassword,mPhone,mRepeatPassord;
    Button mRegisterBtn, mLoginBtn;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.inputEmail2);
        mPhone = findViewById(R.id.phoneNumb);
        mPassword =  findViewById(R.id.inputPassword);
        mRepeatPassord =  findViewById(R.id.inputRepeatPassword);
        mRegisterBtn =  findViewById(R.id.btnRegister2);
        mLoginBtn = findViewById(R.id.btnLoginRegister);

        Auth = FirebaseAuth.getInstance();

        if(Auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =  mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String ConfPass = mRepeatPassord.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Password have more than 6 characters");
                    return;
                }
                if(!password.equals(ConfPass)){
                    mRepeatPassord.setError("Passwords don't match.");
                    return;
                }

                Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User Created", Toast.LENGTH_SHORT).show();
                            openMenu();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void openMenu(){
        Intent intent  = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}