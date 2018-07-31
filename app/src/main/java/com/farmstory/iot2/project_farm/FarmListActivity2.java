package com.farmstory.iot2.project_farm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import vo.Plant;

public class FarmListActivity2 extends AppCompatActivity {

    private ListView mPlantListView;
    private PlantListAdapter mPlantListAdapter;
    private List<Plant> mPlants;
    private TextView mLogText;

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

                                Intent i = new Intent(FarmListActivity2.this/*현재 액티비티 위치*/ , AccountActivity.class/*이동 액티비티 위치*/);
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

    private void loadPlants() {

        Thread t = new Thread() {
            public void run() {
                try {
                    int x = (int)(Math.random() * 900) + 100;
                    String y = String.valueOf((int)(Math.random() * 900) + 100);
                    String serverUrl = String.format("http://172.16.6.8:8087/farmstory/mobile_plant_list.action?x=%d&y=%s", x, y);

                    URL url = new URL(serverUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    final int responseCode = con.getResponseCode();

                    if (responseCode == 200) {  //정상 응답인 경우
                        processResult(con.getInputStream());
                        //processResult1(con.getInputStream());
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //show error message
                                Toast.makeText(getApplicationContext(),
                                        "error " + responseCode, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };
        t.start();

    }

    private void processResult(InputStream inputStream) {

        mPlants.clear();

        try {
            //JSON 문자열 -> 객체 트리로 변환하는 변환기 만들기
            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Plant[] plants = gson.fromJson(reader, Plant[].class);

            for (Plant plant : plants) {
                mPlants.add(plant);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //데이터가 변경되었으니 목록을 갱신해서 표시하세요
                    mPlantListAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processResult1(InputStream is) throws IOException {
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        final StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        mLogText.post(new Runnable() {
            @Override
            public void run() {
                mLogText.setText(response.toString());
            }
        });
    }
}
