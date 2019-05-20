package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SetActivity extends AppCompatActivity {

    Button ButtonList;
    Button ButtonMake;
    Button ButtonSignout;
    Button ButtonTest;

    private FirebaseAuth mAuth;

    private long backKeyPressedTime = 0;
    private Toast toast;

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
            Intent intent = new Intent(SetActivity.this,MoodListActivity.class);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}
