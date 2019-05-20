package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.a7f.drawingsound.model.Sheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SheetSettingActivity extends AppCompatActivity {

    Button ButtonPrev;
    Button ButtonSet;
    EditText EditTextTitle;
    EditText EditTextComposer;
    EditText EditTextDate;
    EditText EditTextMood;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

//    private final long FINISH_INTERVAL_TIME = 2000;
//    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_setting);
        settingDB();
        settingHandler();
        settingAuth();
    }

    private void settingDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void settingHandler(){
        ButtonPrev = (Button)findViewById(R.id.ButtonPrev);
        ButtonSet = (Button)findViewById(R.id.ButtonSet);
        EditTextTitle = (EditText)findViewById(R.id.EditTextTitle);
        EditTextComposer = (EditText)findViewById(R.id.EditTextComposer);
        //EditTextDate = (EditText)findViewById(R.id.EditTextDate);
        //EditTextMood = (EditText)findViewById(R.id.EditTextMood);


        ButtonPrev.setOnClickListener(Prevonclick);
        ButtonSet.setOnClickListener(Setclick);
    }

    private void settingAuth(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    Button.OnClickListener Prevonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SheetSettingActivity.this,HummingFFTActivity.class);
            finish();
            startActivity(intent);
        }
    };

    Button.OnClickListener Setclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title, composer, url, date, mood;

            title = EditTextTitle.getText().toString();
            composer = EditTextComposer.getText().toString();
            url = "https://firebasestorage.googleapis.com/v0/b/drawingsound-1d381.appspot.com/o/sheet.PNG?alt=media&token=f0fbfc88-f562-44bd-a5c9-abf5c66e8bdb";
            long now = System.currentTimeMillis();
            Date formatDate = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
            date = sdfNow.format(formatDate);
            // date = EditTextDate.getText().toString();

            final RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup);
            int id = rg.getCheckedRadioButtonId();
            RadioButton rb = (RadioButton)findViewById(id);


            if(!title.isEmpty() && !composer.isEmpty() && rb != null){
                mood = rb.getText().toString();
                try {
                    Intent intent = getIntent();
                    String sheetdata = intent.getExtras().getString("Sheet");
//                    String mobilesheetdata = intent.getExtras().getString("MobileSheet");
                    Log.e("sheetdata",sheetdata);

                    Sheet sheet = new Sheet(title, composer, url, date, mood, sheetdata);

                    myRef.child("sheets").child(currentUser.getUid()).push().setValue(sheet);
                }catch (Exception e){
                    //
                    Log.e("firebasesaveerror",e.getMessage());
                }

                Intent intent = new Intent(SheetSettingActivity.this,MoodListActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "정보를 입력해주세요.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
