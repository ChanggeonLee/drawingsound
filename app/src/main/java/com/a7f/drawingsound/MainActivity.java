package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button ButtonLogin;
    Button ButtonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonLogin = (Button)findViewById(R.id.ButtonLogin);
        ButtonSignup = (Button)findViewById(R.id.ButtonSignup);

        ButtonLogin.setOnClickListener(LoginClickListener);
        ButtonSignup.setOnClickListener(SignupClickListener);


    }

    Button.OnClickListener LoginClickListener = new View.OnClickListener() {
        public void onClick(View v){
            Intent intent=new Intent(MainActivity.this,HummingActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener SignupClickListener = new View.OnClickListener() {
        public void onClick(View v){
            //할일 적어주세염
        }
    };


}
