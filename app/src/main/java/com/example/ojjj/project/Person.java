package com.example.ojjj.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

public class Person extends AppCompatActivity implements Serializable {
    private int[] pixels= null;
    private int width=0,height=0;

    byte[] byteArray = null;
    String name;
    String message;
    int face;
    String phone = "";
    int user_id;
    boolean chatting_room_exist = false;
    int count;


    public Person(){}
    public Person(Person clone){
        name = clone.name;
        message = clone.message;
        face = clone.face;
        phone = clone.phone;
        byteArray = clone.byteArray;
        count = 0;
    }
    public Person(String name, String message, int img, String phone, int user_id) {
        this.name = name;
        this.message = message;
        this.user_id = user_id;
        this.count = 0;

        if(phone != null)
            this.phone = phone;
        if (img == 0)
            this.face = R.drawable.default_face;
        else
            this.face = img;

    }
    public void makeBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byteArray = stream.toByteArray();
    }
    public Bitmap getBitmap(){
        if(byteArray == null)
            return null;
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<Person> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<Person> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.friends,null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.friends_name);
            viewHolder.message = (TextView)convertView.findViewById(R.id.friends_message);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.friends_face);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.name.setText((CharSequence) list.get(position).name);
        viewHolder.message.setText("\""+(CharSequence)list.get(position).message+"\"");
        viewHolder.image.setImageResource(list.get(position).face);

        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView message;
        public ImageView image;
    }

}

