package com.a7f.drawingsound;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button ButtonLogin;
    Button ButtonSignup;


    // 사용자 권한 확인 필요 변수
    static final int RECORD_PERMISSON=1;
    static final int READ_PERMISSON=1;
    static final int WRITE_PERMISSON=1;

    public void PRecord(){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "Record 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "Record 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "Record 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSON);
            }
        }
    }

    public void PRead(){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "read 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "read 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "read 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSON);
            }
        }
    }

    public void PWrite(){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "write 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "write 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "write 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSON);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PRead();
        PRecord();
        PWrite();

        ButtonLogin = (Button)findViewById(R.id.ButtonLogin);
        ButtonSignup = (Button)findViewById(R.id.ButtonSignup);

        ButtonLogin.setOnClickListener(LoginClickListener);
        ButtonSignup.setOnClickListener(SignupClickListener);
    }

    Button.OnClickListener LoginClickListener = new View.OnClickListener() {
        public void onClick(View v){
             Intent intent=new Intent(MainActivity.this,SetActivity.class);
             startActivity(intent);
        }
    };

    Button.OnClickListener SignupClickListener = new View.OnClickListener() {
        public void onClick(View v){
            //할일 적어주세염
        }
    };


}
