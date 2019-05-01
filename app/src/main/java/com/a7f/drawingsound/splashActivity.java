package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashActivity extends AppCompatActivity {

    Intent intent;

    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        t = new Thread() {
            public void run() {
                try{
                    sleep(1000);
                    intent = new Intent(splashActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    //
                }
            }
        };

        t.start();
    }

    public void splashScreen (final int x) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intent = new Intent(splashActivity.this, SigninActivity.class);

//                startActivity(intent);
//                finish();
//
                startActivity(intent);
                finish();
            }
        }).run();
    }
}
