package com.example.ojjj.project;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ChattingRoom extends AppCompatActivity {
    private Person person = null;
    private ArrayList<chat_list> list;
    ListAdaptor adapter;
    private ListView listView;
    private Chatting_db chat_db;
    private SQLiteDatabase db;
    private TextView name;
    private EditText edit_contents;
    private Cursor cursor;
    private Handler handler = new Handler();

    final static String ACTION = "GET_DATA";

    //방송 수신자의 행동
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Cursor cursor;
            cursor = db.rawQuery("SELECT * From data_table WHERE user_id = "+person.user_id+";",null);

            cursor.moveToLast();
            String name_query = cursor.getString(1);
            String contents_query = cursor.getString(2);
            chat_list one = new chat_list(name_query,contents_query);
            list.add(one);
            adapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingroom);

        name = findViewById(R.id.text_chatting_name);
        edit_contents = findViewById(R.id.edit_chatting);

        chat_db = new Chatting_db(this);
        try{
            db = chat_db.getWritableDatabase();
        }
        catch(SQLiteException ex){
            db = chat_db.getReadableDatabase();
            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
        }

        Intent it = getIntent();
        person = (Person)it.getSerializableExtra("PERSON");
        name.setText("with  "+ person.name);
        listView = (ListView)findViewById(R.id.chatting_room_list);

        list = new ArrayList<chat_list>();
        adapter = new ListAdaptor(list);
        listView.setAdapter(adapter);


        if(person.chatting_room_exist == false){

            Thread counter_part;
            // 상대방 쓰레드 추가
            counter_part = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                            Thread.sleep(1000);
                            person.count ++;

                            if(person.count %5 == 0){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.execSQL("INSERT INTO data_table VALUES ("+person.user_id+" , '" + (person.name+" : ") + "', '"+person.count+"');");

                                        /*
                                        Cursor cursor;
                                        cursor = db.rawQuery("SELECT * From data_table WHERE user_id = "+person.user_id+" and contents = '"+person.count+"';",null);
                                        cursor.moveToLast();
                                        String name_query = cursor.getString(1);
                                        String contents_query = cursor.getString(2);
                                        chat_list one = new chat_list(name_query,contents_query);
                                        list.add(one);
                                        adapter.notifyDataSetChanged();
                                        */

                                        Intent intent = new Intent();
                                        intent.setAction(Main_Body.ACTION);
                                        intent.putExtra(Main_Body.DATA, person.name);
                                        sendBroadcast(intent);

                                        Intent intent2 = new Intent();
                                        intent2.setAction(ChattingRoom.ACTION);
                                        sendBroadcast(intent2);

                                    }
                                });
                            }

                        }catch (Exception e){}
                    }
                }
            });
            counter_part.start();
        }
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM data_table WHERE user_id = "+person.user_id+";", null);

        while(cursor.moveToNext()){
            String name_query = cursor.getString(1);
            String contents_query = cursor.getString(2);
            chat_list one = new chat_list(name_query,contents_query);
            list.add(one);
            adapter.notifyDataSetChanged();
        }
    }

    public void enter_chatting(View v){
        String contents = edit_contents.getText().toString();
        db.execSQL("INSERT INTO data_table VALUES (" +person.user_id+ ", '나 : ' , '"+contents+"');");

        Cursor cursor;
        cursor = db.rawQuery("SELECT * From data_table WHERE user_id = "+person.user_id+";",null);

        cursor.moveToLast();
        String name_query = cursor.getString(1);
        String contents_query = cursor.getString(2);
        chat_list one = new chat_list(name_query,contents_query);
        list.add(one);
        adapter.notifyDataSetChanged();

        edit_contents.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(receiver, filter);
    }
}


class Chatting_db extends SQLiteOpenHelper {
    private static final String DB_NAME = "chatting_v.15.db";
    private static final int DB_VERSION = 2;

    public Chatting_db(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS data_table(user_id INTEGER, name TEXT, contents TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS ONE");
        onCreate(db);
    }
}


class chat_list implements Serializable {
    String name;
    String contents;
    private LayoutInflater inflater;

    public chat_list(String name, String contents){
        this.name = name;
        this.contents = contents;
    }
}

class ListAdaptor extends  BaseAdapter{
    LayoutInflater inflater;
    List<chat_list> list;

    public ListAdaptor(List<chat_list> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Context context = parent.getContext();
        chat_list cl = (chat_list) getItem(position);

        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contents_list, null);
        }

        TextView name = (TextView)convertView.findViewById(R.id.chatting_room_text_name);
        TextView content = (TextView)convertView.findViewById(R.id.chatting_room_text_contents);

        if(cl.name.equals("나 : ")){
            LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.contents_layout);
            linearLayout.setGravity(Gravity.LEFT);
        }else{
            LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.contents_layout);
            linearLayout.setGravity(Gravity.RIGHT);
        }

        name.setText(cl.name);
        content.setText(cl.contents);


        return convertView;
    }
}