package com.farmstory.iot2.project_farm;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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


            }
        });

    }

}
