package com.a7f.drawingsound;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class FindResultActivity extends AppCompatActivity {
    // layout element
    Button ButtonRetry;
    TextView TextViewFindDescription;
    ImageView ImageViewHumIcon;

    private FirebaseAuth mAuth;

    boolean backFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_result);
        TextViewFindDescription = findViewById(R.id.TextViewFindDescription);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String result = intent.getExtras().getString("result");
        TextViewFindDescription.setText(result);

        setTitle("");

        setHandler();

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

    private void setHandler(){
        ButtonRetry = (Button)findViewById(R.id.ButtonRetry);
        TextViewFindDescription = (TextView)findViewById(R.id.TextViewFindDescription);
        ImageViewHumIcon = (ImageView)findViewById(R.id.ImageViewHumIcon);
        ButtonRetry.setOnClickListener(RetryClickListener);
        ButtonRetry.setEnabled(true);
        ButtonRetry.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkblue));

        backFlag = true;

        mAuth = FirebaseAuth.getInstance();
    }

    Button.OnClickListener RetryClickListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
                Intent intent = new Intent(FindResultActivity.this, FindMusicActivity.class);
                startActivity(intent);
                finish();
        }
    };

    @Override
    public void onBackPressed() {
        if(backFlag){
            super.onBackPressed();
        }else{
            //
            Toast.makeText(getApplicationContext(),"녹음 중입니다. 종료 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    private void signOut() {

        new AlertDialog.Builder(this).setTitle("LOGOUT").setMessage("로그아웃 하시겠습니까?").setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent i = new Intent(FindResultActivity.this,SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
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
                return true ;
            case R.id.action_license :
                Intent intent = new Intent(FindResultActivity.this,LicenseActivity.class);
                startActivity(intent);
                //finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
