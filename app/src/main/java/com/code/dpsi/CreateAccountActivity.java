package com.code.dpsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {
     EditText emailEditText,passwordEditText,confirmEditText ;
     Button createAccountBtn;
     ProgressBar progressBar;
     TextView login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText= findViewById(R.id.Email);
        passwordEditText= findViewById(R.id.password);
        confirmEditText= findViewById(R.id.Confirm_password);
        createAccountBtn= findViewById(R.id.Create_btn);
        progressBar= findViewById(R.id.progress_bar);
        login_btn= findViewById(R.id.login_btn);

        createAccountBtn.setOnClickListener(v-> CreateAccount());
        login_btn.setOnClickListener(v->finish());
    }
    void CreateAccount(){
          String email = emailEditText.getText().toString();
          String password = passwordEditText.getText().toString();
          String confirmPassword = confirmEditText.getText().toString();

          boolean isValidate= validateData(email,password,confirmPassword);
          if (!isValidate){
              return;
          };
          createAccountInFirebase(email,password);
    }
      void createAccountInFirebase(String email,String password){
        changeInProgress(true);

          FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
          firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                  new OnCompleteListener<AuthResult>() {
                      @Override

                      public void onComplete(@NonNull Task<AuthResult> task) {
                          changeInProgress(false);
                          if(task.isSuccessful()){
                              //creating is done
                              Utility.showToast(CreateAccountActivity.this,"Successfully crate account,Check email to verify");
                              firebaseAuth.getCurrentUser().sendEmailVerification();
                              firebaseAuth.signOut();
                              finish();
                          }
                          else {
                              //failure
                              Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());
                          }
                      }
                  });

      }
      void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
               progressBar.setVisibility(View.GONE);
               createAccountBtn.setVisibility(View.VISIBLE);}
      }

    boolean validateData(String email,String password,String confirmPassword){
        //validate the data of user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false ;
        }
        if(password.length()<6){
            passwordEditText.setError("Password is too short");
            return false;
        }
        if (!password.equals(confirmPassword)){
            confirmEditText.setError("Password doesn't match ");
            return false;
        }
        return true;
    }
}