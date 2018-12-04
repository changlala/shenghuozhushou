package com.example.shenghuozhushou;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URI;
import java.util.List;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        LinearLayout lJisuanqi = findViewById(R.id.jisuanqi);
        LinearLayout lDadianhua = findViewById(R.id.dadianhua);
        LinearLayout lTongxunlu = findViewById(R.id.tongxunlu);
        LinearLayout lNaozhong = findViewById(R.id.naozhong);
        LinearLayout lRili = findViewById(R.id.rili);
        LinearLayout lYinyue = findViewById(R.id.yinyue);
        LinearLayout lTianqi = findViewById(R.id.tianqi);
        LinearLayout lXinwen = findViewById(R.id.xinwen);
        LinearLayout lDuanxin = findViewById(R.id.duanxin);


        lJisuanqi.setOnClickListener(this);
        lDadianhua.setOnClickListener(this);
        lTongxunlu.setOnClickListener(this);
        lNaozhong.setOnClickListener(this);
        lRili.setOnClickListener(this);
        lYinyue.setOnClickListener(this);
        lTianqi.setOnClickListener(this);
        lXinwen.setOnClickListener(this);
        lDuanxin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.jisuanqi:
                PackageInfo calInfo = getAppInfo(this,"Calculator","calculator");
                intent = getPackageManager().getLaunchIntentForPackage(calInfo.packageName);
                break;
            case R.id.dadianhua:
                intent = new Intent(Intent.ACTION_DIAL);
                break;
            case R.id.tongxunlu:
                intent = new Intent(MainActivity.this,TongxunluActivity.class);
                break;
            case R.id.naozhong:
                intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                break;
            case R.id.rili:
                intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI);

                break;
            case R.id.yinyue:

                break;
            case R.id.tianqi:
                ;
                break;
            case R.id.xinwen:
                ;
                break;
            case R.id.duanxin:
                intent = new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("smsto:"));

                break;
        }
        if (intent != null && intent.resolveActivity(getPackageManager()) != null) {//检查至少有一个符合的app
            startActivity(intent);
        }else{
            Log.d(TAG, "intent error");
        }
    }

    /* 寻找包含特殊字段的已安装app的packageInfo*/
    private PackageInfo getAppInfo(Context context , String appFlag1 , String appFlag2){
        PackageManager manager = context.getPackageManager();
        //得到所有app的包信息
        List<PackageInfo> packageInfos = manager.getInstalledPackages(0);
        for(PackageInfo packageInfo : packageInfos){
            String packageName = packageInfo.packageName;
            //判断包名是否含有特定字段
            if(packageName.contains(appFlag1) || packageName.contains(appFlag2))
                return packageInfo;
        }
        return null;
    }


}
