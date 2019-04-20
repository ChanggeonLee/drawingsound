package com.a7f.drawingsound;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class HummingActivity extends AppCompatActivity {
    Button ButtonStart;
    Button ButtonReset;
    Button ButtonPlay;
    Button ButtonApply;

    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private MediaPlayer mediaPlayer;

    private Random random ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humming);


        random = new Random();

        ButtonStart = (Button)findViewById(R.id.ButtonStart);
        ButtonReset = (Button)findViewById(R.id.ButtonReset);
        ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);

        ButtonReset.setEnabled(false);
        ButtonPlay.setEnabled(false);
        ButtonApply.setEnabled(false);

        ButtonStart.setOnClickListener(StartClickListener);
        ButtonReset.setOnClickListener(ResetClickListener);
        ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        public void onClick(View v){
            String filename = "/recording" + random.nextInt(100) + ".3gp";
            Log.d("filename",filename);

            if(ButtonStart.getText().equals("Start")){

                try {
                    // outputFile 생성

                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + filename;

                    // mediarecoder
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);

                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise){
                    //
                } catch (IOException ioe){
                    //
                }
                ButtonStart.setText("Stop");
                ButtonStart.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

            }else if(ButtonStart.getText().equals("Stop")){

                try{
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                } catch(RuntimeException e){
                    Toast.makeText(getApplicationContext(), "Audio Recorder stop error", Toast.LENGTH_LONG).show();
                }
                ButtonStart.setText("Start");
                ButtonStart.setEnabled(false);
                ButtonReset.setEnabled(true);
                ButtonPlay.setEnabled(true);
                ButtonApply.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
            }

        }
    };

    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            ButtonApply.setEnabled(false);
            ButtonReset.setEnabled(false);
            ButtonStart.setEnabled(true);
            ButtonPlay.setEnabled(false);
        }
    };

    Button.OnClickListener PlayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(ButtonPlay.getText().equals("Play")){
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    ButtonPlay.setText("PlayStop");
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    //
                }
            }else if(ButtonPlay.getText().equals("PlayStop")){
                try {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    ButtonPlay.setText("Play");
                } catch (Exception e){
                    //
                }
            }
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent=new Intent(HummingActivity.this,SheetActivity.class);
            intent.putExtra("outputName",outputFile);
            startActivity(intent);
        }
    };
}
