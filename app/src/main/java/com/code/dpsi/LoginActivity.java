package com.code.dpsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.password);

        loginBtn = findViewById(R.id.Login_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtn = findViewById(R.id.create_account_btn);

        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

    void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        boolean isValidate= validateData(email,password);
        if (!isValidate){
            return;
        };
        loginAccountInFirebase(email,password);
    }
    void loginAccountInFirebase(String email,String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                   //login is successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to MainActivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else {
                        Utility.showToast(LoginActivity.this,"Email not verified, please verify your email");
                    }
                }
                else {
                    // login is failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }
     void changeInProgress(boolean inProgress){
         if (inProgress){
             progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        //validate the data of user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }


        return true;
    }
}