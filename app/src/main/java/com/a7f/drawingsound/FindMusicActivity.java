package com.a7f.drawingsound;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaPlayer;
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

import com.a7f.drawingsound.lib.RecordAudio;
import com.a7f.drawingsound.lib.RecordAudioToWAV;
import com.a7f.drawingsound.lib.SendToServer;
import com.deskode.recorddialog.RecordDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class FindMusicActivity extends AppCompatActivity {
    final private static String RECORDED_FILE = "/sdcard/recorede.mp4";
    MediaPlayer mPlayer;
    MediaPlayer mediaPlayer;
    Recorder recorder;
    private String _AudioSavePathInDevice = null;
//    Button button;
//
//    Button button2;
//    Button button3;
    String result;
    RecordDialog recordDialog;
    //
    // layout element


    //
    Button ButtonStart;
    Button ButtonReset;

    //Button ButtonPlay;
    Button ButtonApply;
    TextView TextViewFindDescription;
    ImageView ImageViewHumIcon;

    RecordAudioToWAV recordTask;

    private FirebaseAuth mAuth;

    boolean backFlag;
    boolean completeFlag;
    private List<String> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);
        setTitle("");

        //recordTask = new RecordAudioToWAV((TextView) findViewById(R.id.TextViewFFT));

        setHandler();
        //setupRecorder();

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(FindMusicActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("비슷한 음악 찾는 중");

            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
            Intent intent = new Intent(FindMusicActivity.this, FindResultActivity.class);
            //intent.putStringArrayListExtra("Note", (ArrayList<String>) note);
            finish();
            startActivity(intent);
        }
    }

    private void setHandler() {
//        button = (Button) findViewById(R.id.button);
//        button2 = (Button) findViewById(R.id.button2);
//        button3 = (Button) findViewById(R.id.button3);
//        button.setOnClickListener(testStartListener);
//        button2.setOnClickListener(testStopListener);
//        button3.setOnClickListener(wavTestListener);

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

    ////////////////////////////////////


    private void setupRecorder() {
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                    }
                }), file());
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    @NonNull
    private File file() {
        //@SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "find_music.wav");
        _AudioSavePathInDevice = file.getPath();
        return file;
    }

    public String getAudioPath() {
        return _AudioSavePathInDevice;
    }


    private void startMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(_AudioSavePathInDevice);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMediaPlayer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //_recordButton.setImageResource(R.drawable.ic_pause);
        //STATE_BUTTON = "PLAY";
        //playerSecondsElapsed = 0;
        //startTimer();
        mediaPlayer.start();
    }


    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            //_recordButton.setImageResource(R.drawable.ic_play);
            //STATE_BUTTON = "STOP";
            //_timerView.setText("00:00:00");
            //stopTimer();
        }
    }


    ////////////////////////////////생성
    Button.OnClickListener wavTestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setupRecorder();

            recorder.startRecording();
//            try {
//                mPlayer = MediaPlayer.create(getContext(), R.raw.hangouts_message);
//                mPlayer.start();
//                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        recorder.startRecording();
//                        //startTimer();
//                    }
//                });
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }


            //startMediaPlayer(); // 아 이거는 음악 재생인가본데
            Toast.makeText(FindMusicActivity.this, "녹음 시작합니다~!", Toast.LENGTH_LONG).show();
            //recordDialog = RecordDialog.newInstance("Record Audio");
            //recordDialog.setMessage("press for record");
            //recordDialog.show(FindMusicActivity.this.getFragmentManager(),"TAG");
//            recordDialog.setPositiveButton("Save", new RecordDialog.ClickListener() {
//                @Override
//                public void OnClickListener(String path) {
//                    Toast.makeText(FindMusicActivity.this,"Save audio:"+path,Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };


    // 중지
    Button.OnClickListener testStartListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                recorder.stopRecording();
                //mPlayer = MediaPlayer.create(getContext(), R.raw.pop);
                //mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(FindMusicActivity.this, "녹음 중지~!", Toast.LENGTH_LONG).show();
            //stopMediaPlayer(); // 이것도 음악 재생 중지인가본대
        }

    };

    //저장
    Button.OnClickListener testStopListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //stopMediaPlayer();

            Toast.makeText(FindMusicActivity.this, "Save audio:" + getAudioPath(), Toast.LENGTH_LONG).show();
//
        }
//        @Override
//        public void onClick(View v) {
//            if(recorder==null)
//                return;
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//
//            Toast.makeText(getApplicationContext(),"녹음 중지",Toast.LENGTH_LONG).show();
//        }
    };

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            setupRecorder();

            recorder.startRecording();
            Toast.makeText(FindMusicActivity.this, "녹음 시작합니다~!", Toast.LENGTH_LONG).show();

            //if(recordTask.getStarted()){
            Log.d("Buttonstop", "click success");
            //recordTask.setStarted(false);
            ButtonStart.setText("녹음 시작하기");
            TextViewFindDescription.setText("녹음이 성공적으로 완료되었습니다!\n노래를 검색해 볼까요?");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_start);

            // recordTask.cancel(true);

            ButtonStart.setEnabled(false);
            ButtonReset.setEnabled(true);
            ButtonApply.setEnabled(false);

            ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
            ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
            ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkLight));

            //backFlag = false;
        }

    };

    Button.OnClickListener ResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            recordTask = null;
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

//            recordTask = new RecordAudioToWAV((TextView)findViewById(R.id.TextViewFFT));
            try {
                recorder.stopRecording();
                //mPlayer = MediaPlayer.create(getContext(), R.raw.pop);
                //mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(FindMusicActivity.this,"녹음 중지~!",Toast.LENGTH_LONG).show();
            ButtonStart.setText("녹음 다시하기");
            TextViewFindDescription.setText("궁금하신 노래를\n불러주세요!");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_ing);
            //backFlag = false;
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {


        // 여기다가 서버에 던지는 코드 주기
        @Override
        public void onClick(View v) {
//            if(!note.isEmpty()) {
//                recordTask.transferToWAV();
                //result = SendToServer.sendWav();
                Toast.makeText(FindMusicActivity.this,"Save audio:"+getAudioPath(),Toast.LENGTH_LONG).show();
                // 여기다가 이제 서버에 던지는 코드 줘야됨
                CheckTypesTask task = new CheckTypesTask();
                task.execute();

                Intent intent = new Intent(FindMusicActivity.this, FindResultActivity.class);
//                //intent.putStringArrayListExtra("Note", (ArrayList<String>) note);
//                finish();
//                startActivity(intent);

//            }else{
//                Toast.makeText(getApplicationContext(),"인식된 음이 없습니다. 다시 시도하세요",Toast.LENGTH_SHORT).show();
//            }

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