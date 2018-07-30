package com.farmstory.iot2.project_farm;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import vo.Account;

public class AccountActivity extends AppCompatActivity {

    //로그인 버튼
    private Button mSignInButton;
    private Button mSignUpButton;
    private EditText mId;
    private EditText mPassword;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        getSupportActionBar().setTitle("FarmStory");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSignInButton = (Button) findViewById(R.id.signInButton);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mId = findViewById(R.id.inputId);
        mPassword = findViewById(R.id.inputPw);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInRequestThread t = new SignInRequestThread();
                t.start();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    class SignInRequestThread extends Thread {
        @Override
        public void run() {

            try {
                String id = mId.getText().toString();
                String password = mPassword.getText().toString();

                //Get 방식
                URL url = new URL(String.format("http://172.16.6.8:8087/farmstory/msignin.action?id="+id+"&password="+password));// URL클래스의 생성자로 주소를 넘겨준다.
                HttpURLConnection con = (HttpURLConnection) url.openConnection();// 해당 주소의 페이지
                // 로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.
                con.setRequestMethod("GET");// POST방식으로 요청한다.( 기본값은 GET )
                int responseCode = con.getResponseCode();
                if (responseCode == 200) { // 정상 응답일 경우
                    boolean result = processResult(con);

                    if (result) {
                        //로그인 성공 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                Intent intent = new Intent(AccountActivity.this, SelectMenuActivity.class);
                                intent.putExtra("member", account);
                                startActivity(intent);
                            }
                        });
                    } else {
                        //로그인 실패 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    //show error message
                    Toast.makeText(getApplicationContext(),
                            "error " + responseCode, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            //handler.sendEmptyMessage(1);
        }

        private boolean processResult(HttpURLConnection conn) {
            boolean result = true;
            try {
                //JSON 문자열 -> 객체 트리로 변환하는 변환기 만들기
                InputStream is = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                JsonParser parser = new JsonParser();

                //변환 처리 -> JsonElement 반환
                JsonElement je = parser.parse(reader);
                JsonObject element = je.getAsJsonObject();
                if (element.get("result") == null) {
                    Gson gson = new Gson();
                    account = gson.fromJson(element, Account.class); // JSON 객체 VO 객체로 직접 변환
                } else {
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                result = false;
            }
            return result;
        }
    }
}
