package com.farmstory.iot2.project_farm;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ValueActivity extends Activity {
    private int mYear, mMonth, mDay;
    private TextView mTxtDate;
    private Button mButton;

    private List<vo.PlantVal> plantVals = new ArrayList<>();
    private TableLayout layout1;
    private TableLayout layout2;
    private TableLayout layout3;
    private TableLayout layout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);

        mTxtDate = findViewById(R.id.txtdate);
        mButton = findViewById(R.id.btndate);
        layout1 = findViewById(R.id.table1);
        layout2 = findViewById(R.id.table2);
        layout3 = findViewById(R.id.table3);
        layout4 = findViewById(R.id.table4);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ValueActivity.this, mDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        RequestThread requestThread = new RequestThread();
        requestThread.start();
        UpdateNow();

    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    RequestThread requestThread = new RequestThread();
                    requestThread.start();
                    UpdateNow();

                }
            };


    void UpdateNow() {

        mTxtDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

    }

    class RequestThread extends Thread {
        @Override
        public void run() {

            try {
                URL url = new URL(String.format("http://172.16.6.8:8087/farmstory/value/mFindTime.action?date=%s&regPotNo=farmstory", String.format("%d-%d-%d", mYear, mMonth + 1, mDay)));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    processResult(con);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

    private void processResult(HttpURLConnection conn) {

        plantVals.clear();//기존 목록 제거

        try {
            //JSON 문자열 -> 객체 트리로 변환하는 변환기 만들기
            InputStream is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            JsonParser parser = new JsonParser();

            //변환 처리 -> JsonElement 반환
            JsonElement je = parser.parse(reader);

            //객체 탐색
            JsonArray items = je.getAsJsonArray();

            Gson gson = new Gson();

            for (int i = 0; i < items.size(); i++) {
                JsonObject element = items.get(i).getAsJsonObject();
                final vo.PlantVal val = gson.fromJson(element, vo.PlantVal.class); // JSON 객체 VO 객체로 직접 변환

                plantVals.add(val);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (layout1.getChildCount() > 1) {
                            layout1.removeViews(1, 7);
                        }
                        for (int j = 0; j < 28; j++) {

                            if (plantVals.get(j).getPlvType().equals("온도")) {

                                TableRow tableRow = new TableRow(ValueActivity.this);
                                layout1.addView(tableRow);

                                TextView textView1 = new TextView(ValueActivity.this);

                                textView1.setText(String.valueOf(plantVals.get(j).getPlvDateTime()));
                                tableRow.addView(textView1);

                                TextView textView2 = new TextView(ValueActivity.this);
                                textView2.setText(String.valueOf(plantVals.get(j).getPlvVal()));
                                tableRow.addView(textView2);
                            }
                        }

                        if (layout2.getChildCount() > 1) {
                            layout2.removeViews(1, 7);
                        }
                        for (int j = 0; j < 28; j++) {

                            if (plantVals.get(j).getPlvType().equals("습도")) {

                                TableRow tableRow = new TableRow(ValueActivity.this);
                                layout2.addView(tableRow);

                                TextView textView1 = new TextView(ValueActivity.this);

                                textView1.setText(String.valueOf(plantVals.get(j).getPlvDateTime()));
                                tableRow.addView(textView1);

                                TextView textView2 = new TextView(ValueActivity.this);
                                textView2.setText(String.valueOf(plantVals.get(j).getPlvVal()));
                                tableRow.addView(textView2);
                            }
                        }

                        if (layout3.getChildCount() > 1) {
                            layout4.removeViews(1, 7);
                        }
                        for (int j = 0; j < 28; j++) {

                            if (plantVals.get(j).getPlvType().equals("조도")) {

                                TableRow tableRow = new TableRow(ValueActivity.this);
                                layout3.addView(tableRow);

                                TextView textView1 = new TextView(ValueActivity.this);

                                textView1.setText(String.valueOf(plantVals.get(j).getPlvDateTime()));
                                tableRow.addView(textView1);

                                TextView textView2 = new TextView(ValueActivity.this);
                                textView2.setText(String.valueOf(plantVals.get(j).getPlvVal()));
                                tableRow.addView(textView2);
                            }
                        }

                        if (layout4.getChildCount() > 1) {
                            layout4.removeViews(1, 7);
                        }
                        for (int j = 0; j < 28; j++) {

                            if (plantVals.get(j).getPlvType().equals("압력")) {

                                TableRow tableRow = new TableRow(ValueActivity.this);
                                layout4.addView(tableRow);

                                TextView textView1 = new TextView(ValueActivity.this);

                                textView1.setText(String.valueOf(plantVals.get(j).getPlvDateTime()));
                                tableRow.addView(textView1);

                                TextView textView2 = new TextView(ValueActivity.this);
                                textView2.setText(String.valueOf(plantVals.get(j).getPlvVal()));
                                tableRow.addView(textView2);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


}
