package com.example.shenghuozhushou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewsActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if(getSupportActionBar()!= null)
            getSupportActionBar().hide();
    }
}
