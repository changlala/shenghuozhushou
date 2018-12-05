package com.example.shenghuozhushou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 基类activity 当网络连接变化时 弹Toast
 */
public class MyBaseActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean isFirstRun ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if(networkChangeReceiver == null){
            networkChangeReceiver = new NetworkChangeReceiver();
            //注册完成时就会弹一条通知
            isFirstRun = true;
            registerReceiver(networkChangeReceiver,intentFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(networkChangeReceiver != null){
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //防止广播注册成功还要弹一条网络变化
            if(isFirstRun){
                isFirstRun = false;
                return;
            }
            if(networkInfo != null && networkInfo.isConnected()){
                Toast.makeText(context,"网络已连接",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"网络已断开",Toast.LENGTH_LONG).show();
            }
        }
    }
}
