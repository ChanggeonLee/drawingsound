package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a7f.drawingsound.lib.RecordAudio;

import java.util.ArrayList;
import java.util.List;

public class HummingFFTActivity extends AppCompatActivity {
    // layout element
    Button ButtonStart;
    Button ButtonReset;
    //Button ButtonPlay;
    Button ButtonApply;
    TextView TextViewHumDescription;
    ImageView ImageViewHumIcon;

    RecordAudio recordTask;

    boolean backFlag;

    private List<String> note;

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
        //ButtonPlay = (Button)findViewById(R.id.ButtonPlay);
        ButtonApply = (Button)findViewById(R.id.ButtonApply);
        TextViewHumDescription = (TextView)findViewById(R.id.TextViewHumDescription);
        ImageViewHumIcon = (ImageView)findViewById(R.id.ImageViewHumIcon);

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
    }

    Button.OnClickListener StartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(recordTask.getStarted()){
                Log.d("Buttonstop", "click success");
                recordTask.setStarted(false);
                ButtonStart.setText("허밍 시작하기");
                TextViewHumDescription.setText("허밍이 성공적으로 종료되었습니다!\n악보를 생성해 볼까요?");
                ImageViewHumIcon.setImageResource(R.drawable.ic_humming_start);

                recordTask.cancel(true);

                ButtonStart.setEnabled(false);
                ButtonReset.setEnabled(true);
                ButtonApply.setEnabled(true);

                ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
                ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
                ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkDark));

                note = recordTask.getNoteData();

                backFlag = true;
            }else{
                recordTask.setStarted(true);
                ButtonStart.setText("허밍 종료하기");
                TextViewHumDescription.setText("입술을 다문채로\n음을 흥얼거려주세요!");
                ImageViewHumIcon.setImageResource(R.drawable.ic_humming_ing);
                try{
                    recordTask.execute();
                }catch(Exception e){
                    //
                }
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
            //ButtonPlay.setEnabled(false);
            ButtonApply.setEnabled(false);

            TextViewHumDescription.setText("허밍 시작하기 버튼을 누른 뒤\n허밍을 시작해주세요!");
            ImageViewHumIcon.setImageResource(R.drawable.ic_humming_start);

            ButtonStart.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueDark));
            ButtonReset.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoBlueLight));
            ButtonApply.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.indigoPinkLight));

            recordTask = new RecordAudio((TextView)findViewById(R.id.TextViewFFT));
        }
    };

    Button.OnClickListener ApplyClickListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if(!note.isEmpty()) {
                Intent intent = new Intent(HummingFFTActivity.this, LoadingSheetActivity.class);
                intent.putStringArrayListExtra("Note", (ArrayList<String>) note);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"인식된 음이 없습니다. 다시 시도하세요",Toast.LENGTH_SHORT).show();
            }
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
