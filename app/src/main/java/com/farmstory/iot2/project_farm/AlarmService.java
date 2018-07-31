package com.farmstory.iot2.project_farm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;

import vo.Account;
import vo.Alarm;

public class AlarmService extends Service {

    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean isRunning() {
        return running;
    }

    public void doStop() {
        running = false;
    }

    private Account member;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        member = (Account) intent.getSerializableExtra("member");

        Thread t = new Thread() {
            @Override
            public void run() {
                running = true;
                while (running) {
                    try {
                        URL url = new URL("http://172.16.6.8:8087/farmstory/value/mFindAlarms.action?id=" + member.getMemId());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");

                        int responseCode = con.getResponseCode();
                        if (responseCode == 200) {

                            processResult(con);
                        } else {

                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();

        return Service.START_NOT_STICKY;

    }

    private void processResult(HttpURLConnection conn) {

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
                final Alarm val = gson.fromJson(element, Alarm.class); // JSON 객체 VO 객체로 직접 변환
                if (val.getValType().equals("압력")) {

                    Intent intent = new Intent(AlarmService.this, AlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pIntent = PendingIntent.getActivity(AlarmService.this, 0, intent, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("화분이 넘어졌어요")
                            .setContentTitle("화분이 아파요")
                            .setContentText("세워주세요")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true)
                            .build();

                    //notification.notify();
                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(1, notification);


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
