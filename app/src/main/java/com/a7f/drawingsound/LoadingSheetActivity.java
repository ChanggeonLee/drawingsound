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
        int count = 0;

        sheet = "|";

        for (int i = 0 ; i < note.length() ; i++){

            if(count != 8 && note.charAt(i) != 'X'){
                sheet += note.charAt(i);
                count++;
            }else if(note.charAt(i) != 'X'){
                sheet += "|";
                sheet += note.charAt(i);
                count = 0;
            }

//            if(temp == ' '){
//                temp = note.charAt(i);
//            } else if(temp == note.charAt(i)) {
//                count++;
//            } else if(temp == note.charAt(i) && count == 3){
//
//            }
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
