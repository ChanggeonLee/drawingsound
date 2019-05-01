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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class HummingActivity extends AppCompatActivity {

    // layout element
    Button ButtonStart;
    Button ButtonReset;
    Button ButtonPlay;
    Button ButtonApply;

    // 실시간으로 FFT 적용된걸 보여줌
    ImageView ImageViewFFT;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;

    // recorder setting
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private MediaPlayer mediaPlayer;

    // file name randomize
    private Random random;

    // FFT setting
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;

    int blockSize = 256;
    boolean started = false;
    RecordAudio recordTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humming);

        setHandler();
    }

    private void setFFT(){
        // FFT setting
        transformer = new RealDoubleFFT(blockSize);

        // FFT 적용을 실시간으로 보여주기 위해서 설정
        ImageViewFFT = (ImageView)findViewById(R.id.ImageViewFFT);
        bitmap = Bitmap.createBitmap((int)256, (int)100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        ImageViewFFT.setImageBitmap(bitmap);
    }

    private void setHandler(){
        random = new Random();

        ButtonStart = (Button)findViewById(R.id.ButtonStart);
        ButtonReset = (Button)findViewById(R.id.ButtonReset);
        ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);

        ButtonStart.setOnClickListener(StartClickListener);
        ButtonReset.setOnClickListener(ResetClickListener);
        ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);


        ButtonReset.setEnabled(false);
        ButtonPlay.setEnabled(false);
        ButtonApply.setEnabled(false);
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        public void onClick(View v){
            String filename = "/recording" + random.nextInt(100) + ".mp3";
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
            Intent intent=new Intent(HummingActivity.this, LoadingSheetActivity.class);
            intent.putExtra("outputName",outputFile);
            startActivity(intent);
        }
    };


    private class RecordAudio extends AsyncTask<Void, double[], Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                // AudioRecord를 설정하고 사용한다.
                int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);
                // short로 이뤄진 배열인 buffer는 원시 PCM 샘플을 AudioRecord 객체에서 받는다.
                // double로 이뤄진 배열인 toTransform은 같은 데이터를 담지만 double 타입인데, FFT 클래스에서는 double타입이 필요해서이다.
//                short[] buffer = new short[blockSize];
                byte[] buffer = new byte[bufferSize];

                double[] toTransform = new double[blockSize];

                String filename = "/recording" + random.nextInt(100) + ".mp3";
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + filename;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                } catch(FileNotFoundException e) {
                    Log.e("File IO", "cannot open");
                    e.printStackTrace();
                }


                audioRecord.startRecording();
                while (started) {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);


                    try {

                        fos.write(buffer, 0, bufferSize);    //  읽어온 readData 를 파일에 write 함
                    }catch (IOException e){
                        Log.e("File IO", "input write");
//                        e.printStackTrace();
                    }


//                    // for FFT
//                    // AudioRecord 객체에서 데이터를 읽은 다음에는 short 타입의 변수들을 double 타입으로 바꾸는 루프를 처리한다.
//                    // 직접 타입 변환(casting)으로 이 작업을 처리할 수 없다. 값들이 전체 범위가 아니라 -1.0에서 1.0 사이라서 그렇다
//                    // short를 32,768.0(Short.MAX_VALUE) 으로 나누면 double로 타입이 바뀌는데, 이 값이 short의 최대값이기 때문이다.
//                    for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
//                        toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
//                    }
//
//                    // 이제 double값들의 배열을 FFT 객체로 넘겨준다. FFT 객체는 이 배열을 재사용하여 출력 값을 담는다. 포함된 데이터는 시간 도메인이 아니라
//                    // 주파수 도메인에 존재한다. 이 말은 배열의 첫 번째 요소가 시간상으로 첫 번째 샘플이 아니라는 얘기다. 배열의 첫 번째 요소는 첫 번째 주파수 집합의 레벨을 나타낸다.
//                    // 256가지 값(범위)을 사용하고 있고 샘플 비율이 8,000 이므로 배열의 각 요소가 대략 15.625Hz를 담당하게 된다. 15.625라는 숫자는 샘플 비율을 반으로 나누고(캡쳐할 수 있는
//                    // 최대 주파수는 샘플 비율의 반이다. <- 누가 그랬는데...), 다시 256으로 나누어 나온 것이다. 따라서 배열의 첫 번째 요소로 나타난 데이터는 영(0)과 15.625Hz 사이에
//                    // 해당하는 오디오 레벨을 의미한다.
//                    transformer.ft(toTransform);
//                    // publishProgress를 호출하면 onProgressUpdate가 호출된다.
//                    publishProgress(toTransform);
                }
                audioRecord.stop();

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("File IO", "input Failed");
                }

            } catch (Throwable t) {
                  Log.e("AudioRecord", "Recording Failed");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... toTransform) {
            canvas.drawColor(Color.BLACK);

            for(int i = 0; i < toTransform[0].length; i++){
                int x = i;
                int downy = (int) (100 - (toTransform[0][i] * 10));
                int upy = 100;
                canvas.drawLine(x, downy, x, upy, paint);
            }

            ImageViewFFT.invalidate();
        }

    }

}
