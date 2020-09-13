package com.example.ojjj.project;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_Fragment extends Fragment {
    int[] rgb;

    public Chat_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chat_, container, false);
        rgb = (int[])getArguments().getIntArray("RGB");
        TextView head = v.findViewById(R.id.head);
        EditText find = v.findViewById(R.id.chat_search);
        head.setTextColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        find.setHintTextColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
        return v;
    }
}