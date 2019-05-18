package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LoadingSheetActivity extends AppCompatActivity {
    private Intent intent;
    private List<String> note;
    private String sheet;

    private Thread t;

    private TextView TextViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_sheet);

        TextViewNote = (TextView)findViewById(R.id.TextViewNote);

        intent = getIntent();

        note = intent.getStringArrayListExtra("Note");

        Log.e("note",note.toString());

        char space = ' ';
        int count = 0; // 마디세기
        int start = 0; // 처음 음표 받아오기
        int input = 0; // 입력 음표 세기



        note = eraseBlank(note);
        note = makeNote(note);
        sheet = divisionNode(note);

//        sheet += note;
//        sheet += note;

        //        //녹음 시작 후 첫 공백 날리기
        //        for(int j = 0 ; j <note.length() ; j++){
        //            if (note.charAt(j) == 'X'){
        //                start++;
        //            }else{
        //                break;
        //            }
        //
        //        }

        //한 마디가 10
//        for(int i = start ; i < note.length() ; i++) {
//            if (count != 10) { //마디가 끝나지 않은 경우
//                if (note.charAt(i) == 'X' && input == 1) {
//                    if(note.charAt(i) == note.charAt(i-1)){
//                        input = 0;
//                        sheet += "z";
//                        sheet += space;
//                        count++;
//                    }else{
//                        input = 0;
//                        //pass
//                    }
//                } else if (note.charAt(i) == 'X' && input < 1){
//                    if(note.charAt(i) == note.charAt(i-1)) {
//                        //sheet += "z";
//                        //sheet += space;
//                        input++;
//                        //count++;
//                    }else {
//                        //pass
//                    }
//
//                } else if (note.charAt(i) != 'X' && input  == 1){
//                    if (note.charAt(i) == note.charAt(i-1)){
//                        input = 0;
//                        sheet += note.charAt(i);
//                        sheet += space;
//                        count++;
//                    }else{
//                        input = 0;
//                        //pass
//                    }
//                } else if (note.charAt(i) != 'X' && input < 1){
//                    if(i==start){
//                       // input++; //pass
//                    }else if(note.charAt(i) == note.charAt(i-1)) {
//                        input++;
//                        //sheet += note.charAt(i);
//                        //sheet += space;
//                        //count++;
//                    }else if(note.charAt(i) != note.charAt(i-1)){
//                        //pass
//                    }
//                }
//            }else if (count == 10) { //마디가 끝난 경우
//                if (note.charAt(i) == 'X' && input == 1) {
//                    if(note.charAt(i) == note.charAt(i-1)){
//                        input = 0;
//                        sheet += "|";
//                        sheet += "z";
//                        sheet += space;
//                        count = 0;
//                    }else{
//                        input = 0;
//                        //pass
//                    }
//                } else if (note.charAt(i) == 'X' && input < 1){
//                    if(note.charAt(i) == note.charAt(i-1)) {
//                        input++;
//                       // sheet += "|";
//                        //sheet += "z";
//                        //sheet += space;
//                        //count = 0;
//                    }else{
//                        //pass
//                    }
//                } else if (note.charAt(i) != 'X' && input  == 1){
//                    if (note.charAt(i) == note.charAt(i-1)){
//                        input = 0;
//                        sheet += "|";
//                        sheet += note.charAt(i);
//                        sheet += space;
//                        count = 0;
//                    }else{
//                        input = 0;
//                        //pass
//                    }
//                }else if (note.charAt(i) != 'X' && input < 1){
//                    if (note.charAt(i) == note.charAt(i-1)) {
//                        input++;
//                        //sheet += note.charAt(i);
//                        //sheet += space;
//                        //count++;
//                    }else{
//                        //pass;
//                    }
//                }
//
//            }
//        }

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

    // 첫 시작 공백 지우기
    private List<String> eraseBlank(List<String> note){

        List<String> eraseBlank = new ArrayList<String>();
        int i = 0;

        //녹음 시작 후 첫 공백 날리기
        for(i = 0 ; i < note.size() ; i++){
            if (!note.get(i).equals("z")){
                break;
            }
        }
        while(i < note.size()){
            eraseBlank.add(note.get(i));
            i++;
        }

//        Log.e("after",note.toString());
//        Log.e("before",eraseBlank.toString());

        return eraseBlank;
    }

    // 음표와 쉼표 만들기
    private List<String> makeNote(List<String> note){
        List<String> makeNote = new ArrayList<String>();
        String currentNote = "";
        // char space = ' ';
        int count = 0; // 마디세기

        currentNote = note.get(0);
        for(int i = 1 ; i < note.size() ; i++){
            // 첫음 3개당 8분음표 하나
            if(!currentNote.equals(note.get(i))){
                // 앞뒤가 다른지
                // 앞에 3개 확인하기
                if( i+2 < note.size() && !currentNote.equals(note.get(i)) && !currentNote.equals(note.get(i+1)) && !currentNote.equals(note.get(i+2)) ){
                    // 음표 만들기
                    if( 3 <= count && count <= 24){
                        makeNote.add(currentNote + String.valueOf(count/3));
                    }
                    count = 0;
                    currentNote = note.get(i);
                }else{
                    count++;
                }

            }else if(currentNote.equals(note.get(i))) {
                count++;
            }
            // 음표 만들기
        }

        Log.e("makeNote before", note.toString());
        Log.e("makeNote after" , makeNote.toString());

        return makeNote;
    }

    // 마디 나누기
    private String divisionNode(List<String> note){
        String sheet = "|";
        String currentNote;
        int currentBeat;
        int line = 0;
        int sum = 0;


        // 한마디에 sum 8
        for(int i = 0 ; i < note.size() ; i++){

            currentNote = note.get(i);
            if(currentNote.length() == 2){
                currentBeat = Character.getNumericValue(currentNote.charAt(1));
                currentNote = currentNote.charAt(0)+"";
            }else{
                currentBeat = Character.getNumericValue(currentNote.charAt(2));
                currentNote = currentNote.charAt(0)+""+currentNote.charAt(1);
            }
            sum += currentBeat;
            if(sum < 8){
                sheet += currentNote + Integer.toString(currentBeat) + " ";
            }else if(sum == 8){
                sheet += currentNote + Integer.toString(currentBeat) + "|";
                line ++;
                if(line==3){
                    sheet+= "\n";
                    line = 0;
                }
                sum = 0;
            }else if(8 < sum){
                int temp = sum - 8;
                sheet += currentNote + Integer.toString(currentBeat-temp) + "|";
                line ++;
                if(line == 3){
                    sheet+= "\n";
                    line = 0;
                }
                sheet += currentNote + Integer.toString(temp) + " ";
                sum = temp;
            }
        }

        if(sum != 0){
            sheet += "z"+Integer.toString(8-sum);
        }

        if(sheet.charAt(sheet.length()-1) == '|'){
            sheet += "|";
        }else{
            sheet += "||";
        }

        Log.e("before",note.toString());
        Log.e("after",sheet);

        return sheet;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
