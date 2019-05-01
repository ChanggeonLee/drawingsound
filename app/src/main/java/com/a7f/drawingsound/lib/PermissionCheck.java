package com.a7f.drawingsound.lib;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermissionCheck {
    // 사용자 권한 확인 필요 변수
    static final int RECORD_PERMISSON=1;
    static final int READ_PERMISSON=1;
    static final int WRITE_PERMISSON=1;

    public void PRecord(Activity main){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(main, Manifest.permission.RECORD_AUDIO );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "Record 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "Record 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(main, Manifest.permission.RECORD_AUDIO)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(main.getApplicationContext(), "Record 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSON);
            }
        }
    }

     public void PRead(Activity main){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(main, Manifest.permission.READ_EXTERNAL_STORAGE );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "read 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "read 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(main, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(main.getApplicationContext(), "read 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSON);
            }
        }
    }

    public void PWrite(Activity main){
        // 권한 부여 되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(main, Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //            Toast.makeText(getApplicationContext(), "write 권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            //            Toast.makeText(getApplicationContext(), "write 권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(main, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(main.getApplicationContext(), "write 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(main, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSON);
            }
        }
    }
}
