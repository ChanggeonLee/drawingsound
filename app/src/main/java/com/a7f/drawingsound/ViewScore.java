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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewScore extends AppCompatActivity {
    private WebView WebViewScore;
    private WebSettings WebSettinsScore;
    private String uri;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button ButtonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_score);
        setTitle("");

        WebViewScore = (WebView) findViewById(R.id.WebViewScore);
        WebViewScore.setWebViewClient(new WebViewClient());
        WebSettinsScore = WebViewScore.getSettings();
        WebSettinsScore.setJavaScriptEnabled(true);
        WebSettinsScore.setSupportZoom(true);
        uri = sheetUri();

        WebViewScore.loadUrl(uri);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        mAuth = FirebaseAuth.getInstance();
        settingDB();
        setHandler();
    }

    private String sheetUri() {
        String uri;
        String key;
        String uid;

        Intent intent = getIntent();
        key = intent.getStringExtra("sheetKey");
        uid = FirebaseAuth.getInstance().getUid();

        uri = "http://drawingsound.com:8888/sheet/msheet?key=" + key + "&uid=" + uid;

        return uri;
    }


    private void signOut() {

        new AlertDialog.Builder(this).setTitle("LOGOUT").setMessage("로그아웃 하시겠습니까?").setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent i = new Intent(ViewScore.this,SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
//                Intent intent = new Intent(ViewScore.this, SigninActivity.class);
//                startActivity(intent);
//                finish();
                return true;
            case R.id.action_license :
                Intent intent = new Intent(ViewScore.this,LicenseActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setHandler() {
        ButtonDelete = (Button) findViewById(R.id.ButtonDelete);
        ButtonDelete.setOnClickListener(DeleteClickListener);
    }

    private void deleteSheet() {

        new AlertDialog.Builder(this).setTitle("DELETE").setMessage("악보를 정말 삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String uid = mAuth.getUid();
                String key;
                Intent intent = getIntent();
                key = intent.getStringExtra("sheetKey");
                myRef.child("sheets").child(uid).child(key).removeValue();
                finish();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    Button.OnClickListener DeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            deleteSheet();
//            String uid = mAuth.getUid();
//            String key;
//            Intent intent = getIntent();
//            key = intent.getStringExtra("sheetKey");
//            myRef.child("sheets").child(uid).child(key).removeValue();
//            finish();
        }
    };


    private void settingDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

}
