package com.example.ojjj.project;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Setting_Fragment extends Fragment {
    Person user;
    int music;
    int[] rgb;
    int c_r,c_g,c_b;
    TextView tv;
    TextView head;
    TextView musicB;
    TextView whoiam;
    TextView setcol;
    public Setting_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting_, container, false);
        user = (Person) getArguments().getSerializable("USER");
        rgb = (int[])getArguments().getIntArray("RGB");
        musicB = v.findViewById(R.id.setting_btn_music);
        whoiam = v.findViewById(R.id.setting_btn_iam);
        c_r = rgb[0];
        c_g = rgb[1];
        c_b = rgb[2];
        head = v.findViewById(R.id.setting_head);
        head.setTextColor(Color.rgb(c_r,c_g,c_b));
        whoiam.setTextColor(Color.rgb(c_r,c_g,c_b));
        musicB.setTextColor(Color.rgb(c_r,c_g,c_b));
        musicB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_sel(view);
            }
        });
        whoiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Main_Body) getActivity()).user_change(0);
            }
        });
        setcol = v.findViewById(R.id.setting_btn_color);
        setcol.setTextColor(Color.rgb(c_r,c_g,c_b));
        setcol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_sel(view);
            }
        });
        return v;
    }

    public void music_sel(View v){
        final Dialog musicplayer = new Dialog(v.getContext());
        musicplayer.setContentView(R.layout.music_select);
        final RadioGroup RG = (RadioGroup)musicplayer.findViewById(R.id.music_group);
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.music_0:
                        musicplayer.dismiss();
                        music = 0;
                        break;
                    case R.id.music_1:
                        musicplayer.dismiss();
                        music = 1;
                        break;
                }
                ((Main_Body)getActivity()).music_change(music);
            }
        });
        musicplayer.show();
    }
    public void color_sel(View v){
        final Dialog color_dialog = new Dialog(v.getContext());
        color_dialog.setContentView(R.layout.setting_color);
        TextView ok = (TextView)color_dialog.findViewById(R.id.ok);
        TextView cacel = (TextView)color_dialog.findViewById(R.id.cancel);
        tv = (TextView)color_dialog.findViewById(R.id.color_view);
        tv.setBackgroundColor(Color.rgb(c_r,c_g,c_b));
        SeekBar Red =(SeekBar)color_dialog.findViewById(R.id.color_R);
        Red.setProgress(c_r);
        Red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                c_r = seekBar.getProgress();
                tv.setBackgroundColor(Color.rgb(c_r,c_g,c_b));
            }
        });
        SeekBar Green =(SeekBar)color_dialog.findViewById(R.id.color_G);
        Green.setProgress(c_g);
        Green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                c_g = seekBar.getProgress();
                tv.setBackgroundColor(Color.rgb(c_r,c_g,c_b));
            }
        });
        SeekBar Blue =(SeekBar)color_dialog.findViewById(R.id.color_B);
        Blue.setProgress(c_b);
        Blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                c_b = seekBar.getProgress();
                tv.setBackgroundColor(Color.rgb(c_r,c_g,c_b));
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgb[0] = c_r;
                rgb[1] = c_g;
                rgb[2] = c_b;
                color_dialog.dismiss();
                head.setTextColor(Color.rgb(c_r,c_g,c_b));
                whoiam.setTextColor(Color.rgb(c_r,c_g,c_b));
                musicB.setTextColor(Color.rgb(c_r,c_g,c_b));
                setcol.setTextColor(Color.rgb(c_r,c_g,c_b));
                ((Main_Body)getActivity()).color_change(rgb);
            }
        });
        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_dialog.dismiss();
            }
        });

        color_dialog.show();
    }

}
