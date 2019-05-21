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
import android.widget.TextView;
import android.widget.Toast;

import com.a7f.drawingsound.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    Button ButtonSave;
    Button ButtonCancel;
    EditText EditTextPasswd;
    EditText EditTextEmail;
    EditText EditTextName;

    private String email, passwd, name;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        settingDB();
        settingAuth();
        settingHandler();
    }

    private void settingDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void settingAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

    private void settingHandler(){
        ButtonSave = (Button)findViewById(R.id.ButtonSave);
        ButtonCancel = (Button)findViewById(R.id.ButtonCancel);

        EditTextPasswd = (EditText)findViewById(R.id.EditTextPasswd);
        EditTextPasswd.setTransformationMethod(new PasswordTransformationMethod());

        EditTextEmail = (EditText)findViewById(R.id.EditTextEmail);

        EditTextName = (EditText)findViewById(R.id.EditTextName);

        ButtonSave.setOnClickListener(SaveClick);
        ButtonCancel.setOnClickListener(CancelClick);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignupActivity.this,SetActivity.class);
            startActivity(intent);
        }
    }

    private void saveUserInfo(FirebaseUser Fuser){
        String uid;
        uid = Fuser.getUid();
        Log.d("getuserid", uid);
        try {
            User user = new User(name, email);
            myRef.child("users").child(uid).setValue(user);
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Log.d("auth error:",e.toString() );
            //
        }
    }

    private void createAccount(String email, String password) {
        final String TAG = "EmailPassword";
        Log.d(TAG, "createAccount:" + email);
        Log.d(TAG, "createAccount:" + password);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("UserID",user.getUid());
                        saveUserInfo(user);
                        Log.d(TAG, "createUserWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            TextView mTxtPassword = null;
                            Toast.makeText(SignupActivity.this, "비밀번호 6자리 입력해주세요",
                                    Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(SignupActivity.this, "존재하지 않는 이메일입니다",
                                    Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignupActivity.this, "이미 있는 계정입니다",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
        });
    }



    Button.OnClickListener SaveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            email = EditTextEmail.getText().toString();
            passwd = EditTextPasswd.getText().toString();
            name = EditTextName.getText().toString();

            if(!email.isEmpty() && !passwd.isEmpty() && !name.isEmpty()){
                createAccount(email,passwd);
            }else if(email.isEmpty()){
                Toast.makeText(SignupActivity.this, "이메일을 입력하세요",
                        Toast.LENGTH_SHORT).show();
            }else if(passwd.isEmpty()){
                Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요",
                        Toast.LENGTH_SHORT).show();
            }else if(name.isEmpty()){
                Toast.makeText(SignupActivity.this, "이름을 입력하세요",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener CancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}


