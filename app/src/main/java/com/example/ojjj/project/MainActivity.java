package com.example.ojjj.project;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Person user= null;
    TextView my_name = null;
    ImageView my_face = null;
    Intent it_music = null;
    int[] rgb = new int[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        rgb[0] = 255;
        rgb[1] = 64;
        rgb[2] = 129;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);

                user = new Person("펭귄","쉿!",R.drawable.penguin,"01012345678",1);
                my_name = findViewById(R.id.user_name);
                my_face = findViewById(R.id.user_face);
                my_name.setText(user.name);
                if(user.getBitmap() == null)
                    my_face.setImageResource(user.face);
                else
                    my_face.setImageBitmap(user.getBitmap());
            }
        },3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                rgb = (int[])data.getIntArrayExtra("RGB");
                getWindow().setStatusBarColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
                Button btn = findViewById(R.id.login);
                btn.setTextColor(Color.rgb(255,255,255));
                btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
                btn = findViewById(R.id.song);
                btn.setTextColor(Color.rgb(255,255,255));
                btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));

            }
            else if(resultCode == RESULT_CANCELED){
                user = (Person)data.getSerializableExtra("USER");
                my_name.setText(user.name);
                if(user.getBitmap() == null)
                    my_face.setImageResource(user.face);
                else
                    my_face.setImageBitmap(user.getBitmap());
            }
        }
    }

    public void start_onclick(View v){
        Intent it = new Intent(this,Main_Body.class);
        it.putExtra("USER",user);
        it.putExtra("MUSIC",it_music);
        it.putExtra("RGB",rgb);
        startActivityForResult(it,1);
    }
    public void music_start(View v){
        final Dialog musicplayer = new Dialog(this);
        musicplayer.setContentView(R.layout.music_select);
        final RadioGroup RG = (RadioGroup)musicplayer.findViewById(R.id.music_group);
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.music_0:
                        musicplayer.dismiss();
                        music_play(0);
                        break;
                    case R.id.music_1:
                        musicplayer.dismiss();
                        music_play(1);
                        break;
                }
            }
        });
        musicplayer.show();
    }
    public void music_play(int music){
        if(it_music == null){
            it_music = new Intent(MainActivity.this,MusicService.class);
            it_music.putExtra("MUSIC",music);
            startService(it_music);
        }
        else{
            stopService(it_music);
            it_music = new Intent(MainActivity.this,MusicService.class);
            it_music.putExtra("MUSIC",music);
            startService(it_music);
        }
    }
}