package com.a7f.drawingsound;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.a7f.drawingsound.lib.SendToServer;
import com.deskode.recorddialog.Util;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.Timer;
import java.util.TimerTask;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class FindMusicActivity extends AppCompatActivity {

    Recorder recorder;
    private String _AudioSavePathInDevice = null;
    private Timer _timer;
    private int recorderSecondsElapsed=0;
    String result;
    Button ButtonStart;
    Button ButtonReset;

    //Button ButtonPlay;
    Button ButtonApply;
    TextView TextViewFindDescription;
    ImageView ImageViewHumIcon;
    TextView record_time;

    private FirebaseAuth mAuth;

    boolean backFlag;
    boolean completeFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);
        setTitle("");

        setHandler();

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

//    // 진행 상황 나타내는 클래스, progress dialog 출력 부분
//    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
//
//        ProgressDialog asyncDialog = new ProgressDialog(FindMusicActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            asyncDialog.setMessage("비슷한 음악 찾는 중");
//
//            asyncDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            try {
//                for (int i = 0; i < 5; i++) {
//                    Thread.sleep(1000);
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            asyncDialog.dismiss();
//            super.onPostExecute(result);
//            Intent intent = new Intent(FindMusicActivity.this, FindResultActivity.class);
//            finish();
//            startActivity(intent);
//        }
//    }

    private void setHandler() {

        record_time = (TextView)findViewById(R.id.TextViewFFT);
        ButtonStart = (Button) findViewById(R.id.ButtonStart);
        ButtonReset = (Button) findViewById(R.id.ButtonReset);
        //ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button) findViewById(R.id.ButtonApply);
        TextViewFindDescription = (TextView) findViewById(R.id.TextViewFindDescription);
        ImageViewHumIcon = (ImageView) findViewById(R.id.ImageViewHumIcon);

        ButtonStart.setOnClickListener(StartClickListener);
        ButtonReset.setOnClickListener(ResetClickListener);
        //ButtonPlay.setOnClickListener(PlayClickListener);
        ButtonApply.setOnClickListener(ApplyClickListener);

        ButtonReset.setEnabled(false);
        // ButtonPlay.setEnabled(false);
        ButtonApply.setEnabled(false);

        ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
        ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
        ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkLight));

        backFlag = true;
        completeFlag = false;
        mAuth = FirebaseAuth.getInstance();
    }

     // 녹음 시작 누르면 레코딩 준비와 함께 파일 생성
    private void setupRecorder() {
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                    }
                }), file());
    }

    // 주파수나 인코딩 비트 조절
    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    // 파일 생성
    @NonNull
    private File file() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "find_music.wav");
        _AudioSavePathInDevice = file.getPath();
        return file;
    }

    // 파일 경로
    public String getAudioPath() {
        return _AudioSavePathInDevice;
    }

    // 타이머 시작
    private void startTimer(){
        recorderSecondsElapsed = 0;
        stopTimer();
        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    // 타이머 멈춤
    private void stopTimer(){

        if (_timer != null) {
            _timer.cancel();
            _timer.purge();
            _timer = null;
        }
    }

    // 타이머 시간 업데이트
    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    recorderSecondsElapsed++;
                    record_time.setText(Util.formatSeconds(recorderSecondsElapsed));
            }
        });
    }


    // 녹음 시작 버튼 리스터
    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            setupRecorder();
            recorder.startRecording();
            startTimer();

            Log.d("Buttonstop", "click success");
            ButtonStart.setText("녹음 시작하기");
            TextViewFindDescription.setText("녹음 중입니다.\n10초 이상 녹음해주세요!");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_start);

            ButtonStart.setEnabled(false);
            ButtonReset.setEnabled(true);
            ButtonApply.setEnabled(false);

            ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
            ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
            ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkLight));

            backFlag = false;
        }

    };

    // 녹음 종료 버튼 리스너
    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopTimer();

            ButtonStart.setEnabled(true);
            ButtonReset.setEnabled(false);
//            //ButtonPlay.setEnabled(false);
            ButtonApply.setEnabled(true);
//
            TextViewFindDescription.setText("녹음 시작하기 버튼을 누른 뒤\n노래 시작해주세요!");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_start);

            ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
            ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
            ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkDark));

            if(recorderSecondsElapsed<=11){
                ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkLight));
                ButtonApply.setEnabled(false);
            }

            try {
                recorder.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ButtonStart.setText("녹음 다시하기");
            TextViewFindDescription.setText("궁금하신 노래를\n불러주세요!");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_ing);
            backFlag = true;
        }
    };

    // 음악 검색하기 버튼 리스너
    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

                SendToServer sendTask = new SendToServer("find_music.wav");

                try {
                    result = sendTask.execute("http://drawingsound.com/model/musicname").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            Intent intent = new Intent(FindMusicActivity.this, FindResultActivity.class);
                intent.putExtra("result",result);
                startActivity(intent);
                finish();

        }
    };

    @Override
    public void onBackPressed() {
        if(backFlag){
            super.onBackPressed();
        }else{
            //
            Toast.makeText(getApplicationContext(),"녹음 중입니다. 종료 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    private void signOut() {

        new AlertDialog.Builder(this).setTitle("LOGOUT").setMessage("로그아웃 하시겠습니까?").setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent i = new Intent(FindMusicActivity.this,SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout :
                signOut();
                return true ;
            case R.id.action_license :
                Intent intent = new Intent(FindMusicActivity.this,LicenseActivity.class);
                startActivity(intent);
                //finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}