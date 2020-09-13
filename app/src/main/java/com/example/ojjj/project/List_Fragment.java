package com.example.ojjj.project;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class List_Fragment extends Fragment {
    private List<Person> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private ListView mylist;
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<Person> arraylist;
    Person user;
    View v;
    TextView num;
    int[] rgb;


    public List_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_list_, container, false);

        user = (Person) getArguments().getSerializable("USER");
        rgb = (int[])getArguments().getIntArray("RGB");
        LinearLayout layout_contain = (LinearLayout)v.findViewById(R.id.friends_i_am);
        LayoutInflater layout_inflater = (LayoutInflater)getActivity().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
        layout_inflater.inflate(R.layout.friends,layout_contain,true);
        layout_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Main_Body)getActivity()).user_change(1);
            }
        });
        who_i_am();

        editSearch = (EditText) v.findViewById(R.id.friends_name_search);
        listView = (ListView) v.findViewById(R.id.list_friends);
        registerForContextMenu(listView);

        // 리스트를 생성한다.
        list = new ArrayList<Person>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<Person>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, (Main_Body)getActivity());

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        Button btn_add = v.findViewById(R.id.btn_friends_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friends_add();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent((Main_Body)getActivity(), ChattingRoom.class);
                it.putExtra("PERSON", list.get(position));
                startActivityForResult(it,2);
                list.get(position).chatting_room_exist = true;
            }
        });

        Comparator<Person> cmp = new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                return person.name.compareTo(t1.name);
            }
        };

        Collections.sort(list,cmp);

        adapter.notifyDataSetChanged();

        num = v.findViewById(R.id.friends_num);
        num.setText(adapter.getCount()+"명");

        TextView head =  v.findViewById(R.id.head);
        head.setTextColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        editSearch.setHintTextColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        num.setTextColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        Button btn = v.findViewById(R.id.btn_friends_add);
        btn.setTextColor(Color.rgb(255,255,255));
        btn.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));


        return v;
    }
    public void who_i_am(){
        TextView my_name = (TextView)v.findViewById(R.id.friends_name);
        my_name.setText(user.name);
        TextView my_message = (TextView)v.findViewById(R.id.friends_message);
        my_message.setText(user.message);
        ImageView my_face = (ImageView) v.findViewById(R.id.friends_face);
        if(user.getBitmap() == null) {
            my_face.setImageResource(user.face);
        }
        else
            my_face.setImageBitmap(user.getBitmap());
    }
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();
        int j = 0;
        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
            num.setText(adapter.getCount()+"명");
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).name.toLowerCase().contains(charText))
                {
                    j++;
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));

                }
            }
            num.setText(j + "명");
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }
    private void settingList(){
        Person who = new Person("금기륜","Hi",0,null,2);
        list.add(who);
        who = new Person("탁원준","goood",R.drawable.tak,"01022904218",3 );
        list.add(who);
        who = new Person("최수인","아아앙 절대 싫음",0,"1303",4);
        list.add(who);
        who = new Person("방영철","맘터 ㄱ?",0,null,5);
        list.add(who);
        who = new Person("이상호","짱발롬",0,null,6);
        list.add(who);
        who = new Person("에어스톤","zZ",0,null,7);
        list.add(who);
        who = new Person("세준찡","왜 이쪽을 봐주지 않는거니",R.drawable.tak,"12345678",8);
        list.add(who);
        who = new Person("참치동원","ㅋ",0,null,9);
        list.add(who);

    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        ListView lv = (ListView)v.findViewById(R.id.list_friends);

        menu.setHeaderTitle(list.get(info.position).name);
        menu.add(0,1,0,"프로필 보기");
        menu.add(0,2,0 ,"친구 삭제");
        menu.add(0,3,0,"대화하기");
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case 1:
                Intent it = new Intent((Main_Body)getActivity(),Friend_about.class);
                it.putExtra("FRIEND",list.get(info.position));
                startActivity(it);
                return true;
            case 2:
                friends_delete(info.position);
                return true;
            case 3:
                Intent intent = new Intent((Main_Body)getActivity(), ChattingRoom.class);
                intent.putExtra("PERSON", list.get(info.position));
                startActivityForResult(intent,2);
                list.get(info.position).chatting_room_exist = true;
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void friends_add(){
        Intent it = new Intent(getActivity(),Friends_find.class);
        startActivityForResult(it,1);
    }
    public void friends_delete(int num){
        int count, check=num;
        count = adapter.getCount();
        if(count>0){
            if(check>-1 && check <count){
                list.remove(check);
                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Person who = new Person((Person)data.getSerializableExtra("WHO"));
                list.add(who);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

