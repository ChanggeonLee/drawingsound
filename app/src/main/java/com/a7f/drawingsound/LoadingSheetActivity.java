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

//        note = eraseBlank(note);
        note = divRest(note , 3);
        note = divNote(note , 3);
        note = eraseX(note);
        note = eraseBlank(note);
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
            if (note.get(i).charAt(0) != 'z'){
                break;
            }
        }
        while(i < note.size()){
            eraseBlank.add(note.get(i));
            i++;
        }

        Log.e("eraseBlank",eraseBlank.toString());

        return eraseBlank;
    }

    // 음표 구분, 쉼표 넣기
    private List<String> divRest(List<String> note, int beat){
        List<String> divrest = new ArrayList<String>();

        int count = 0; // 마디세기

        if(!NullNote(note)){
            return divrest;
        }

        // 쉼표 구분
        for(int i = 0 ; i < note.size() ; i++) {
            // 음표 구분
            if (note.get(i).equals("z")) {
                count++;
            } else if ( !note.get(i).equals("z") && count >= beat) {
                divrest.add("z"+Integer.toString(count/beat));
                divrest.add(note.get(i));
                count = 0;
            } else if (!note.get(i).equals("z") && 0 < count && count < 3){
                divrest.add("x");
                count = 0;
            } else {
                divrest.add(note.get(i));
            }
        }
        Log.e("note",note.toString());
        Log.e("divrest", divrest.toString());

        return divrest;
    }

    // 음표 박자 넣기
    private List<String> divNote(List<String> note, int beat){
        List<String> divnote = new ArrayList<String>();
        int count = 0; // 마디세기

        if(!NullNote(note)){
            return divnote;
        }

        // 음표 구분
        String currentNote ="";
        currentNote = note.get(0);
        count = 0;
        for(int i = 0 ; i < note.size()-1 ; i++) {
            // 음표 구분
            if(currentNote.equals(note.get(i+1))){
                count++;
            }else if(!currentNote.equals(note.get(i+1))){
                if(currentNote.charAt(0) == 'z' || currentNote.charAt(0) == 'x'){
                    divnote.add(currentNote);
                }else{
                    divnote.add(currentNote + Integer.toString(count/beat));
                }
                currentNote = note.get(i+1);
                count = 0;
            }
        }
        divnote.add(currentNote + Integer.toString(count/beat));

        Log.e("divnote", divnote.toString());

        return divnote;
    }

    // 음표와 쉼표 만들기
    private List<String> eraseX(List<String> note){
        List<String> erasex = new ArrayList<String>();

        if(!NullNote(note)){
            return erasex;
        }

        for(int i = 0 ; i < note.size() ; i++){
            char beat = '0';
            if(note.get(i).length() == 2){
                beat = note.get(i).charAt(1);
            }else if(note.get(i).length() == 3){
                beat = note.get(i).charAt(2);
            }
            if(!note.get(i).equals("x") && beat != '0'){
                erasex.add(note.get(i));
            }
        }

        Log.e("erasex", erasex.toString());

        return erasex;
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

        Log.e("after",sheet);
        return sheet;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
