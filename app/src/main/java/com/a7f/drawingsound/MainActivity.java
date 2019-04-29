package com.a7f.drawingsound;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button ButtonLogin;
    Button ButtonSignup;
    EditText EditTextEmail;
    EditText EditTextPasswd;


    // 사용자 권한 확인 필요 변수
    static final int RECORD_PERMISSON=1;
    static final int READ_PERMISSON=1;
    static final int WRITE_PERMISSON=1;

    private static final String TAG = "LoginWithEmail";

    private FirebaseAuth mAuth;

    private void PRecord(){
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

    private void PRead(){
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

    private void PWrite(){
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

    private void NetworkError(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("Network 연결 후 다시 시도해주세요");
        alert.show();

    }


    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private boolean checkNetwork(){
        try {
            NetworkInfo networkInfo = getNetworkInfo();
            if (networkInfo.isConnected()) {
                return true;
            }
        } catch (NullPointerException ne){
            NetworkError();
        }
        return false;
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

        EditTextEmail = (EditText)findViewById(R.id.EditTextEmail);
        EditTextPasswd = (EditText)findViewById(R.id.EditTextPasswd);
        EditTextPasswd.setTransformationMethod(new PasswordTransformationMethod());

        ButtonLogin.setOnClickListener(LoginClickListener);
        ButtonSignup.setOnClickListener(SignupClickListener);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(MainActivity.this,SetActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent=new Intent(MainActivity.this,SetActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }


    Button.OnClickListener LoginClickListener = new View.OnClickListener() {
        public void onClick(View v){

            String email,passwd;

            email = EditTextEmail.getText().toString();
            passwd = EditTextPasswd.getText().toString();
            if(checkNetwork()) {
                signIn(email, passwd);
            }
        }
    };

    Button.OnClickListener SignupClickListener = new View.OnClickListener() {
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            if(checkNetwork()) {
                startActivity(intent);
            }
        }
    };
}
