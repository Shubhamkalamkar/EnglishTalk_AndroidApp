package com.shubham.englishtalk.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.shubham.englishtalk.R;

import models.User;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    int RC_SIGN_IN = 11;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            goToNextActivity();
        }

        database = FirebaseDatabase.getInstance();

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

        // new add
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this,gso);
        // end

        gsc = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new add
//                signIn();
                // end

                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
               // startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

    // new add

//   void signIn(){
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN );
//    }
   // end


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN){

        if (requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            authWithGoogle(account.getIdToken());
        }
        else {
            Toast.makeText(this, "somthing went wrong, try again", Toast.LENGTH_SHORT).show();
        }
    }

    // new add

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN){
//
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//               GoogleSignInAccount account = task.getResult(ApiException.class);
//                authWithGoogle(account.getIdToken());
//                goToNextActivity();
//            } catch (ApiException e) {
//                Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    // end

    void goToNextActivity(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    void authWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            User firebaseUser = new User(user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString(),"unknown", 50);
                            database.getReference()
                                    .child("profiles")
                                    .child(user.getUid())
                                    .setValue(firebaseUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}