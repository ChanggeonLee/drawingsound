package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SheetSettingActivity extends AppCompatActivity {

    Button ButtonPrev;
    Button ButtonSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_setting);

        ButtonPrev = (Button)findViewById(R.id.ButtonPrev);
        ButtonSet = (Button)findViewById(R.id.ButtonSet);

        ButtonPrev.setOnClickListener(Prevonclick);
        ButtonSet.setOnClickListener(Setclick);
    }

    Button.OnClickListener Prevonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SheetSettingActivity.this,HummingActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener Setclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SheetSettingActivity.this,SheetListActivity.class);
            startActivity(intent);
        }
    };
}
