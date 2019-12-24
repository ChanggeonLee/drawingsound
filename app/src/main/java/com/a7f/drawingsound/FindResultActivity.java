package com.a7f.drawingsound;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


    private TextView TextViewFindArtist;
    private TextView TextViewMusic;

    private TextView TextViewAlbumTitle0;
    private TextView TextViewAlbumTitle1;
    private TextView TextViewAlbumTitle2;

    private ImageView ImageViewAlbumImg0;
    private ImageView ImageViewAlbumImg1;
    private ImageView ImageViewAlbumImg2;

    private FirebaseAuth mAuth;

    boolean backFlag;

    private AlbumData albumList;
    private Album albumData;
    private String[] results;
    private String imageUrl;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_result);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        result = intent.getExtras().getString("result");
        results = result.split("/");

        albumList = new AlbumData();

        setTitle("");

        setHandler();

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);
    }

    private void setHandler(){
        TextViewAlbumTitle0 = (TextView)findViewById(R.id.TextViewAlbumTitle0);
        TextViewAlbumTitle1 = (TextView)findViewById(R.id.TextViewAlbumTitle1);
        TextViewAlbumTitle2 = (TextView)findViewById(R.id.TextViewAlbumTitle2);
        ImageViewAlbumImg0 = (ImageView)findViewById(R.id.ImageViewAlbumImg0);
        ImageViewAlbumImg1 = (ImageView)findViewById(R.id.ImageViewAlbumImg1);
        ImageViewAlbumImg2 = (ImageView)findViewById(R.id.ImageViewAlbumImg2);
        TextViewMusic = (TextView)findViewById(R.id.TextViewMusic);

        if(!result.trim().equals("error")){
            albumData = albumList.findItems(results[0]);
            TextViewAlbumTitle0.setText(albumData.getTitle()+" "+albumData.getArtist());
            imageUrl = albumData.getImg();
            Glide.with(this).load(imageUrl).into(ImageViewAlbumImg0);

            albumData = albumList.findItems(results[1]);
            TextViewAlbumTitle1.setText(albumData.getTitle()+" "+albumData.getArtist());
            imageUrl = albumData.getImg();
            Glide.with(this).load(imageUrl).into(ImageViewAlbumImg1);

            albumData = albumList.findItems(results[2]);
            TextViewAlbumTitle2.setText(albumData.getTitle()+" "+albumData.getArtist());
            imageUrl = albumData.getImg();
            Glide.with(this).load(imageUrl).into(ImageViewAlbumImg2);
            TextViewMusic.setText("비슷한 노래는 입니다.");

        }else {
            TextViewAlbumTitle0.setText("비슷한 노래를 찾을 수 없습니다.");
            TextViewMusic.setText("잠시후에 다시 시도해주세요.");
        }

        ButtonRetry = (Button)findViewById(R.id.ButtonRetry);
        ButtonRetry.setOnClickListener(RetryClickListener);

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
