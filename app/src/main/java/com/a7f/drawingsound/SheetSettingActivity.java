package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.a7f.drawingsound.model.Sheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SheetSettingActivity extends AppCompatActivity {

    Button ButtonPrev;
    Button ButtonSet;
    EditText EditTextTitle;
    EditText EditTextComposer;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

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
            String title, composer;

            title = EditTextTitle.getText().toString();
            composer = EditTextComposer.getText().toString();

            if(!title.isEmpty() && !composer.isEmpty()){

                try {
                    Sheet sheet = new Sheet(title, composer);
                    myRef.child("sheets").child(currentUser.getUid()).push().setValue(sheet);
                }catch (Exception e){
                    //
                }

                Intent intent = new Intent(SheetSettingActivity.this,SheetListActivity.class);
                startActivity(intent);
            }
        }
    };
}
