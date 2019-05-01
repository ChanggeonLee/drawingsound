package com.a7f.drawingsound.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkCheck {

    private Activity activity;

    public NetworkCheck(Activity activity){
        this.activity = activity;
    }

    private void NetworkError(){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("Network 연결 후 다시 시도해주세요");
        alert.show();

    }

    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public boolean checkNetwork(){
        try {
            NetworkInfo networkInfo = getNetworkInfo();
            if (networkInfo.isConnected()) {
                return true;
            }
        } catch (NullPointerException ne){
            NetworkError();
        }
        return false;
    }
}
