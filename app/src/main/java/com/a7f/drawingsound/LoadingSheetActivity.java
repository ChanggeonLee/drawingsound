package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingSheetActivity extends AppCompatActivity {
    Intent intent;
    String outputName;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_sheet);

        intent = getIntent();
        outputName = intent.getStringExtra("outputName");



        t = new Thread() {
            public void run() {
                try{
                    sleep(1000);
                    intent = new Intent(LoadingSheetActivity.this,SheetSettingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    //
                }
            }
        };

        t.start();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
