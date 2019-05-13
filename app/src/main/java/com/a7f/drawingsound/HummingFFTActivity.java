package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a7f.drawingsound.lib.RecordAudio;

public class HummingFFTActivity extends AppCompatActivity {
    // layout element
    Button ButtonStart;
    Button ButtonReset;
    Button ButtonPlay;
    Button ButtonApply;

    RecordAudio recordTask;

    boolean backFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humming_fft);

        recordTask = new RecordAudio((TextView)findViewById(R.id.TextViewFFT));

        setHandler();
    }

    private void setHandler(){
        ButtonStart = (Button)findViewById(R.id.ButtonStart);
        ButtonReset = (Button)findViewById(R.id.ButtonReset);
        ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);

        ButtonStart.setOnClickListener(StartClickListener);
        ButtonReset.setOnClickListener(ResetClickListener);
        //ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);

        ButtonReset.setEnabled(false);
        ButtonPlay.setEnabled(false);
        ButtonApply.setEnabled(false);

        backFlag = true;
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(recordTask.getStarted()){
                Log.d("Buttonstop", "click success");
                recordTask.setStarted(false);
                ButtonStart.setText("Start");
                recordTask.cancel(true);

                ButtonStart.setEnabled(false);
                ButtonReset.setEnabled(true);
                ButtonApply.setEnabled(true);
                backFlag = true;

            }else{
                recordTask.setStarted(true);
                ButtonStart.setText("Stop");
                recordTask.execute();
                backFlag = false;
            }
        }
    };

    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recordTask = null;
            ButtonStart.setEnabled(true);
            ButtonReset.setEnabled(false);
            ButtonPlay.setEnabled(false);
            ButtonApply.setEnabled(false);
            recordTask = new RecordAudio((TextView)findViewById(R.id.TextViewFFT));
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(HummingFFTActivity.this, LoadingSheetActivity.class);
            finish();
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        if(backFlag){
            super.onBackPressed();
        }else{
            //
            Toast.makeText(getApplicationContext(),"녹음 중 입니다. 종료 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
        }

    }
}
