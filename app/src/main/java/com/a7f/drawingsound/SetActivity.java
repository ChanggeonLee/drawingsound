package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SetActivity extends AppCompatActivity {

    Button ButtonList;
    Button ButtonMake;
    Button ButtonSignout;
    Button ButtonTest;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        setHandler();
    }

    private void setHandler() {
        mAuth = FirebaseAuth.getInstance();

        ButtonList = (Button)findViewById(R.id.ButtonList);
        ButtonMake = (Button)findViewById(R.id.ButtonMake);

        ButtonSignout = (Button)findViewById(R.id.ButtonSignout);
        ButtonSignout.setOnClickListener(SignoutClick);

        ButtonList.setOnClickListener(ListClick);
        ButtonMake.setOnClickListener(MakeClick);

        ButtonTest = (Button)findViewById(R.id.ButtonTest);
        ButtonTest.setOnClickListener(TestClick);
    }

    private void signOut() {
        mAuth.signOut();
    }

    Button.OnClickListener TestClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetActivity.this, ViewScore.class);
            startActivity(intent);
            finish();
        }
    };


    Button.OnClickListener SignoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signOut();
            Intent intent = new Intent(SetActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
    };

    Button.OnClickListener ListClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetActivity.this,SheetListActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener MakeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetActivity.this,HummingFFTActivity.class);
            startActivity(intent);
        }
    };

}
