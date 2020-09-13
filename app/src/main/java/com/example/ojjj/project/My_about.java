package com.example.ojjj.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

public class My_about extends AppCompatActivity {
    Person iam = null;
    EditText my_name;
    EditText my_phone;
    EditText my_message;
    ImageView my_face;
    Bitmap img;
    boolean check =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_about);
        Intent it = getIntent();
        iam = (Person)it.getSerializableExtra("PERSON");
        int[] rgb = (int[])it.getIntArrayExtra("RGB");
        getWindow().setStatusBarColor(Color.rgb(rgb[0],rgb[1],rgb[2]));

        my_name = findViewById(R.id.my_about_name);
        my_face = findViewById(R.id.my_about_face);
        my_message = findViewById(R.id.my_about_message);
        my_phone = findViewById(R.id.my_about_phone);

        my_name.setText(iam.name);
        my_phone.setText(iam.phone);
        my_message.setText(iam.message);
        if(iam.getBitmap() == null)
            my_face.setImageResource(iam.face);
        else
            my_face.setImageBitmap(iam.getBitmap());

        Button ok  = findViewById(R.id.my_btn_change);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iam.name = my_name.getText().toString();
                iam.message = my_message.getText().toString();
                if(check)
                    iam.makeBitmap(img);
                Intent it = new Intent();

                it.putExtra("IAM",iam);
                setResult(RESULT_OK,it);
                finish();
            }
        });
        Button no = findViewById(R.id.my_btn_cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        my_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setType("image/*");
                it.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(it,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    my_face.setImageBitmap(img);
                    check = true;
                }catch(Exception e){e.printStackTrace();}
            }
        }
    }
}
