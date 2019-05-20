package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

    private void setHandler() {
        mAuth = FirebaseAuth.getInstance();

        ButtonList = (Button)findViewById(R.id.ButtonList);
        ButtonMake = (Button)findViewById(R.id.ButtonMake);


        ButtonList.setOnClickListener(ListClick);
        ButtonMake.setOnClickListener(MakeClick);

    }

    private void signOut() {
        mAuth.signOut();
    }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout :
                signOut();
                Intent intent = new Intent(SetActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}
