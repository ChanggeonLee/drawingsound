package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewScore extends AppCompatActivity {
    private WebView WebViewScore;
    private WebSettings WebSettinsScore;
    private String uri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_score);
        setTitle("");

        WebViewScore = (WebView)findViewById(R.id.WebViewScore);
        WebViewScore.setWebViewClient(new WebViewClient());
        WebSettinsScore = WebViewScore.getSettings();
        WebSettinsScore.setJavaScriptEnabled(true);

        uri = sheetUri();

        WebViewScore.loadUrl(uri);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        mAuth = FirebaseAuth.getInstance();
    }

    private String sheetUri(){
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
        mAuth.signOut();
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
                Intent intent = new Intent(ViewScore.this, SigninActivity.class);
                startActivity(intent);
                finish();
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}
