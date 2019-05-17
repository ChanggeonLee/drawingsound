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

        char space = ' ';
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
            if (count != 10) { //마디가 끝나지 않은 경우
                if (note.charAt(i) == 'X' && input == 1) {
                    if(note.charAt(i) == note.charAt(i-1)){
                        input = 0;
                        sheet += "z";
                        sheet += space;
                        count++;
                    }else{
                        input = 0;
                        //pass
                    }
                } else if (note.charAt(i) == 'X' && input < 1){
                    if(note.charAt(i) == note.charAt(i-1)) {
                        //sheet += "z";
                        //sheet += space;
                        input++;
                        //count++;
                    }else {
                        //pass
                    }

                } else if (note.charAt(i) != 'X' && input  == 1){
                    if (note.charAt(i) == note.charAt(i-1)){
                        input = 0;
                        sheet += note.charAt(i);
                        sheet += space;
                        count++;
                    }else{
                        input = 0;
                        //pass
                    }
                } else if (note.charAt(i) != 'X' && input < 1){
                    if(i==start){
                       // input++; //pass
                    }else if(note.charAt(i) == note.charAt(i-1)) {
                        input++;
                        //sheet += note.charAt(i);
                        //sheet += space;
                        //count++;
                    }else if(note.charAt(i) != note.charAt(i-1)){
                        //pass
                    }
                }
            }else if (count == 10) { //마디가 끝난 경우
                if (note.charAt(i) == 'X' && input == 1) {
                    if(note.charAt(i) == note.charAt(i-1)){
                        input = 0;
                        sheet += "|";
                        sheet += "z";
                        sheet += space;
                        count = 0;
                    }else{
                        input = 0;
                        //pass
                    }
                } else if (note.charAt(i) == 'X' && input < 1){
                    if(note.charAt(i) == note.charAt(i-1)) {
                        input++;
                       // sheet += "|";
                        //sheet += "z";
                        //sheet += space;
                        //count = 0;
                    }else{
                        //pass
                    }
                } else if (note.charAt(i) != 'X' && input  == 1){
                    if (note.charAt(i) == note.charAt(i-1)){
                        input = 0;
                        sheet += "|";
                        sheet += note.charAt(i);
                        sheet += space;
                        count = 0;
                    }else{
                        input = 0;
                        //pass
                    }
                }else if (note.charAt(i) != 'X' && input < 1){
                    if (note.charAt(i) == note.charAt(i-1)) {
                        input++;
                        //sheet += note.charAt(i);
                        //sheet += space;
                        //count++;
                    }else{
                        //pass;
                    }
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
