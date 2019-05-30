package com.a7f.drawingsound;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LicenseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        TextView icon1 = (TextView) findViewById(R.id.TextViewFontDescription);
        TextView icon2 = (TextView) findViewById(R.id.TextViewFontDescription2);
        String text = "Icon made by Freepik from www.flaticon.com";
        String text2 = "Icon made by Eucalyp from www.flaticon.com";
        icon1.setText(text);
        icon2.setText(text2);

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern = Pattern.compile("Freepik");
        Pattern pattern2 = Pattern.compile("Eucalyp");
        Pattern pattern3 = Pattern.compile("www.flaticon.com");

        Linkify.addLinks(icon1, pattern, "https://www.freepik.com/",null,mTransform);
        Linkify.addLinks(icon2,pattern2, "https://www.flaticon.com/authors/eucalyp/",null,mTransform);
        Linkify.addLinks(icon1,pattern3,"https://www.flaticon.com/",null,mTransform);
        Linkify.addLinks(icon2,pattern3,"https://www.flaticon.com/",null,mTransform);

        setTitle("");
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(LicenseActivity.this, SetActivity.class);

        //startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    private void signOut() {

        new AlertDialog.Builder(this).setTitle("LOGOUT").setMessage("로그아웃 하시겠습니까?").setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent i = new Intent(LicenseActivity.this,SigninActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout :
                signOut();
                return true ;
            case R.id.action_license :
                Intent intent = new Intent(LicenseActivity.this,LicenseActivity.class);
                finish();
                startActivity(intent);
                return true;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}


