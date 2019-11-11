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


        int beat = 2;

        note = combineOctave(note);     // 반음차이 제거
        note = makeNote(note);          // 음표 만들기
        note = eraseBlank(note);        // 처음, 마지막 z 지우기
        note = combineCode(note);       // 반음 차이나고, 박자가 1인 음표 합치기
        note = combineBeat(note);       // 떨림으로 인해 끊어진 음 합치기
        note = divNote(note);           // 박자 만들기
        sheet = divisionNode(note);     // 마디 나누기

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


    // 옥타브 튀는 음 합치기
    // 오른쪽에 있는 음과 알파벳이 같다면 대문자로.
    // B b B -> B B B
    private List<String> combineOctave(List<String> note){
        String s1, s2;

        for(int i = 0; i < note.size()-1; i++){
            s1 = note.get(i);
            s2 = note.get(i+1);

            if(s1.charAt(0) == '^' && s2.charAt(0) == '^' && s1.substring(1).equalsIgnoreCase(s2.substring(1))) {
                if(s1.charAt(1) < s2.charAt(1)) s2 = '^' + s2.substring(1).toUpperCase();
                else if(s1.charAt(1) > s2.charAt(1)) s1 = '^' + s1.substring(1).toUpperCase();
            }
            else if(s1.equalsIgnoreCase(s2)) {
                if(s1.charAt(0) < s2.charAt(0)) s2 = s2.toUpperCase();
                else if(s1.charAt(0) > s2.charAt(0)) s1 = s1.toUpperCase();
            }

            note.set(i, s1);
            note.set(i+1, s2);
        }

        Log.e("note",note.toString());
        Log.e("combineOctave", note.toString());

        return note;
    }

    // ccc -> c3
    private List<String> makeNote(List<String> note){
        List<String> makeNote = new ArrayList<String>();

        if(!NullNote(note)){
            return makeNote;
        }

        int count = 1;
        int i;
        for(i = 0; i < note.size()-1; i++) {
            if(note.get(i).equals(note.get(i+1))){
                count++;
            } else {
                makeNote.add(note.get(i)+count);
                count = 1;
            }
        }
        makeNote.add(note.get(i)+count);

        Log.e("makeNode", makeNote.toString());

        return makeNote;
    }


    // 처음, 끝 공백 지우기
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
        while(i < note.size() - 1){
            eraseBlank.add(note.get(i));
            i++;
        }

        //마지막 공백 날리기
        if(note.get(i).charAt(0) != 'z') eraseBlank.add(note.get(i));

        Log.e("eraseBlank",eraseBlank.toString());

        return eraseBlank;
    }

    // 반음 차이나는 음 합치기.
    // 음 떨림 제거
    private List<String> combineCode(List<String> note){
        for(int i = 0; i < note.size()-1; i++) {
            String s1 = note.get(i);
            String s2 = note.get(i+1);
            int s1_n, s2_n;

            if(s1.charAt(0) == '^') s1_n = Integer.parseInt(s1.substring(2));
            else s1_n = Integer.parseInt(s1.substring(1));

            if(s2.charAt(0) == '^') s2_n = Integer.parseInt(s2.substring(2));
            else s2_n = Integer.parseInt(s2.substring(1));

            // 숫자 확인해서 1이면 떨림음으로 보고 처리. 더 길게 인식된 음으로 변환
            // 코드는 긴 애 걸로, 박자는 짧은 애 걸로
            if(Math.abs(codeToNum(s1) - codeToNum(s2)) == 5){
                String convert = "";
                if(s1_n == 1){
                    if(s2.charAt(0) == '^') convert = s2.substring(0,2);
                    else convert = Character.toString(s2.charAt(0));
                }
                else if(s2_n == 1){
                    if(s1.charAt(0) == '^') convert = s1.substring(0,2);
                    else convert = Character.toString(s1.charAt(0));
                }
                convert += "1";

                if(s1_n == 1) note.set(i, convert);
                else if(s2_n == 1) note.set(i+1, convert);
            }
        }

        Log.e("combineCode", note.toString());

        return note;
    }

    // 앞 뒤가 같은 코드일 때 박자 합치기
    private List<String> combineBeat(List<String> note){
        List<String> combineNote = new ArrayList<>();

        String currentNote, nextNote, newNote;
        String code = null;
        int sum = 0;
        int cnt = 0;
        boolean isCombine = false;

        int i;
        for(i = 0; i < note.size()-1; i++) {
            currentNote = note.get(i);
            nextNote = note.get(i+1);

            // 현재, 다음 노트 모두 ^코드이고, 숫자가 같으면 code 저장, cnt++, sum구하기
            if(currentNote.charAt(0) == '^' && nextNote.charAt(0) == '^' && currentNote.charAt(1) == nextNote.charAt(1)) {
                code = currentNote.substring(0,2);
                cnt++;
                if(cnt == 1) sum = Integer.parseInt(currentNote.substring(2)) + Integer.parseInt(nextNote.substring(2));
                else sum += Integer.parseInt(nextNote.substring(2));
            }

            // ^아닌 코드
            else if(currentNote.charAt(0) != '^' && nextNote.charAt(0) != '^' && currentNote.charAt(0) == nextNote.charAt(0)) {
                code = Character.toString(currentNote.charAt(0));
                cnt++;
                if(cnt == 1) sum = Integer.parseInt(currentNote.substring(1)) + Integer.parseInt(nextNote.substring(1));
                else sum += Integer.parseInt(nextNote.substring(1));
            }

            // 서로 다른 코드일 때, 새로운 노트에 추가해야함
            else {
                isCombine = true;
            }

            // i-cnt번째부터 i번째까지 같은 코드이므로 결합
            if(isCombine) {
                if(code == null) {
                    combineNote.add(currentNote);
                    isCombine = false;
                    continue;
                }

                newNote = code + sum;
                combineNote.add(newNote);

                code = null;
                cnt = 0;
                sum = 0;
                isCombine = false;
            }
        }

        // 마지막원소
        if(isCombine || (isCombine == false && code == null)) {
            combineNote.add(note.get(note.size()-1));
        } else {
            newNote = code + sum;
            combineNote.add(newNote);
        }

        Log.e("combineBeat", combineNote.toString());

        return combineNote;
    }

    // 음표 박자 생성하기
    private List<String> divNote(List<String> note) {
        List<String> divNote = new ArrayList<String>();
        String currentNote;
        int num;

        for(int i = 0; i < note.size(); i++) {
            currentNote = note.get(i);

            if(currentNote.charAt(0)=='^') num = Integer.parseInt(currentNote.substring(2));
            else num = Integer.parseInt(currentNote.substring(1));

            if(currentNote.charAt(0) == 'z' && num <= 3) continue;

            if(num != 1) num++;
            if(num >= 2) {
                num=num/2;

                if(currentNote.charAt(0)=='^') currentNote = currentNote.substring(0,2) + num;
                else currentNote = Character.toString(currentNote.charAt(0)) + num;

                divNote.add(currentNote);
            }
        }

        Log.e("divNote", divNote.toString());

        return divNote;
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
                // 이음줄삽입
                sheet += currentNote + Integer.toString(currentBeat-temp) + "-" + "|";
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


    // 코드를 숫자로 변환
    private static int codeToNum(String code) {
        char currentCode;
        int toNum = 0;

        // #붙은 코드 숫자 변환
        if(code.charAt(0) == '^') {
            currentCode = code.charAt(1);
            if(currentCode == 'C') toNum = 15;
            else if(currentCode == 'D') toNum = 25;
            else if(currentCode == 'F') toNum = 40;
            else if(currentCode == 'G') toNum = 50;
            else if(currentCode == 'A') toNum = 60;
            else if(currentCode == 'c') toNum = 75;
            else if(currentCode == 'd') toNum = 85;
            else if(currentCode == 'f') toNum = 100;
            else if(currentCode == 'g') toNum = 110;
            else if(currentCode == 'a') toNum = 120;
            else if(currentCode == 'z') toNum = 0;
        }
        // #붙지 않은 코드 숫자 변환
        else {
            currentCode = code.charAt(0);
            if(currentCode == 'C') toNum = 10;
            else if(currentCode == 'D') toNum = 20;
            else if(currentCode == 'E') toNum = 30;
            else if(currentCode == 'F') toNum = 35;
            else if(currentCode == 'G') toNum = 45;
            else if(currentCode == 'A') toNum = 55;
            else if(currentCode == 'B') toNum = 65;
            else if(currentCode == 'c') toNum = 70;
            else if(currentCode == 'd') toNum = 80;
            else if(currentCode == 'e') toNum = 90;
            else if(currentCode == 'f') toNum = 95;
            else if(currentCode == 'g') toNum = 105;
            else if(currentCode == 'a') toNum = 115;
            else if(currentCode == 'b') toNum = 125;
            else if(currentCode == 'z') toNum = 0;
        }
        return toNum;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}