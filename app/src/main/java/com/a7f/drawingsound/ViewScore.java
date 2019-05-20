package com.a7f.drawingsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_score);

        WebViewScore = (WebView)findViewById(R.id.WebViewScore);
        WebViewScore.setWebViewClient(new WebViewClient());
        WebSettinsScore = WebViewScore.getSettings();
        WebSettinsScore.setJavaScriptEnabled(true);

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference().child("sheets");

        String uid = FirebaseAuth.getInstance().getUid();
        String key = "-LfIunUroLqPGFakqFSZ";

        uri = "http://drawingsound.com:8888/sheet/msheet?key=" + key + "&uid=" + uid;

        WebViewScore.loadUrl(uri);
    }
}
