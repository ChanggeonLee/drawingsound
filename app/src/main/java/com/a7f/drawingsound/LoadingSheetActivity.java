package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoadingSheetActivity extends AppCompatActivity {
    Intent intent;
    String note;
    String sheet;

    Thread t;

    TextView TextViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_sheet);

        TextViewNote = (TextView)findViewById(R.id.TextViewNote);

        intent = getIntent();
        note = intent.getExtras().getString("Note");

        TextViewNote.setText(note);

        char temp = ' ';
        int count = 0; // 마디세기
        int start = 0; // 처음 음표 받아오기
        int input = 0; // 입력 음표 세기

        sheet = "|:";

        //녹음 시작 후 첫 공백 날리기
        for(int j = 0 ; j <note.length() ; j++){
            if (note.charAt(j) == 'X'){
                start++;
            }else{
                break;
            }

        }


        //한 마디가 10
        for(int i = start ; i < note.length() ; i++) {
            if (count != 10 && note.charAt(i) != 'z') {
                if (note.charAt(i) == 'X') {
                    sheet += "z";
                    sheet += temp;
                    count++;
                } else{
                    sheet += note.charAt(i);
                    count++;
                    sheet +=temp;
                }
            }else if (count == 10) {
                if (note.charAt(i) == 'X') {
                    sheet += "|";
                    sheet += "z";
                    sheet += temp;
                    count = 0;
                } else {
                    sheet += "|";
                    sheet += note.charAt(i);
                    sheet += temp;
                    count = 0;
                }
            }
        }



        sheet += ":|";

        TextViewNote.setText(sheet);

        t = new Thread() {
            public void run() {
                try{
                    sleep(1000);
                    intent = new Intent(LoadingSheetActivity.this,SheetSettingActivity.class);
                    intent.putExtra("Sheet",sheet);
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
