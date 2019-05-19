package com.a7f.drawingsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a7f.drawingsound.lib.NetworkCheck;
import com.a7f.drawingsound.lib.PermissionCheck;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {

    Button ButtonLogin;
    Button ButtonSignup;
    EditText EditTextEmail;
    EditText EditTextPasswd;

    private FirebaseAuth mAuth;
    private NetworkCheck networkcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);

        PermissionCheck permissioncheck = new PermissionCheck();

        permissioncheck.PRead(SigninActivity.this);
        permissioncheck.PRecord(SigninActivity.this);
        permissioncheck.PWrite(SigninActivity.this);

        setHandler();
    }

    private void setHandler(){
        ButtonLogin = (Button)findViewById(R.id.ButtonLogin);
        ButtonSignup = (Button)findViewById(R.id.ButtonSignup);

        EditTextEmail = (EditText)findViewById(R.id.EditTextEmail);
        EditTextPasswd = (EditText)findViewById(R.id.EditTextPasswd);
        EditTextPasswd.setTransformationMethod(new PasswordTransformationMethod());

        ButtonLogin.setOnClickListener(LoginClickListener);
        ButtonSignup.setOnClickListener(SignupClickListener);

        mAuth = FirebaseAuth.getInstance();

        networkcheck = new NetworkCheck(SigninActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(SigninActivity.this,SetActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String email, String password) {
        final String TAG = "LoginWithEmail";
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(SigninActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(SigninActivity.this,SetActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed. 이메일과 비번확인",
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

            if(!email.isEmpty() && !passwd.isEmpty()){
                if(networkcheck.checkNetwork()) {
                    signIn(email, passwd);
                }
            }else{
                Toast.makeText(SigninActivity.this, "이메일과 비밀번호 입력하세요",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener SignupClickListener = new View.OnClickListener() {
        public void onClick(View v){
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            if(networkcheck.checkNetwork()) {
                startActivity(intent);
            }
        }
    };
}
