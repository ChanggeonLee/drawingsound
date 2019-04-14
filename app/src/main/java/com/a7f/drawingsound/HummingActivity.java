package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HummingActivity extends AppCompatActivity {
    Button ButtonStart;
    Button ButtonReset;
    Button ButtonPlay;
    Button ButtonApply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humming);

        ButtonStart = (Button)findViewById(R.id.ButtonLogin);
        ButtonReset = (Button)findViewById(R.id.ButtonReset);
        ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);

//        ButtonStart.setOnClickListener(StartClickListener);
//        ButtonReset.setOnClickListener(ResetClickListener);
//        ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        public void onClick(View v){
            //
        }
    };

    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //
        }
    };

    Button.OnClickListener PlayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent=new Intent(HummingActivity.this,SheetActivity.class);
            startActivity(intent);
        }
    };
}
