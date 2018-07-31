package com.farmstory.iot2.project_farm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class SelectMenuActivity extends AppCompatActivity {

    Button mplantSelected;
    Button mdiraySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);

        getSupportActionBar().setTitle("FarmStory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mplantSelected=(Button) findViewById(R.id.plantSelected);
        mdiraySelected=(Button) findViewById(R.id.diarySelected);


        mplantSelected.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPlant;
                selectPlant = new Intent(v.getContext(), FarmListActivity.class);
                startActivity(selectPlant);
            }
        });

        mdiraySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPlant;
                selectPlant = new Intent(v.getContext(), DiaryActivity.class);
                startActivity(selectPlant);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // menu xml -> 객체(inflate)
        // menu에 추가
        getMenuInflater().inflate(R.menu.menu_overflow,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new AlertDialog.Builder(this/* 해당 액티비티를 가르킴 */)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                SharedPreferences prefs = getSharedPreferences("account", 0);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("autologin", false);
                                editor.remove("memId");
                                editor.remove("memPw");
                                editor.remove("memEmail");

                                editor.commit();

                                Intent i = new Intent(SelectMenuActivity.this/*현재 액티비티 위치*/ , AccountActivity.class/*이동 액티비티 위치*/);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
