package com.farmstory.iot2.project_farm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.lang.reflect.Member;

public class MainActivity extends AppCompatActivity {

    private Button mUseAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageView image = new ImageView(this);
//        image.setImageResource(R.drawable.main);
//        setContentView(image);

//        Drawable alpha = ((ScrollView)findViewById(R.id.mainLayOut)).getBackground();
//
//        alpha.setAlpha(50);

        getSupportActionBar().setTitle("FarmStory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Member member = (Member) intent.getSerializableExtra("member");

        mUseAppButton = findViewById(R.id.useAppButton);

        mUseAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(v.getContext(), AccountActivity.class);
                startActivity(account);
            }
        });
    }
}
