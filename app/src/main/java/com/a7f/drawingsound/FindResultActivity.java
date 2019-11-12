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

import com.a7f.drawingsound.data.AlbumData;
import com.a7f.drawingsound.model.Album;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;


public class FindResultActivity extends AppCompatActivity {
    // layout element
    private Button ButtonRetry;
    private Button ButtonHome;


    private TextView TextViewFindDescription;
    private TextView TextViewFindArtist;
    private TextView TextViewMusic;

    private ImageView ImageViewHumIcon;

    private FirebaseAuth mAuth;

    boolean backFlag;

    private AlbumData albumList;
    private Album albumData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_result);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String result = intent.getExtras().getString("result");
        Log.e("result", result);
        albumList = new AlbumData();
        albumData = albumList.findItems(result);

        setTitle("");

        setHandler();

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

    private void setHandler(){
        TextViewFindDescription = (TextView)findViewById(R.id.TextViewFindDescription);
        TextViewFindDescription.setText(albumData.getTitle());

        TextViewFindArtist = (TextView)findViewById(R.id.TextViewFindArtist);
        TextViewFindArtist.setText(albumData.getArtist());

        ImageViewHumIcon = (ImageView)findViewById(R.id.mock_up);
        String imageUrl = albumData.getImg();
        Glide.with(this).load(imageUrl).into(ImageViewHumIcon);

        TextViewMusic = (TextView)findViewById(R.id.TextViewMusic);
        TextViewMusic.setText("비슷한 노래는 " + albumData.getTitle() + " 입니다.");

        ButtonRetry = (Button)findViewById(R.id.ButtonRetry);
        ButtonRetry.setOnClickListener(RetryClickListener);

//        ButtonHome = (Button)findViewById(R.id.ButtonHome);
//        ButtonHome.setOnClickListener(HomeClickListener);

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

//    Button.OnClickListener HomeClickListener = new View.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            finish();
//        }
//    };

    @Override
    public void onBackPressed() {
        if(backFlag){
            super.onBackPressed();
        }else{
            Toast.makeText(getApplicationContext(),"하단의 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
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
