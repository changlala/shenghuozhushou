package com.example.shenghuozhushou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TongxunluActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongxunlu);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setTitle("通讯录");

        }
    }
}
