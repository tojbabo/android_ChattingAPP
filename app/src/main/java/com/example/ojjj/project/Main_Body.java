package com.example.ojjj.project;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main_Body extends AppCompatActivity {
    final static String ACTION = "GET_STRING";
    final static String DATA = "STRING";
    Person user=null;
    Intent it_music=null;
    LinearLayout LL;
    Bundle bundle;
    boolean change = false;
    Fragment fr;
    int[] rgb;


    //방송 수신자의 행동
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(Main_Body.this, intent.getStringExtra(DATA)+ "님으로부터 메세지", Toast.LENGTH_SHORT).show();
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(String.valueOf(this),null,intent.getStringExtra(DATA),null,null);
            }catch (Exception e){ e.printStackTrace();}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_body);
/*
        LL = findViewById(R.id.layout_main_body);
        LL.setBackgroundColor(Color.rgb(0,255,0));
        getWindow().setStatusBarColor(Color.rgb(0,255,0));
*/
        Intent it = getIntent();
        user = (Person)it.getSerializableExtra("USER");
        it_music = (Intent)it.getSerializableExtra("MUSIC");
        rgb = (int[])it.getIntArrayExtra("RGB");

        color_change(rgb);

        fr = new List_Fragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("USER",user);
        bundle.putIntArray("RGB",rgb);
        fr.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.main_frame,fr)
                .commit();
    }

    public void change_menu (View v){
        switch(v.getId()){
            case R.id.main_btn_friend:
                fr = new List_Fragment();
                bundle = new Bundle(1);
                bundle.putSerializable("USER",user);
                bundle.putIntArray("RGB",rgb);
                fr.setArguments(bundle);
                break;
            case R.id.main_btn_chat:
                fr = new Chat_Fragment();
                bundle = new Bundle(3);
                bundle.putIntArray("RGB",rgb);
                fr.setArguments(bundle);
                break;
            case R.id.main_btn_setting:
                fr = new Setting_Fragment();
                bundle = new Bundle(2);
                bundle.putSerializable("USER",user);
                bundle.putIntArray("RGB",rgb);
                fr.setArguments(bundle);
                break;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.main_frame,fr)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(receiver, filter);
    }

    public void music_change(int num){
        if(it_music == null){
            it_music = new Intent(this,MusicService.class);
            it_music.putExtra("MUSIC",num);
            startService(it_music);
        }
        else{
            stopService(it_music);
            it_music = new Intent(this,MusicService.class);
            it_music.putExtra("MUSIC",num);
            startService(it_music);
        }

    }

    public void user_change(int num){
        Intent it = new Intent(this,My_about.class);
        it.putExtra("PERSON",user);
        it.putExtra("RGB",rgb);
        startActivityForResult(it,1);
        if (num == 1)
            change = true;

    }
    public void color_change(int[] rgb){
        Button btn = findViewById(R.id.main_btn_chat);
        btn.setTextColor(Color.rgb(255,255,255));
        btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        btn = findViewById(R.id.main_btn_friend);
        btn.setTextColor(Color.rgb(255,255,255));
        btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        btn = findViewById(R.id.main_btn_setting);
        btn.setTextColor(Color.rgb(255,255,255));
        btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));

        LinearLayout LL = findViewById(R.id.Main_body_color);
        LL.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        this.rgb[0] = rgb[0];
        this.rgb[1] = rgb[1];
        this.rgb[2] = rgb[2];
        getWindow().setStatusBarColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        Intent it= new Intent();
        it.putExtra("RGB",rgb);
        setResult(RESULT_OK,it);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Person who = new Person((Person)data.getSerializableExtra("IAM"));
                user=who;
                Intent it= new Intent();
                it.putExtra("USER",user);
                setResult(RESULT_CANCELED,it);
                if(change) {
                    fr = new List_Fragment();
                    bundle = new Bundle(1);
                    bundle.putSerializable("USER",user);
                    bundle.putIntArray("RGB",rgb);
                    fr.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, fr)
                            .commit();
                }
            }
        }
    }

}
