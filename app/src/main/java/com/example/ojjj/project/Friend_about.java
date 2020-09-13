package com.example.ojjj.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Friend_about extends AppCompatActivity {
    String phone_num = null;
    TextView f_message = null;
    TextView f_name = null;
    ImageView f_face = null;
    Button btn_call = null;
    TextView f_phone_num = null;
    Person who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_about);
        f_message = (TextView)findViewById(R.id.friends_about_message);
        f_name = (TextView)findViewById(R.id.friends_about_name);
        f_face = (ImageView)findViewById(R.id.friends_about_face);
        btn_call = (Button)findViewById(R.id.btn_call);
        f_phone_num = (TextView)findViewById(R.id.friends_about_phone);

        Intent it = getIntent();
        who = (Person)it.getSerializableExtra("FRIEND");

        f_message.setText("\""+who.message+"\"");
        f_name.setText(who.name);
        f_face.setImageResource(who.face);
        phone_num = who.phone;
        if(phone_num == null) {
            btn_call.setVisibility(View.GONE);
            f_phone_num.setVisibility(View.INVISIBLE);
        }
        else
            f_phone_num.setText(phone_num);


      /*  byte[] arr = getIntent().getByteArrayExtra("IMG");
        Bitmap image = BitmapFactory.decodeByteArray(arr,0,arr.length);
        f_face.setImageBitmap(image);*/
        //f_face.setImageResource(R.drawable.tak);
    }
    public void Friends_about_onclick(View v){
        Intent it = null;
        switch (v.getId()){
            case R.id.btn_call:
                it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone_num));
                break;
            case R.id.btn_converse:
                Intent intent = new Intent(this, ChattingRoom.class);
                intent.putExtra("PERSON", who);
                startActivity(intent);
                who.chatting_room_exist = true;
                break;
        }
        if(it!=null)
            startActivity(it);
    }
}
