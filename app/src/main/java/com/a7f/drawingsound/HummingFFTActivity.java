package com.a7f.drawingsound;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.actions.NoteIntents;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

import ca.uol.aig.fftpack.Complex1D;
import ca.uol.aig.fftpack.ComplexDoubleFFT;
import ca.uol.aig.fftpack.RealDoubleFFT;
import ca.uol.aig.fftpack.RealDoubleFFT_Even;
import ca.uol.aig.fftpack.RealDoubleFFT_Even_Odd;
import ca.uol.aig.fftpack.RealDoubleFFT_Odd_Odd;

public class HummingFFTActivity extends AppCompatActivity {
    // layout element
    Button ButtonStart;
    Button ButtonReset;
    Button ButtonPlay;
    Button ButtonApply;

    // 실시간으로 FFT 적용된걸 보여줌
    TextView TextViewFFT;

    // FFT setting
    int frequency = 11025;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;

    int blockSize = 1200;
    boolean started = false;
    RecordAudio recordTask;

    private int noteIndex = 0;

    private String[] note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humming_fft);

        setFFT();
        setHandler();
    }

    private void setFFT(){
        // FFT setting
        transformer = new RealDoubleFFT(blockSize);
        TextViewFFT = (TextView)findViewById(R.id.TextViewFFT);

        note = new String[10000];
    }

    private void setHandler(){
        ButtonStart = (Button)findViewById(R.id.ButtonStart);
        ButtonReset = (Button)findViewById(R.id.ButtonReset);
        ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);

        ButtonStart.setOnClickListener(StartClickListener);
        ButtonReset.setOnClickListener(ResetClickListener);
//        ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);

        ButtonReset.setEnabled(false);
        ButtonPlay.setEnabled(false);
        ButtonApply.setEnabled(false);
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(started){
                started = false;
                ButtonStart.setText("Start");
                recordTask.cancel(true);

                ButtonStart.setEnabled(false);
                ButtonReset.setEnabled(true);
                ButtonApply.setEnabled(true);
            }else{
                started = true;
                ButtonStart.setText("Stop");
                recordTask = new RecordAudio();
                recordTask.execute();
            }
        }
    };

    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ButtonStart.setEnabled(true);
            ButtonReset.setEnabled(false);
            ButtonPlay.setEnabled(false);
            ButtonApply.setEnabled(false);
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(HummingFFTActivity.this,LoadingSheet.class);
            finish();
            startActivity(intent);
        }
    };


    private class RecordAudio extends AsyncTask<Void, double[], Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                // AudioRecord를 설정하고 사용한다.
                //int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, blockSize);
                // short로 이뤄진 배열인 buffer는 원시 PCM 샘플을 AudioRecord 객체에서 받는다.
                // double로 이뤄진 배열인 toTransform은 같은 데이터를 담지만 double 타입인데, FFT 클래스에서는 double타입이 필요해서이다.
                short[] buffer = new short[blockSize];
                double[] toTransform = new double[blockSize];

                audioRecord.startRecording();
                while (started) {
                    int bufferReadResult = audioRecord.read(buffer, 0, blockSize);
                    // for FFT
                    // AudioRecord 객체에서 데이터를 읽은 다음에는 short 타입의 변수들을 double 타입으로 바꾸는 루프를 처리한다.
                    // 직접 타입 변환(casting)으로 이 작업을 처리할 수 없다. 값들이 전체 범위가 아니라 -1.0에서 1.0 사이라서 그렇다
                    // short를 32,768.0(Short.MAX_VALUE) 으로 나누면 double로 타입이 바뀌는데, 이 값이 short의 최대값이기 때문이다.
                    for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                        toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
                    }


                    // 이제 double값들의 배열을 FFT 객체로 넘겨준다. FFT 객체는 이 배열을 재사용하여 출력 값을 담는다. 포함된 데이터는 시간 도메인이 아니라
                    // 주파수 도메인에 존재한다. 이 말은 배열의 첫 번째 요소가 시간상으로 첫 번째 샘플이 아니라는 얘기다. 배열의 첫 번째 요소는 첫 번째 주파수 집합의 레벨을 나타낸다.
                    // 256가지 값(범위)을 사용하고 있고 샘플 비율이 8,000 이므로 배열의 각 요소가 대략 15.625Hz를 담당하게 된다. 15.625라는 숫자는 샘플 비율을 반으로 나누고(캡쳐할 수 있는
                    // 최대 주파수는 샘플 비율의 반이다. <- 누가 그랬는데...), 다시 256으로 나누어 나온 것이다. 따라서 배열의 첫 번째 요소로 나타난 데이터는 영(0)과 15.625Hz 사이에
                    // 해당하는 오디오 레벨을 의미한다.
                    transformer.ft(toTransform);
                    // publishProgress를 호출하면 onProgressUpdate가 호출된다.
                    publishProgress(toTransform);
                }
                audioRecord.stop();

            } catch (Throwable t) {
                Log.e("AudioRecord", "Recording Failed");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... toTransform) {
            int max = 0;
            int index = 0;
            Log.d("length",Integer.toString(toTransform[0].length));
            for(int i = 0; i < toTransform[0].length; i++){
                if(max < toTransform[0][i]){
                    max = (int)toTransform[0][i];
                    index = i;
                }
            }
            Log.d("frequency",Double.toString(index*4.6));
            getNote(index*4.6);

        }
    }

    private void getNote(double fre){
        String Note ;
        // 3.3 곱한거에서 +3 -3 범위
        if(250 <= fre && fre <= 269){
            // 261 C4
            Note = "C4";
        }else if(269 < fre && fre <= 285){
            // 277 C#
            Note = "C#";
        }else if(285 < fre && fre <= 301){
            // 293 D
            Note = "D";
        }else if(301 < fre && fre <= 319){
            // 311 D#
            Note = "D#";
        }else if(319 < fre && fre <= 337){
            // 329 E
            Note = "E";
        }else if(337 < fre && fre <= 357){
            // 349 F
            Note = "F";
        }else if(357 < fre && fre <= 377){
            // 369 F#
            Note = "F#";
        }else if(377 < fre && fre <= 399){
            // 391 G
            Note = "G";
        }else if(399 < fre && fre <= 423){
            // 415 G#
            Note = "G#";
        }else if(423 < fre && fre <= 448){
            // 440 A
            Note = "A";
        }else if(448 < fre && fre <= 474){
            // 466 A#
            Note = "A#";
        }else if(474 < fre && fre <= 501){
            // 493 B
            Note = "B";
        }else{
            Note = "X";
        }
        note[noteIndex] = Note;
        noteIndex++;
        Log.d("index",Integer.toString(noteIndex));
        TextViewFFT.setText(Note);

    }

}
