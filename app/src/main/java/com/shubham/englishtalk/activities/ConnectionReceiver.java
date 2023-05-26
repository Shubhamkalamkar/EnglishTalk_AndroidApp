package com.shubham.englishtalk.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shubham.englishtalk.R;

public class ConnectionReceiver extends BroadcastReceiver {

    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (isConnected(context)){
           // Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
        } else {
         //   Toast.makeText(context, "no Internet", Toast.LENGTH_SHORT).show();
            showDialog();

        }
    }

    public boolean isConnected(Context context){

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }


    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_dialog, null);
        Button btnOk = view.findViewById(R.id.btnOk);
        builder.setView(view);

        final Dialog dialog = builder.create();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
