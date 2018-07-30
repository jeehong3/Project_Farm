package com.farmstory.iot2.project_farm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText signupId;
    EditText signupName;
    EditText signupPassword;
    EditText signupConfirm;
    EditText signupPhone;
    EditText signupEmail;
    Button signupSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupId = findViewById(R.id.signupId);
        signupName = findViewById(R.id.signupName);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirm = findViewById(R.id.signupConfirm);
        signupPhone = findViewById(R.id.signupPhone);
        signupEmail = findViewById(R.id.signupEmail);
        signupSubmit = findViewById(R.id.signupSubmit);

        signupSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //유효성 검사
                //boolean checkValue = checkCorrectValues();
                boolean checkValue = true;
                if(checkValue){
                    //회원가입 코드
                    SignUpRequestThread t = new SignUpRequestThread();
                    t.start();
                }else{
                    return;
                }
            }
        });
    }

    class SignUpRequestThread extends Thread {
        @Override
        public void run() {

            try {
                String id =  signupId.getText().toString();
                String name =  signupName.getText().toString();
                String password =  signupPassword.getText().toString();
                String phone =  signupPhone.getText().toString();
                String email = signupEmail.getText().toString();


                String data = "memId=" + id + "&memPw=" + password + "&memName=" + URLEncoder.encode(name, "utf-8") + "&memPhone=" + phone + "&memEmail=" + URLEncoder.encode(email, "utf-8");

                //Get 방식
                URL url = new URL(String.format("http://172.16.6.8:8087/farmstory/msignup.action?%s", data));// URL클래스의 생성자로 주소를 넘겨준다.

                HttpURLConnection con = (HttpURLConnection)url.openConnection();// 해당 주소의 페이지로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.
                con.setRequestMethod("GET");// POST방식으로 요청한다.( 기본값은 GET )
                int responseCode = con.getResponseCode();
                if (responseCode == 200) { // 정상 응답일 경우
                    BufferedReader br = new BufferedReader( new InputStreamReader( con.getInputStream(), "utf-8" ), con.getContentLength() );
                    String result = br.readLine();
                    br.close();


                    if (result.equals("success")) {
                        //회원가입 성공 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUpActivity.this,"회원가입에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(SignUpActivity.this, AccountActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        //회원가입 실패 코드
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "해당 ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "해당 ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public boolean checkCorrectValues(){
        String id =  signupId.getText().toString();
        String name =  signupName.getText().toString();
        String password =  signupPassword.getText().toString();
        String confirm =  signupConfirm.getText().toString();
        String phone =  signupPhone.getText().toString();
        String email =  signupEmail.getText().toString();

        //아이디 유효성 검사
        if(!Pattern.matches("^[A-Za-z0-9]{4,10}$", id)) {
            Toast.makeText(SignUpActivity.this,"아이디 형식을 확인해 주세요.",Toast.LENGTH_SHORT).show();
            signupId.setText("");
            return false;
        }

        //이름 유효성 검사
        if(!Pattern.matches("^[가-힣]{2,10}$", name)) {
            Toast.makeText(SignUpActivity.this,"이름 형식을 확인해 주세요.",Toast.LENGTH_SHORT).show();
            signupName.setText("");
            return false;
        }

        //비밀번호 유효성 검사
        if(!password.equals(confirm)){
            Toast.makeText(SignUpActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            signupPassword.setText("");
            signupConfirm.setText("");
            return false;
        }else{
            if(!Pattern.matches("^[A-Za-z0-9]{6,20}$", password)) {
                Toast.makeText(SignUpActivity.this,"비밀번호 형식을 확인해 주세요.",Toast.LENGTH_SHORT).show();
                signupPassword.setText("");
                signupConfirm.setText("");
                return false;
            }
        }

        //전화번호 유효성 검사
        if(!Pattern.matches("^\\d{3}-\\d{4}-\\d{4}$", phone)) {
            Toast.makeText(SignUpActivity.this,"전화번호 형식을 확인해 주세요.",Toast.LENGTH_SHORT).show();
            signupPhone.setText("");
            return false;
        }


        //주소 유효성 검사
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this,"이메일 형식을 확인해 주세요.",Toast.LENGTH_SHORT).show();
            signupEmail.setText("");
            return false;
        }

        return true;
    }

}
