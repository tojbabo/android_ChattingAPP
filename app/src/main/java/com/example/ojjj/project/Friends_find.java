package com.example.ojjj.project;

import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Friends_find extends AppCompatActivity {
    int option = 1;
    static int index = 100;

    private List<Person> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<Person> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_find);
        listView = (ListView) findViewById(R.id.friends_find_list);
        editSearch = (EditText) findViewById(R.id.friends_find_search);
        registerForContextMenu(listView);

        // 리스트를 생성한다.
        list = new ArrayList<Person>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<Person>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        list.clear();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ask_add_Dialog(list.get(i));
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

    }

    public void Friends_find_setopt(View v) {
        if (v.getId() == R.id.radio_name) {
            editSearch.setHint("이름 입력");
            editSearch.setText("");
            option = 1;
        } else {
            editSearch.setHint("번호 입력");
            editSearch.setText("");
            option = 2;
        }
    }

    private void settingList() {
        Person who = new Person("fdfsdfas", "Hi", 0, null, index++);
        list.add(who);
        who = new Person("asdfsdf", "goood", R.drawable.tak, "01022904218",index++);
        list.add(who);
        who = new Person("bcbcvdv", "아아앙 절대 싫음", 0, "1303",index++);
        list.add(who);
        who = new Person("dfdfef", "맘터 ㄱ?", 0, null,index++);
        list.add(who);
        who = new Person("sdfsdfwwefwef", "짱발롬", 0, null,index++);
        list.add(who);
        who = new Person("dfsdfdf톤", "zZ", 0, null,index++);
        list.add(who);
        who = new Person("zxcvzxcv", "왜 이쪽을 봐주지 않는거니", R.drawable.tak, "12345678",index++);
        list.add(who);
        who = new Person("qewfqefqwfqw", "ㅋ", 0, null,index++);
        list.add(who);

    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            //list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                if (option == 1) {
                    // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                    if (arraylist.get(i).name.toLowerCase().contains(charText)) {
                        // 검색된 데이터를 리스트에 추가한다.
                        list.add(arraylist.get(i));
                    }
                } else if (option == 2) {
                    if (arraylist.get(i).phone.contains(charText)) {
                        // 검색된 데이터를 리스트에 추가한다.
                        list.add(arraylist.get(i));
                    }
                }
            }
            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
            adapter.notifyDataSetChanged();
        }
    }

    public void ask_add_Dialog(Person who){
        final Person proto = new Person(who);
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.firends_find_add);

        ImageView face = (ImageView)addDialog.findViewById(R.id.friends_find_add_face);
        TextView name = (TextView)addDialog.findViewById(R.id.friends_find_add_name);

        Button yes = addDialog.findViewById(R.id.friends_find_add_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("WHO",proto);
                setResult(RESULT_OK,intent);
                addDialog.dismiss();
                finish();
            }
        });
        Button no = addDialog.findViewById(R.id.friends_find_add_cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
            }
        });
        face.setImageResource(proto.face);
        name.setText(proto.name);
        addDialog.show();
    }
}
