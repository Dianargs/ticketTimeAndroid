package com.example.movie_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;
    Button btnLogin,btnRegister;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin2);
        btnRegister = findViewById(R.id.btnRegisterLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    inputEmail.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    inputPassword.setError("Password is required.");
                    return;
                }


                //authentication
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login with success.",Toast.LENGTH_SHORT).show();
                            intent  = new Intent(view.getContext(), BottomNav.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}