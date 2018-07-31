package com.farmstory.iot2.project_farm;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectMenuActivity extends AppCompatActivity {

    Button mplantSelected;
    Button mDiarySelected;
    Button mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);

        getSupportActionBar().setTitle("FarmStory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


     mplantSelected=(Button) findViewById(R.id.plantSelected);
     mDiarySelected = findViewById(R.id.diarySelected);
     mValue = findViewById(R.id.value);

        mplantSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent selectPlant;
                selectPlant = new Intent(v.getContext(), FarmListActivity.class);
                startActivity(selectPlant);

            }
        });

        mValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent valueView;
                valueView = new Intent(v.getContext(), ValueActivity.class);
                startActivity(valueView);
            }
        });

    }

}
