package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoadingSheetActivity extends AppCompatActivity {
    private Intent intent;
    // private List<String> note;
    private String sheet;
    private Thread t;
    private TextView TextViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_sheet);

        List<String> note;

        TextViewNote = (TextView)findViewById(R.id.TextViewNote);

        intent = getIntent();

        note = intent.getStringArrayListExtra("Note");

        note = eraseBlank(note);
        note = makeNote(note,1);
        sheet = divisionNode(note);

        TextViewNote.setText(sheet);

        if(sheet != null){
            intent = new Intent(LoadingSheetActivity.this,SheetSettingActivity.class);
            intent.putExtra("Sheet",sheet);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"인식된 음이 없습니다. 다시 시도하세요",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean NullNote(List<String> note){
        if(note.isEmpty()){
            //  Toast.makeText(HummingFFTActivity.class,"다시 시도해 주세요",Toast.LENGTH_SHORT).show();
            Log.e("empty note",note.toString());
            finish();
            return false;
        }
        return true;
    }

    // 첫 시작 공백 지우기
    private List<String> eraseBlank(List<String> note){

        List<String> eraseBlank = new ArrayList<String>();
        int i = 0;

        if(!NullNote(note)){
            return eraseBlank;
        }

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
    private List<String> makeNote(List<String> note, int beat){
        List<String> makeNote = new ArrayList<String>();
        String currentNote = "";
        // char space = ' ';
        int count = 0; // 마디세기

        if(!NullNote(note)){
            return makeNote;
        }

        currentNote = note.get(0);
        for(int i = 1 ; i < note.size() ; i++){
            // 첫음 3개당 8분음표 하나
            if(!currentNote.equals(note.get(i))){
                // 앞뒤가 다른지
                // 앞에 3개 확인하기
//                 && !currentNote.equals(note.get(i+1)) && !currentNote.equals(note.get(i+2))
                if( i+2 < note.size() && !currentNote.equals(note.get(i)) && !currentNote.equals(note.get(i+1)) ){
//                        || (note.get(i).equals("z")) && !note.get(i+1).equals("z")){
                    // 음표 만들기
                    if( beat <= count && count <= (beat*8) ){
                        makeNote.add(currentNote + String.valueOf(count/beat));
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

        if(!NullNote(note)){
            return null;
        }

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
        //
        //        Log.e("before",note.toString());
        //        Log.e("after",sheet);
        return sheet;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
