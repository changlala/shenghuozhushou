package com.example.shenghuozhushou;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MusicActivity";
    ListView listView = null;
    ArrayAdapter<String> musicAdapter = null;
    List<String> musicList = new ArrayList<String>();
    List<String> musicPathList = new ArrayList<String>();
    private String[] PROJECTION = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media._ID
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        listView = findViewById(R.id.listview_music);
        //对于自定义view注意提供第三个参数
        musicAdapter = new ArrayAdapter<>(this,R.layout.contacts_item_view,R.id.textview,
                musicList);
        listView.setAdapter(musicAdapter);
        listView.setOnItemClickListener(this);
        queryMusics();
    }

    private void queryMusics(){
        Cursor cursor = null;
        try{
            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,PROJECTION,null
                                                ,null,null);

            if(cursor != null){
                while(cursor.moveToNext() ){
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                    musicList.add(displayName+"  "+title+"  "+duration+"  ");
//                    musicPathList.add(path);
                    musicPathList.add(id);

                }
                musicAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null)
                cursor.close();
        }

    }

    /*经过试验，不要调用getContentUri来获取uri，而是用CONTENT_URI常量搭配_ID列组成表名+id的形式表示uri*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Uri selectedMusicUri = MediaStore.Audio.Media.getContentUri(musicPathList.get(position));
        /*CONTENT_URI常量搭配_ID列组成表名+id的形式表示uri*/
        Uri selectedMusicUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,musicPathList.get(position));
        Log.d(TAG, "onItemClick: uri: "+selectedMusicUri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(selectedMusicUri);
        //type类型不要加！！！
//        intent.setType("audio/*");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Log.d(TAG, "onItemClick: intent error");
        }
    }
}
