package com.shubham.englishtalk.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubham.englishtalk.databinding.ActivityMainBinding;

import models.User;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    // new add

//    GoogleSignInClient gsc;
//    GoogleSignInOptions gso;
     // end


    User user;
    Context context;

    long coins = 0;

    BroadcastReceiver broadcastReceiver;


    String[] permissions = new String[]{"android.permission.RECORD_AUDIO", "android.permission.CAMERA", "android.permission.MODIFY_AUDIO_SETTINGS"};
    private int requestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        broadcastReceiver = new ConnectionReceiver();
        registerNetworkBroadcast();

        binding.menuBtn.setOnClickListener((v)->showMenu());

        // new add

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this,gso);

        //end
//        if (!isConnected()) {
//            binding.mainActivityLoader.setVisibility(View.GONE);
//            binding.lottieAnimationView.setVisibility(View.GONE);
//            binding.findBtn.setVisibility(View.GONE);
//            binding.imageView5.setVisibility(View.GONE);
//            binding.linearLayout21.setVisibility(View.GONE);
//            binding.linearLayout3.setVisibility(View.GONE);
//            binding.profile.setVisibility(View.GONE);
//            binding.textView6.setVisibility(View.GONE);
//            binding.linearLayout2.setVisibility(View.GONE);
//
//            binding.linearLayout2.setVisibility(View.GONE);
//            binding.offline.setVisibility(View.VISIBLE);
//
//        } else {
//
//            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
//        }


            binding.mainActivityLoader.setVisibility(View.VISIBLE);
            binding.lottieAnimationView.setVisibility(View.GONE);
            binding.findBtn.setVisibility(View.GONE);
            binding.imageView5.setVisibility(View.GONE);
            binding.linearLayout21.setVisibility(View.GONE);
            binding.linearLayout3.setVisibility(View.GONE);
            binding.profile.setVisibility(View.GONE);
            binding.textView6.setVisibility(View.GONE);
            binding.linearLayout2.setVisibility(View.GONE);

            binding.linearLayout2.setVisibility(View.GONE);

//            MobileAds.initialize(this, new OnInitializationCompleteListener() {
//                @Override
//                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                }
//            });

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            database.getReference().child("profiles")
                    .child(currentUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.mainActivityLoader.setVisibility(View.GONE);
                            binding.lottieAnimationView.setVisibility(View.VISIBLE);
                            binding.findBtn.setVisibility(View.VISIBLE);
                            binding.profile.setVisibility(View.VISIBLE);
                            binding.textView6.setVisibility(View.VISIBLE);
                            user = snapshot.getValue(User.class);
                            coins = user.getCoins();
                            binding.coins.setText("You Have " + coins);
                            Glide.with(MainActivity.this)
                                    .load(user.getProfile())
                                    .into(binding.profile);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.findBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPermissionGranted()) {
//                        if (coins > 5) {
//                            coins = coins - 5;
                            database.getReference().child("profiles")
                                    .child(currentUser.getUid())
                                    .child("coins")
                                    .setValue(coins);
                            Intent intent = new Intent(MainActivity.this, ConnectingActivity.class);
                            intent.putExtra("profile", user.getProfile());
                            startActivity(intent);
                            // startActivity(new Intent(MainActivity.this, ConnectingActivity.class));
//                        } else {
//                            Toast.makeText(MainActivity.this, "insufficient coins", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        askPermissions();
                    }
                }
            });

//            binding.baksa.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(MainActivity.this, RewardActivity.class));
//                }
//            });

        }

//        private boolean isConnected () {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
//            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//        }

    protected void registerNetworkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }

    void askPermissions () {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }

        private boolean isPermissionGranted () {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }

            return true;
        }

    void showMenu(){
        //TODO display menu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,binding.menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("PrivacyPolicy");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
//                    logout();
                    return true;
                }
                else if (menuItem.getTitle()=="PrivacyPolicy"){
                    startActivity(new Intent(MainActivity.this,PrivacyPolicy.class));
                    return true;
                }
                return false;
            }
        });
    }

//    void logout(){
//        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete( Task<Void> task) {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
//    }

}